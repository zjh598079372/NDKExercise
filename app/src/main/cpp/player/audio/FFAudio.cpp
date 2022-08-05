//
// Created by CL002 on 2022-7-28.
//


#include "FFAudio.h"

FFAudio::FFAudio(int index, FFJniCallback *ffJniCallback,
                 AVFormatContext *avFormatContext, Thread_Mode threadMode) {
    this->audioIndex = index;
    this->ffJniCallback = ffJniCallback;
    this->avFormatContext = avFormatContext;
    this->threadMode = threadMode;
    this->packetQueue = new PacketQueue();

}


FFAudio::~FFAudio() {
    release();
}

void FFAudio::analysisStream() {
    int result = 0;
    AVCodec *audioCodec = avcodec_find_decoder(
            avFormatContext->streams[audioIndex]->codecpar->codec_id);
    avCodecContext = avcodec_alloc_context3(audioCodec);
    result = avcodec_parameters_to_context(avCodecContext,
                                           avFormatContext->streams[audioIndex]->codecpar);
    if (result < 0) {
        XLOGE("result-->value-->%s,%d", av_err2str(result), 47);
        ffJniCallback->onErrorListener(threadMode, result, av_err2str(result));

        return;
    }
    result = avcodec_open2(avCodecContext, audioCodec, NULL);
    if (result) {
        XLOGE("result-->value-->%s,%d", av_err2str(result), 53);
        ffJniCallback->onErrorListener(threadMode, result, av_err2str(result));
        return;
    }
    XLOGE("result-->value-->%s,%d", av_err2str(result), result);

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
    swrContext = swr_alloc_set_opts(NULL, out_ch_layout, out_sample_fmt,
                                    out_sample_rate, in_ch_layout, in_sample_fmt,
                                    in_sample_rate, 0, NULL);
    if (swrContext == NULL) {
        ffJniCallback->onErrorListener(threadMode, result, av_err2str(result));
        return;
    }

    result = swr_init(swrContext);
    if (result) {
        ffJniCallback->onErrorListener(threadMode, result, av_err2str(result));
        return;
    }
    resampleOutBuffer = (uint8_t *) malloc(avCodecContext->frame_size * 2 * 2);
    XLOGE("result-->value-->%s,%d", av_err2str(result), result);

}

void *decodeAudioThread(void *context) {
    FFAudio *ffAudio = (FFAudio *) context;

    while (!ffAudio->isExit) {
        AVPacket *avPacket = av_packet_alloc();
        int result = av_read_frame(ffAudio->avFormatContext, avPacket);
        if (result >= 0) {
            //成功
            if (avPacket->stream_index == ffAudio->audioIndex) {
                ffAudio->packetQueue->push(avPacket);
            } else {
                //1、下面这个方法做了三件事情
                // 1.1、解引用数据 data
                //1.2、销毁结构体内容
                //1.3、把avPacket设置=null
                av_packet_free(&avPacket);
            }
        } else {
            //失败
            av_packet_free(&avPacket);
        }
    }
    return 0;
}

void *playThread(void *context) {
    FFAudio *ffAudio = (FFAudio *) context;
    ffAudio->initCreateOpenSLES();
    return 0;
}

void FFAudio::play() {
//1、一个线程去解码packet
    pthread_t decodeAudioTid;
    pthread_create(&decodeAudioTid, NULL, decodeAudioThread, this);
    pthread_detach(decodeAudioTid);

//2、一个线程去播放
    pthread_t playTid;
    pthread_create(&playTid, NULL, playThread, this);
    pthread_detach(playTid);


}

void FFAudio::setStop(bool isStop) {
    isExit = isStop;
}

void FFAudio::release() {
    if (avCodecContext) {
        avcodec_close(avCodecContext);
        avcodec_free_context(&avCodecContext);
        avCodecContext = NULL;
    }
    if (resampleOutBuffer) {
        free(resampleOutBuffer);
        resampleOutBuffer = NULL;
    }

    if (swrContext) {
        swr_free(&swrContext);
        free(swrContext);
        swrContext = NULL;
    }

    if (packetQueue) {
        delete packetQueue;
    }

}

void playerCallback(SLAndroidSimpleBufferQueueItf caller, void *pContext) {
    FFAudio *ffAudio = (FFAudio *) pContext;
    int dataSize = ffAudio->resampleAudio();
    // 这里为什么报错，留在后面再去解决
    (*caller)->Enqueue(caller, ffAudio->resampleOutBuffer, dataSize);
}

void FFAudio::initCreateOpenSLES() {

    /**
     * SL_API SLresult SLAPIENTRY slCreateEngine(
	SLObjectItf             *pEngine,
	SLuint32                numOptions,
	const SLEngineOption    *pEngineOptions,
	SLuint32                numInterfaces,
	const SLInterfaceID     *pInterfaceIds,
	const SLboolean         * pInterfaceRequired
);
     */
    //1、创建引擎接口对象
    SLObjectItf pEngine = NULL;
    SLEngineItf slEngineItf ;
    slCreateEngine(&pEngine, 0, NULL, 0, NULL, NULL);
    (*pEngine)->Realize(pEngine, SL_BOOLEAN_FALSE);
    (*pEngine)->GetInterface(pEngine, SL_IID_ENGINE, &slEngineItf);

    //2、设置混音器
    /**
     * SLresult (*CreateOutputMix) (
		SLEngineItf self,
		SLObjectItf * pMix,
		SLuint32 numInterfaces,
		const SLInterfaceID * pInterfaceIds,
		const SLboolean * pInterfaceRequired
	);
     */
    SLObjectItf pMix = NULL;
    SLInterfaceID slInterfaceId[1] = {SL_IID_ENVIRONMENTALREVERB};
    SLboolean sLboolean[1] = {SL_BOOLEAN_FALSE};
    SLEnvironmentalReverbItf slEnvironmentalReverbItf = NULL;
    (*slEngineItf)->CreateOutputMix(slEngineItf, &pMix, 1, slInterfaceId, sLboolean);
    (*pMix)->Realize(pMix,SL_BOOLEAN_FALSE);
    (*pMix)->GetInterface(pMix,SL_IID_ENVIRONMENTALREVERB,&slEnvironmentalReverbItf);
    SLEnvironmentalReverbSettings slEnvironmentalReverbSettings = SL_I3DL2_ENVIRONMENT_PRESET_STONECORRIDOR;
    (*slEnvironmentalReverbItf)->SetEnvironmentalReverbProperties(slEnvironmentalReverbItf,&slEnvironmentalReverbSettings);

    //3、创建播放器

    /***
     * SLresult (*CreateAudioPlayer) (
		SLEngineItf self,
		SLObjectItf * pPlayer,
		SLDataSource *pAudioSrc,
		SLDataSink *pAudioSnk,
		SLuint32 numInterfaces,
		const SLInterfaceID * pInterfaceIds,
		const SLboolean * pInterfaceRequired
	);
     */
    SLObjectItf pPlayer = NULL;
    SLPlayItf pPlayItf = NULL;
    SLDataLocator_AndroidSimpleBufferQueue simpleBufferQueue = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE,2};
    SLDataFormat_PCM pFormat = {SL_DATAFORMAT_PCM,2,SL_SAMPLINGRATE_44_1,SL_PCMSAMPLEFORMAT_FIXED_16,
                                SL_PCMSAMPLEFORMAT_FIXED_16,SL_SPEAKER_FRONT_LEFT|SL_SPEAKER_FRONT_RIGHT,
                                SL_BYTEORDER_LITTLEENDIAN};
    SLDataSource pAudioSrc = {&simpleBufferQueue,&pFormat};
    SLDataLocator_OutputMix  outputMix = {SL_DATALOCATOR_OUTPUTMIX,pMix};
    SLDataSink pAudioSnk = {&outputMix,NULL};
    SLInterfaceID interfaceIds[3] = {SL_IID_BUFFERQUEUE, SL_IID_VOLUME, SL_IID_PLAYBACKRATE};
    SLboolean interfaceRequired[3] = {SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE};
    (*slEngineItf)->CreateAudioPlayer(slEngineItf,&pPlayer,&pAudioSrc,&pAudioSnk,3,
                                      interfaceIds,interfaceRequired);
    (*pPlayer)->Realize(pPlayer,SL_BOOLEAN_FALSE);
    (*pPlayer)->GetInterface(pPlayer,SL_IID_PLAY,&pPlayItf);
    // 3.4 设置缓存队列和回调函数
    SLAndroidSimpleBufferQueueItf playerBufferQueue;
    (*pPlayer)->GetInterface(pPlayer, SL_IID_BUFFERQUEUE, &playerBufferQueue);
    // 每次回调 this 会被带给 playerCallback 里面的 context
    (*playerBufferQueue)->RegisterCallback(playerBufferQueue, playerCallback, this);
    // 3.5 设置播放状态
    (*pPlayItf)->SetPlayState(pPlayItf, SL_PLAYSTATE_PLAYING);
    // 3.6 调用回调函数
    playerCallback(playerBufferQueue, this);


}

int FFAudio::resampleAudio() {
    int dataSize = 0;
    AVPacket *pPacket = NULL;
    AVFrame *pFrame = av_frame_alloc();

    while (1) {
        pPacket = packetQueue->pop();
        // Packet 包，压缩的数据，解码成 pcm 数据
        int codecSendPacketRes = avcodec_send_packet(avCodecContext, pPacket);
        if (codecSendPacketRes == 0) {
            int codecReceiveFrameRes = avcodec_receive_frame(avCodecContext, pFrame);
            if (codecReceiveFrameRes == 0) {
                // AVPacket -> AVFrame
                // 调用重采样的方法，返回值是返回重采样的个数，也就是 pFrame->nb_samples
                dataSize = swr_convert(swrContext, &resampleOutBuffer, pFrame->nb_samples,
                                       (const uint8_t **) pFrame->data, pFrame->nb_samples);
                dataSize = dataSize * 2 * 2;
                // write 写到缓冲区 pFrame.data -> javabyte
                // size 是多大，装 pcm 的数据
                // 1s 44100 点  2通道 ，2字节    44100*2*2
                // 1帧不是一秒，pFrame->nb_samples点
                break;
            }
        }
        // 解引用
        av_packet_unref(pPacket);
        av_frame_unref(pFrame);
    }
    // 1. 解引用数据 data ， 2. 销毁 pPacket 结构体内存  3. pPacket = NULL
    av_packet_free(&pPacket);
    av_frame_free(&pFrame);
    return dataSize;
}


