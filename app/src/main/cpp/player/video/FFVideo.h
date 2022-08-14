//
// Created by zjh on 2022/8/13.
//

#ifndef EXERCISENDK_FFVIDEO_H
#define EXERCISENDK_FFVIDEO_H


#include "../media/FFMedia.h"
#include "../queue/PacketQueue.h"
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <XLog.h>
#include "../audio/FFAudio.h"

extern "C"{
#include "../../include/ffmpeg/libswscale/swscale.h"
#include "../../include/ffmpeg/libavutil/imgutils.h"
#include "../../include/ffmpeg/libavutil/time.h"
};

class FFVideo : public FFMedia {

public:

    PacketQueue *packetQueue = NULL;
    AVPacket *pPacket = NULL;
    SwsContext *swsContext = NULL;
    AVFrame *rgbFrame = NULL;
    FFAudio* ffAudio = NULL;
    uint8_t *frameBuff = NULL;
    jobject surface = NULL;
    int frameSize;
    bool isExit = false;
    double currentTime = 0;
    double delayTime = 0;
    double defaultDelayTime = 0.04;

public:
    FFVideo(int index, FFJniCallback *ffJniCallback, FFPlayStatus *ffPlayStatus,FFAudio* ffAudio);

    ~FFVideo();

    virtual void privateAnalysisStream(AVFormatContext *avFormatContext,
                                       Thread_Mode threadMode);

    virtual void play();

    void release();

    double getFrameSleepTime(AVFrame* pFrame);


    void setSurface(JNIEnv *pEnv,jobject surface);



};


#endif //EXERCISENDK_FFVIDEO_H
