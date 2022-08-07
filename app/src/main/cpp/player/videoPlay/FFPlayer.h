//
// Created by CL002 on 2022-7-22.
//

#ifndef EXERCISENDK_FFPLAYER_H
#define EXERCISENDK_FFPLAYER_H

#include <jni.h>
#include "../FFJniCallback.h"
#include "../FFmpeg.h"

JNIEXPORT void JNICALL play(JNIEnv *env, jobject thiz);
JNIEXPORT void JNICALL prepared(JNIEnv *env, jobject thiz, jstring url);
JNIEXPORT void JNICALL preparedAsync(JNIEnv *env, jobject thiz, jstring url);
class FFPlayer {
public:
    JavaVM* globalVm = 0;
    JNIEnv* globalEnv = 0;
    FFJniCallback* ffJniCallback = 0;
    FFmpeg* fFmpeg = 0;
public:
    static FFPlayer* Get(){
        static FFPlayer ffPlayer;
        return &ffPlayer;
    }

    ~FFPlayer();
    void release();

    void Init(JavaVM *vm, JNIEnv *env);
    void play(JNIEnv *env, jobject thiz);
    void prepared(JNIEnv *env,jobject thiz, jstring url);
    void preparedAsync(JNIEnv *env,jobject thiz, jstring url);


};


#endif //EXERCISENDK_FFPLAYER_H
