//
// Created by CL002 on 2022-7-20.
//

#ifndef EXERCISENDK_ZJHMEDIA_H
#define EXERCISENDK_ZJHMEDIA_H


#include "../FFJniCallback.h"
#include <jni.h>
extern "C"{
#include "../../include/ffmpeg/libavformat/avformat.h"
}

class ZJHMedia {

public:
    FFJniCallback *ffJniCallback = 0;

    ZJHMedia(FFJniCallback *pCallback);
    ~ZJHMedia();

    bool open(const char* url);
    void release_resorce();

    AVFormatContext *avFormatContext;
    AVCodecContext *avCodecContext;
    AVPacket *avPacket;
    AVFrame *avFrame;
};


#endif //EXERCISENDK_ZJHMEDIA_H
