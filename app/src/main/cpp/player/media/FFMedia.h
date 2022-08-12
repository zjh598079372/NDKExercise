//
// Created by CL002 on 2022-8-12.
//

#ifndef EXERCISENDK_FFMEDIA_H
#define EXERCISENDK_FFMEDIA_H


#include "../FFJniCallback.h"
#include "../playStatus/FFPlayStatus.h"

extern "C" {
#include "../../include/ffmpeg/libavformat/avformat.h"
#include "../../include/ffmpeg/libavcodec/avcodec.h"
};

class FFMedia {
public:
    int index = 0;
    FFJniCallback *ffJniCallback = NULL;
    AVFormatContext *avFormatContext = NULL;
    Thread_Mode threadMode;
    FFPlayStatus *ffPlayStatus = NULL;
    AVCodecContext *avCodecContext = NULL;

    bool isExit = false;

public:

    FFMedia(int index, FFJniCallback *ffJniCallback, AVFormatContext *avFormatContext,
            Thread_Mode threadMode);

    ~FFMedia();

    void analysisStream();

    virtual void privateAnalysisStream() = 0;

    void publicAnalysisStream();

    virtual void play() = 0;

    void release();

};


#endif //EXERCISENDK_FFMEDIA_H
