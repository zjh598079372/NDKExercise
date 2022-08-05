//
// Created by CL002 on 2022-7-28.
//

#ifndef EXERCISENDK_FFAUDIO_H
#define EXERCISENDK_FFAUDIO_H
#include "../FFJniCallback.h"
#include <XLog.h>
#include "../ConstDefine.h"
#include "../queue/PacketQueue.h"
#include <pthread.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

extern "C" {
#include "../../include/ffmpeg/libavformat/avformat.h"
#include "../../include/ffmpeg/libswresample/swresample.h"
};


class FFAudio {
public:
    int audioIndex = 0;
    FFJniCallback *ffJniCallback = NULL;
    AVFormatContext *avFormatContext = NULL;
    PacketQueue* packetQueue = NULL;
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

    void initCreateOpenSLES();

    int resampleAudio();

    uint8_t *resampleOutBuffer;
};


#endif //EXERCISENDK_FFAUDIO_H
