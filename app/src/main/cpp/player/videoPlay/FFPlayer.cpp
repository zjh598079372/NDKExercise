//
// Created by CL002 on 2022-7-22.
//

#include "FFPlayer.h"
#include "../FFJniCallback.h"


//JNIEnv *localEnv = NULL;

JavaVM *localvm = NULL;

void FFPlayer::Init(JavaVM *vm, JNIEnv *env) {
    localvm = vm;

}

void prepared(JNIEnv *env, jobject thiz, jstring url) {
//    localEnv = env;
    FFPlayer::Get()->prepared(env, thiz, url);
}

void preparedAsync(JNIEnv *env, jobject thiz, jstring url) {
    FFPlayer::Get()->preparedAsync(env, thiz, url);
}

void play(JNIEnv *env, jobject thiz) {
    FFPlayer::Get()->play(env, thiz);
}

void FFPlayer::preparedAsync(JNIEnv *env, jobject thiz, jstring url) {
    if (!ffJniCallback) {
        ffJniCallback = new FFJniCallback(localvm, env, thiz);
        const char *path = env->GetStringUTFChars(url, NULL);
        fFmpeg = new FFmpeg(ffJniCallback,path);
        fFmpeg->prepareAsync();
    }
}

void FFPlayer::prepared(JNIEnv *env, jobject thiz, jstring url) {
    if (!ffJniCallback) {
        ffJniCallback = new FFJniCallback(localvm, env, thiz);
        const char *path = env->GetStringUTFChars(url, 0);
        fFmpeg = new FFmpeg(ffJniCallback,path);
        fFmpeg->prepare();

//        delete ffJniCallback;
//        ffJniCallback = NULL;
//        delete fFmpeg;
//        fFmpeg = NULL;
    }

}

void FFPlayer::play(JNIEnv *env, jobject thiz) {

    if(fFmpeg){
        fFmpeg->play();
    }
}

