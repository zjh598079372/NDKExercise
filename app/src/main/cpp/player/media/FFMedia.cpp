//
// Created by CL002 on 2022-8-12.
//

#include <XLog.h>
#include "FFMedia.h"

FFMedia::FFMedia(int index, FFJniCallback *ffJniCallback, FFPlayStatus *ffPlayStatus) {
    this->index = index;
    this->ffJniCallback = ffJniCallback;
    this->ffPlayStatus = ffPlayStatus;

}

FFMedia::~FFMedia() {
    release();

}

void FFMedia::analysisStream(AVFormatContext *avFormatContext,
                             Thread_Mode threadMode) {
    publicAnalysisStream(avFormatContext, threadMode);
    privateAnalysisStream(avFormatContext,threadMode);
}

void FFMedia::play() {

}

void FFMedia::release() {
    if (avCodecContext) {
        avcodec_close(avCodecContext);
        avcodec_free_context(&avCodecContext);
        avCodecContext = NULL;
    }

}

void FFMedia::publicAnalysisStream(AVFormatContext *avFormatContext,
                                   Thread_Mode threadMode) {
    int result = 0;
    AVCodec *avCodec = avcodec_find_decoder(
            avFormatContext->streams[index]->codecpar->codec_id);
    avCodecContext = avcodec_alloc_context3(avCodec);
    result = avcodec_parameters_to_context(avCodecContext,
                                           avFormatContext->streams[index]->codecpar);
    if (result < 0) {
        XLOGE("FFAudio-->value-->%s,%d", av_err2str(result), 47);
        ffJniCallback->onErrorListener(threadMode, result, av_err2str(result));

        return;
    }
    result = avcodec_open2(avCodecContext, avCodec, NULL);
    if (result) {
        XLOGE("FFAudio-->value-->%s,%d", av_err2str(result), 54);
        ffJniCallback->onErrorListener(threadMode, result, av_err2str(result));
        return;
    }
    XLOGE("FFAudio-->value-->%s,%d", av_err2str(result), result);
    duration = avFormatContext->duration;
    time_base = avFormatContext->streams[index]->time_base;

}
