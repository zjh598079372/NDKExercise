//
// Created by CL002 on 2022-7-28.
//

#ifndef EXERCISENDK_FFAUDIO_H
#define EXERCISENDK_FFAUDIO_H
#include "../FFJniCallback.h"
#include <XLog.h>
#include "../ConstDefine.h"
#include <pthread.h>

extern "C" {
#include "../../include/ffmpeg/libavformat/avformat.h"
#include "../../include/ffmpeg/libswresample/swresample.h"
};


class FFAudio {
public:
    int audioIndex = 0;
    FFJniCallback *ffJniCallback = NULL;
    AVFormatContext *avFormatContext = NULL;
    Thread_Mode threadMode;

    AVCodecContext *avCodecContext = NULL;
    SwrContext *swrContext = NULL;
    bool isExit = false;

public:
    FFAudio(int index, FFJniCallback *ffJniCallback, AVFormatContext *avFormatContext,
            Thread_Mode threadMode);

    ~FFAudio();

    void analysisStream();
    void play();
    void release();

    void setStop(bool isStop);

};


#endif //EXERCISENDK_FFAUDIO_H
