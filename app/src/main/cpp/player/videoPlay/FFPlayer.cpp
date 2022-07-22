//
// Created by CL002 on 2022-7-22.
//

#include "FFPlayer.h"
#include "../FFJniCallback.h"


void FFPlayer::Init(JavaVM *vm, JNIEnv *env) {
    globalVm = reinterpret_cast<JavaVM *>(env->NewGlobalRef(reinterpret_cast<jobject>(vm)));

}

void prepared(JNIEnv *env, jobject thiz, jstring url){
//    if (!fFJniCallback) {
//        fFJniCallback = new FFJniCallback(globalVm, env, thiz);
//    }
//    if (!zjhMedia) {
//        zjhMedia = new ZJHMedia(fFJniCallback);
//        zjhMedia->open(url);
//    }
    FFPlayer::Get()->prepared(env,thiz,url);
}

void preparedAsync(JNIEnv *env, jobject thiz, jstring url){
    FFPlayer::Get()->preparedAsync(env,thiz,url);
}

void play(JNIEnv *env, jobject thiz){
    FFPlayer::Get()->play(env,thiz);
}

void FFPlayer::preparedAsync(JNIEnv *env, jobject thiz, jstring url) {

}

void FFPlayer::prepared(JNIEnv *env, jobject thiz, jstring url) {

}

void FFPlayer::play(JNIEnv *env, jobject thiz) {

}

