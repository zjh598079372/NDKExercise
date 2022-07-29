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

        return ;
    }
    result = avcodec_open2(avCodecContext, audioCodec, NULL);
    if (result) {
        XLOGE("result-->value-->%s,%d", av_err2str(result), 53);
        ffJniCallback->onErrorListener(threadMode, result, av_err2str(result));
        return ;
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
        return ;
    }

    result = swr_init(swrContext);
    if (result) {
        ffJniCallback->onErrorListener(threadMode, result, av_err2str(result));
        return ;
    }
    XLOGE("result-->value-->%s,%d", av_err2str(result), result);

}

void FFAudio::release(){
    if(avCodecContext){
        avcodec_close(avCodecContext);
        avcodec_free_context(&avCodecContext);
        avCodecContext = NULL;
    }

    if(swrContext){
        swr_free(&swrContext);
        free(swrContext);
        swrContext = NULL;
    }

}
