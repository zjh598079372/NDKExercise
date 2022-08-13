//
// Created by zjh on 2022/7/23.
//

#ifndef EXERCISENDK_FFMPEG_H
#define EXERCISENDK_FFMPEG_H


#include "FFJniCallback.h"
#include <jni.h>
#include "../include/XLog.h"
#include "ConstDefine.h"
#include "audio/FFAudio.h"

extern "C"{
#include "../include/ffmpeg/libavformat/avformat.h"
#include "../include/ffmpeg/libavcodec/avcodec.h"
#include "../include/ffmpeg/libavutil/frame.h"
#include "../include/ffmpeg/libavutil/file.h"
#include "../include/ffmpeg/libavutil/mem.h"
#include "../include/ffmpeg/libswresample/swresample.h"
#include "../include/ffmpeg/libavutil/opt.h"
#include "../include/ffmpeg/libavutil/frame.h"
};


class FFmpeg {
public:
    FFmpeg* pFFmpge = 0;
    FFJniCallback* ffJniCallback = 0;
    FFAudio *ffAudio = 0;
    FFPlayStatus *ffPlayStatus = NULL;
    AVFormatContext *avFormatContext = 0;
    AVCodecContext *avCodecContext = 0;
    char* url = 0;
public:
    FFmpeg(FFJniCallback* ffJniCallback,const char* url);
    ~FFmpeg();

    bool prepare();
    bool prepareAsync();
    bool prepare(Thread_Mode threadMode);
    void play();
    void exitPlay();
    void release();


    jobject audioTrack = 0;
    AVFrame *avFrame = 0;
    AVPacket *avPacket = 0;

    jobject initAudioTrack(JNIEnv *env);


};


#endif //EXERCISENDK_FFMPEG_H
