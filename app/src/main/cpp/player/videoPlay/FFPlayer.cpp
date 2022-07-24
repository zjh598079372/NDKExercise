//
// Created by CL002 on 2022-7-22.
//

#include "FFPlayer.h"
#include "../FFJniCallback.h"


//JNIEnv *localEnv = NULL;

FFJniCallback* ffJniCallback = NULL;
FFmpeg *fFmpeg = NULL;
JavaVM *localvm = NULL;

void FFPlayer::Init(JavaVM *vm, JNIEnv *env) {
    localvm = vm;

}

void prepared(JNIEnv *env, jobject thiz, jstring url){
//    localEnv = env;
    FFPlayer::Get()->prepared(env,thiz,url);
}

void preparedAsync(JNIEnv *env, jobject thiz, jstring url){
    FFPlayer::Get()->preparedAsync(env,thiz,url);
}

void play(JNIEnv *env, jobject thiz){
    FFPlayer::Get()->play(env,thiz);
}

void FFPlayer::preparedAsync(JNIEnv *env,jobject thiz, jstring url) {
    if (!ffJniCallback) {
        ffJniCallback = new FFJniCallback(globalVm, env, thiz);
        fFmpeg = new FFmpeg(ffJniCallback);
        const char* path = globalEnv->GetStringUTFChars(url,NULL);
        fFmpeg->prepareAsync(path);
    }
}

void FFPlayer::prepared(JNIEnv *env, jobject thiz, jstring url) {
    if (!ffJniCallback) {
        ffJniCallback = new FFJniCallback(globalVm, env, thiz);
        fFmpeg = new FFmpeg(ffJniCallback);
        const char* path = env->GetStringUTFChars(url,0);
        fFmpeg->prepare(path);

        delete ffJniCallback;
        delete fFmpeg;
    }

}

void FFPlayer::play(JNIEnv *env, jobject thiz) {

}

