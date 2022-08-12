//
// Created by CL002 on 2022-8-12.
//

#include <XLog.h>
#include "FFMedia.h"

FFMedia::FFMedia(int index, FFJniCallback *ffJniCallback, AVFormatContext *avFormatContext,
                 Thread_Mode threadMode) {
    this->index = index;
    this->ffJniCallback = ffJniCallback;
    this->avFormatContext = avFormatContext;
    this->threadMode = threadMode;
    this->ffPlayStatus = new FFPlayStatus();

}

FFMedia::~FFMedia() {
    release();

}

void FFMedia::analysisStream() {
    publicAnalysisStream();
    privateAnalysisStream();
}

void FFMedia::play() {

}

void FFMedia::release() {
    if (avCodecContext) {
        avcodec_close(avCodecContext);
        avcodec_free_context(&avCodecContext);
        avCodecContext = NULL;
    }

    if (ffPlayStatus) {
        delete ffPlayStatus;
        ffPlayStatus = NULL;
    }
}

void FFMedia::publicAnalysisStream() {
    int result = 0;
    AVCodec *audioCodec = avcodec_find_decoder(
            avFormatContext->streams[index]->codecpar->codec_id);
    avCodecContext = avcodec_alloc_context3(audioCodec);
    result = avcodec_parameters_to_context(avCodecContext,
                                           avFormatContext->streams[index]->codecpar);
    if (result < 0) {
        XLOGE("FFAudio-->value-->%s,%d", av_err2str(result), 47);
        ffJniCallback->onErrorListener(threadMode, result, av_err2str(result));

        return;
    }
    result = avcodec_open2(avCodecContext, audioCodec, NULL);
    if (result) {
        XLOGE("FFAudio-->value-->%s,%d", av_err2str(result), 54);
        ffJniCallback->onErrorListener(threadMode, result, av_err2str(result));
        return;
    }
    XLOGE("FFAudio-->value-->%s,%d", av_err2str(result), result);

}
