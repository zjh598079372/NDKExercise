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

public:
    FFAudio(int index, FFJniCallback *ffJniCallback, AVFormatContext *avFormatContext,
            Thread_Mode threadMode);

    ~FFAudio();

    virtual void privateAnalysisStream();

    virtual void play();

    void audioAnalysisStream();


    void release();

    void initCreateOpenSLES();

    int resampleAudio();


};


#endif //EXERCISENDK_FFAUDIO_H
