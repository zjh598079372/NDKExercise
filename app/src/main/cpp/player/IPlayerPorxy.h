//
// Created by CL002 on 2022-6-30.
//

#ifndef EXERCISENDK_IPLAYERPORXY_H
#define EXERCISENDK_IPLAYERPORXY_H


#include "IPlayer.h"
#include "FFJniCallback.h"
#include <string>
#include "../include/XLog.h"
extern "C"{
#include "../include/ffmpeg/libavformat/avformat.h"
}

class IPlayerPorxy : public IPlayer{
public:
    AVFormatContext* avFormatContext = NULL;

    IPlayer *iPlayer = 0;
    FFJniCallback* fFJniCallback = 0;
    JavaVM* globalVm = 0;


    static IPlayerPorxy* Get(){
        static IPlayerPorxy iPlayerPorxy;
        return &iPlayerPorxy;
    }

    void Init(JavaVM* vm ,JNIEnv *env);

    virtual bool open(JNIEnv* env, const jobject thiz,const char *url);



};


#endif //EXERCISENDK_IPLAYERPORXY_H
