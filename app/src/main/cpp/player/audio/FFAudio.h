//
// Created by CL002 on 2022-7-28.
//

#ifndef EXERCISENDK_FFAUDIO_H
#define EXERCISENDK_FFAUDIO_H

#include "../FFJniCallback.h"
#include <XLog.h>
#include "../ConstDefine.h"
#include "../queue/PacketQueue.h"
#include "../playStatus/FFPlayStatus.h"
#include "../media/FFMedia.h"
#include <pthread.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

extern "C" {
#include "../../include/ffmpeg/libavformat/avformat.h"
#include "../../include/ffmpeg/libswresample/swresample.h"
};


class FFAudio : public FFMedia {
public:

    PacketQueue *packetQueue = NULL;
    SwrContext *swrContext = NULL;
    uint8_t *resampleOutBuffer;
    AVPacket *pPacket = NULL;
    bool isExit = false;
    double synTime = 0;

public:
    FFAudio(int index, FFJniCallback *ffJniCallback, FFPlayStatus *ffPlayStatus);

    ~FFAudio();

    virtual void privateAnalysisStream(AVFormatContext *avFormatContext,
                                       Thread_Mode threadMode);

    virtual void play();

    void release();

    void initCreateOpenSLES();

    int resampleAudio();

    double getSynTime();


};


#endif //EXERCISENDK_FFAUDIO_H
