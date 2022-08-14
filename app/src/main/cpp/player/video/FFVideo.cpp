//
// Created by zjh on 2022/8/13.
//

#include "FFVideo.h"

ANativeWindow *aNativeWindow;


FFVideo::FFVideo(int index, FFJniCallback *ffJniCallback, FFPlayStatus *ffPlayStatus,
                 FFAudio *ffAudio) : FFMedia(
        index, ffJniCallback, ffPlayStatus) {
    this->packetQueue = new PacketQueue();
    this->ffAudio = ffAudio;

}

FFVideo::~FFVideo() {
    FFMedia::~FFMedia();

}

void FFVideo::privateAnalysisStream(AVFormatContext *avFormatContext,
                                    Thread_Mode threadMode) {

    /**
     * 1、初始化 context
     * struct SwsContext *sws_getContext(int srcW, int srcH, enum AVPixelFormat srcFormat,
                                  int dstW, int dstH, enum AVPixelFormat dstFormat,
                                  int flags, SwsFilter *srcFilter,
                                  SwsFilter *dstFilter, const double *param);
     */
    swsContext = sws_getContext(avCodecContext->width, avCodecContext->height,
                                avCodecContext->pix_fmt, avCodecContext->width,
                                avCodecContext->height, AV_PIX_FMT_RGBA, SWS_BILINEAR,
                                NULL, NULL, NULL);
    rgbFrame = av_frame_alloc();
    frameSize = av_image_get_buffer_size(AV_PIX_FMT_RGBA, avCodecContext->width,
                                         avCodecContext->height, 1);
    frameBuff = (uint8_t *) malloc(frameSize);
    /**
     * int av_image_fill_arrays(uint8_t *dst_data[4], int dst_linesize[4],
                         const uint8_t *src, '+5F
                         enum AVPixelFormat pix_fmt, int width, int height, int align);
     */
    av_image_fill_arrays(rgbFrame->data, rgbFrame->linesize, frameBuff, AV_PIX_FMT_RGBA,
                         avCodecContext->width,
                         avCodecContext->height, 1);
    int den = avFormatContext->streams[index]->avg_frame_rate.den;
    int num = avFormatContext->streams[index]->avg_frame_rate.num;

    if (den != 0 && num != 0) {
        defaultDelayTime = 1.0f * den / num;
    }

}

void *playVideoThread(void *context) {
    FFVideo *ffVideo = (FFVideo *) context;
    AVFrame *pFrame = av_frame_alloc();

    /*
     * ANativeWindow 的缓冲区
     * */
    ANativeWindow_Buffer outBuffer;

    JNIEnv *p_env;
    if (ffVideo->ffJniCallback->vm->AttachCurrentThread(&p_env, 0) != JNI_OK) {
        XLOGE("Get child thread env error");
    }

    aNativeWindow = ANativeWindow_fromSurface(p_env, ffVideo->surface);
    ffVideo->ffJniCallback->vm->DetachCurrentThread();
    ANativeWindow_setBuffersGeometry(aNativeWindow, ffVideo->avCodecContext->width,
                                     ffVideo->avCodecContext->height, WINDOW_FORMAT_RGBA_8888);

    while (ffVideo->ffPlayStatus && !ffVideo->ffPlayStatus->isExit) {
        ffVideo->pPacket = ffVideo->packetQueue->pop();
        // Packet 包，压缩的数据，解码成 pcm 数据
        int codecSendPacketRes = avcodec_send_packet(ffVideo->avCodecContext, ffVideo->pPacket);
        if (codecSendPacketRes == 0) {
            int codecReceiveFrameRes = avcodec_receive_frame(ffVideo->avCodecContext, pFrame);
            if (codecReceiveFrameRes == 0) {
                /**
                 * int sws_scale(struct SwsContext *c, const uint8_t *const srcSlice[],
              const int srcStride[], int srcSliceY, int srcSliceH,
              uint8_t *const dst[], const int dstStride[]);
                 */
                sws_scale(ffVideo->swsContext, pFrame->data, pFrame->linesize, 0,
                          ffVideo->avCodecContext->height,
                          ffVideo->rgbFrame->data, ffVideo->rgbFrame->linesize);

                double sleepTime = ffVideo->getFrameSleepTime(pFrame) * 1000000;
                XLOGE("sleepTime-->%lf", sleepTime);
                av_usleep(sleepTime);
                ANativeWindow_lock(aNativeWindow, &outBuffer, NULL);
                memcpy(outBuffer.bits, ffVideo->frameBuff, ffVideo->frameSize);
                ANativeWindow_unlockAndPost(aNativeWindow);
            }
        }
        // 解引用
        av_packet_unref(ffVideo->pPacket);
        av_frame_unref(pFrame);
    }
    // 1. 解引用数据 data ， 2. 销毁 pPacket 结构体内存  3. pPacket = NULL
    av_packet_free(&ffVideo->pPacket);
    av_frame_free(&pFrame);
    return 0;
}


void FFVideo::play() {

//1、一个线程去播放
    pthread_t playTid = NULL;
    pthread_create(&playTid, NULL, playVideoThread, this);
    pthread_detach(playTid);
}

void FFVideo::release() {

    if (swsContext) {
        sws_freeContext(swsContext);
        free(swsContext);
        swsContext = NULL;
    }

    if (frameBuff) {
        free(frameBuff);
        frameBuff = NULL;
    }

    if (rgbFrame) {
        av_frame_free(&rgbFrame);
        rgbFrame = NULL;
    }

    if (surface && ffJniCallback) {
        ffJniCallback->env->DeleteGlobalRef(surface);
        surface = NULL;
    }
}

void FFVideo::setSurface(JNIEnv *pEnv, jobject surface) {
    this->surface = pEnv->NewGlobalRef(surface);

}

double FFVideo::getFrameSleepTime(AVFrame *pFrame) {
    double time = av_frame_get_best_effort_timestamp(pFrame) * av_q2d(time_base);
    if (time > currentTime) {
        currentTime = time;
    }

    double diffTime = ffAudio->getSynTime() - currentTime;
    XLOGE("diffTime-->%lf", diffTime);
    //1、第一次控制在-0.016--0.016，因为安卓一秒刷60帧，一帧是0.016
    if (abs(diffTime) > 0.016) {
        if (diffTime > 0.016) {
            delayTime = delayTime * 2 / 3;
        } else if (diffTime < -0.016) {
            delayTime = delayTime * 3 / 2;
        }

        //2、第二次控制
        if (delayTime < defaultDelayTime / 2) {
            delayTime = defaultDelayTime * 2 / 3;
        } else if (delayTime > defaultDelayTime * 2) {
            delayTime = defaultDelayTime * 3 / 2;
        }
    }

    //3、第三次控制，基本是出错的情况
    if (diffTime >= 0.25) {
        delayTime = 0;
    } else if (diffTime <= -0.25) {
        delayTime = defaultDelayTime * 2;
    }

    return delayTime;
}
