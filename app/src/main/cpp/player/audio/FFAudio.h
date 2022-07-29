//
// Created by CL002 on 2022-7-28.
//

#ifndef EXERCISENDK_FFAUDIO_H
#define EXERCISENDK_FFAUDIO_H
#include "../FFJniCallback.h"
#include <XLog.h>
#include "../ConstDefine.h"

extern "C" {
#include "../../include/ffmpeg/libavformat/avformat.h"
#include "../../include/ffmpeg/libswresample/swresample.h"
};


class FFAudio {
private:
    int audioIndex = 0;
    FFJniCallback *ffJniCallback = NULL;
    AVFormatContext *avFormatContext = NULL;
    Thread_Mode threadMode;

    AVCodecContext *avCodecContext = NULL;
    SwrContext *swrContext = NULL;
public:
    FFAudio(int index, FFJniCallback *ffJniCallback, AVFormatContext *avFormatContext,
            Thread_Mode threadMode);

    ~FFAudio();

    void analysisStream();
    void release();

};


#endif //EXERCISENDK_FFAUDIO_H
