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
    AVCodecContext *avCodecContext = NULL;
    FFPlayStatus *ffPlayStatus = NULL;

    bool isExit = false;
    int duration = 0;
    AVRational time_base;


public:

    FFMedia(int index, FFJniCallback *ffJniCallback, FFPlayStatus *ffPlayStatus);

    ~FFMedia();

    void analysisStream(AVFormatContext *avFormatContext,
                        Thread_Mode threadMode);

    virtual void privateAnalysisStream(Thread_Mode threadMode) = 0;

    void publicAnalysisStream(AVFormatContext *avFormatContext,
                              Thread_Mode threadMode);

    virtual void play() = 0;

    void release();

};


#endif //EXERCISENDK_FFMEDIA_H
