//
// Created by zjh on 2022/7/23.
//

#include "FFmpeg.h"


FFmpeg::FFmpeg(FFJniCallback *ffJniCallback) : ffJniCallback(ffJniCallback) {

}

FFmpeg::~FFmpeg() {
    release();
}

bool FFmpeg::prepare(const char *url) {
    av_register_all();
    avformat_network_init();
    int result;
    int audioIndex = 0;
    result = avformat_open_input(&avFormatContext, url, NULL, NULL);
    XLOGE("result-->value-->%s,%d", av_err2str(result), result);
    if (result) {
        ffJniCallback->onErrorListener(THREAD_MAIN, result, av_err2str(result));
        release();
        return false;
    }
    XLOGE("result-->value-->%s,%d", av_err2str(result), result);
    result = avformat_find_stream_info(avFormatContext, NULL);
    if (result) {
        ffJniCallback->onErrorListener(THREAD_MAIN, result, av_err2str(result));
        release();
        return false;
    }
    audioIndex = av_find_best_stream(avFormatContext, AVMEDIA_TYPE_AUDIO, -1, -1, NULL, 0);
    if (audioIndex < 0) {
        ffJniCallback->onErrorListener(THREAD_MAIN, result, av_err2str(result));
        release();
        return false;
    }
    AVCodec *audioCodec = avcodec_find_decoder(
            avFormatContext->streams[audioIndex]->codecpar->codec_id);
    avCodecContext = avcodec_alloc_context3(audioCodec);
    result = avcodec_parameters_to_context(avCodecContext,
                                           avFormatContext->streams[audioIndex]->codecpar);
    if (result < 0) {
        XLOGE("result-->value-->%s,%d", av_err2str(result), 47);
        ffJniCallback->onErrorListener(THREAD_MAIN, result, av_err2str(result));
        release();
        return false;
    }
    result = avcodec_open2(avCodecContext, audioCodec, NULL);
    if (result) {
        XLOGE("result-->value-->%s,%d", av_err2str(result), 53);
        ffJniCallback->onErrorListener(THREAD_MAIN, result, av_err2str(result));
        release();
        return false;
    }
    XLOGE("result-->value-->%s,%d", av_err2str(result), result);
    audioTrack = ffJniCallback->initAudioTrack();
    XLOGE("result-->value-->%s,%d", av_err2str(result), 57);
    jclass audioTrackClass = ffJniCallback->env->FindClass("android/media/AudioTrack");
    jmethodID writeMethodID = ffJniCallback->env->GetMethodID(audioTrackClass, "write", "([BII)I");

    /**
     * 音频重采样
     * struct SwrContext *swr_alloc_set_opts(struct SwrContext *s,
     *                                int64_t out_ch_layout, enum AVSampleFormat out_sample_fmt, int out_sample_rate,
     *                                 int64_t  in_ch_layout, enum AVSampleFormat  in_sample_fmt, int  in_sample_rate,
     *                                 int log_offset, void *log_ctx);
     */
    int64_t out_ch_layout = AV_CH_LAYOUT_STEREO;
    enum AVSampleFormat out_sample_fmt = AV_SAMPLE_FMT_S16;
    int out_sample_rate = SAMPLE_RATE;
    int64_t in_ch_layout = avCodecContext->channel_layout;
    enum AVSampleFormat in_sample_fmt = avCodecContext->sample_fmt;
    int in_sample_rate = avCodecContext->sample_rate;
    SwrContext *swrContext = swr_alloc_set_opts(NULL, out_ch_layout, out_sample_fmt,
                                                out_sample_rate, in_ch_layout, in_sample_fmt,
                                                in_sample_rate, 0, NULL);
    if (swrContext == NULL) {
        ffJniCallback->onErrorListener(THREAD_MAIN, result, av_err2str(result));
        release();
        return false;
    }

    result = swr_init(swrContext);
    if (result) {
        ffJniCallback->onErrorListener(THREAD_MAIN, result, av_err2str(result));
        release();
        return false;
    }
    XLOGE("result-->value-->%s,%d", av_err2str(result), result);
    /**
    * 用AudioTrack播放，上面创建完AudioTrack实例，以及调用了play()方法之后，
    * 要调用下面的write(byte[] audioData, int offsetInBytes, int sizeInBytes)进行播放
    * int write(@NonNull byte[] audioData, int offsetInBytes, int sizeInBytes)
    */
    //数组的大小 = 一帧的的采样率*通道数*字节数
//    int bufferSize = AV_CH_LAYOUT_STEREO * avCodecContext->frame_size * out_sample_fmt;
    int outChannelNb = av_get_channel_layout_nb_channels(out_ch_layout);
    int bufferSize = av_samples_get_buffer_size(NULL, outChannelNb, avCodecContext->frame_size,
                                                out_sample_fmt, 0);
    uint8_t *data_buffer = (uint8_t *) malloc(bufferSize);
    jbyteArray byteArray = ffJniCallback->env->NewByteArray(bufferSize);
    jbyte *byte = ffJniCallback->env->GetByteArrayElements(byteArray, NULL);

    avPacket = av_packet_alloc();
    avFrame = av_frame_alloc();
    int index = 1;
    while (av_read_frame(avFormatContext, avPacket) >= 0) {
        if (avPacket->stream_index == audioIndex) {
            result = avcodec_send_packet(avCodecContext, avPacket);
            XLOGE("打印============41");
            if (result == 0) {
                result = avcodec_receive_frame(avCodecContext, avFrame);
                if (result == 0) {
                    XLOGE("解码第-->%d帧", index);

                    //调用重采样
                    swr_convert(swrContext, &data_buffer, avFrame->nb_samples,
                                (const uint8_t **) (avFrame->data), avFrame->nb_samples);
                    memcpy(byte, data_buffer, bufferSize);
                    ffJniCallback->env->ReleaseByteArrayElements(byteArray, byte, JNI_COMMIT);
                    ffJniCallback->env->CallIntMethod(audioTrack, writeMethodID, byteArray, 0,
                                                      bufferSize);
                }
            }
            index++;
        }

        av_frame_unref(avFrame);
        av_packet_unref(avPacket);

    }


    ffJniCallback->env->DeleteLocalRef(byteArray);
    if (!data_buffer) {
        free(data_buffer);
        data_buffer = NULL;
    }

    if (audioTrack != NULL) {

        ffJniCallback->env->DeleteLocalRef(audioTrack);
    }

    return true;
}

jobject FFmpeg::initAudioTrack(JNIEnv *env) {

    jclass audioTrackClass = env->FindClass("android/media/AudioTrack");
    jmethodID initMethodID = env->GetMethodID(audioTrackClass, "<init>", "(IIIIII)V");
    jmethodID minBufferSizeID = env->GetStaticMethodID(audioTrackClass, "getMinBufferSize",
                                                       "(III)I");
    int bufferSize = env->CallStaticIntMethod(audioTrackClass, minBufferSizeID, SAMPLE_RATE,
                                              CHANNEL_CONFIG, AUDIO_FORMAT);
    jobject audioTrack = env->NewObject(audioTrackClass, initMethodID,
                                        STREAM_TYPE, SAMPLE_RATE, CHANNEL_CONFIG,
                                        AUDIO_FORMAT, bufferSize, MODE);
    jmethodID playMethodID = env->GetMethodID(audioTrackClass, "play", "()V");
    env->CallVoidMethod(audioTrack, playMethodID);
    return audioTrack;

}

bool FFmpeg::prepareAsync(const char *url) {

}

void FFmpeg::release() {
    if (avFrame) {
        av_frame_free(&avFrame);
        avFrame = NULL;
    }
    if (avPacket) {
        av_packet_free(&avPacket);
        avPacket = NULL;
    }
    if (avCodecContext) {
        avcodec_close(avCodecContext);
        avcodec_free_context(&avCodecContext);
        avCodecContext = 0;
    }
    if (avFormatContext) {
        avformat_close_input(&avFormatContext);
        avformat_free_context(avFormatContext);
        avFormatContext = 0;
    }

    avformat_network_deinit();
    if (ffJniCallback) {
        delete ffJniCallback;
        ffJniCallback = NULL;
    }
}