//
// Created by CL002 on 2022-7-22.
//

#ifndef EXERCISENDK_FFPLAYER_H
#define EXERCISENDK_FFPLAYER_H

#include <jni.h>
#include "../FFJniCallback.h"
#include "../FFmpeg.h"
#define XLOGE(...) __android_log_print(ANDROID_LOG_ERROR,"XxPlay",__VA_ARGS__)

JNIEXPORT void JNICALL play(JNIEnv *env, jobject thiz);
JNIEXPORT void JNICALL prepared(JNIEnv *env, jobject thiz, jstring url);
JNIEXPORT void JNICALL preparedAsync(JNIEnv *env, jobject thiz, jstring url);
JNIEXPORT void JNICALL setSurface(JNIEnv *env, jobject thiz, jobject surface);
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


    void setSurface(JNIEnv *pEnv, jobject thiz, jobject surface);
};


#endif //EXERCISENDK_FFPLAYER_H
