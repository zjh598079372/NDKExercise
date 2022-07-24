//
// Created by zjh on 2022/7/10.
//

#include <XLog.h>
#include "FFJniCallback.h"
#include "ConstDefine.h"

FFJniCallback::FFJniCallback(JavaVM *vm, JNIEnv *env, jobject thiz) : vm(vm), env(env) {

    XLOGE("ENV-->%p",&env);
    jPlayerObject = env->NewGlobalRef(thiz);
    jPlayerClass = env->GetObjectClass(jPlayerObject);
    errorJmethodID = env->GetMethodID(jPlayerClass, "onErrorListener", "(ILjava/lang/String;)V");
    successJmethodID = env->GetMethodID(jPlayerClass, "onSuccessListener", "()V");
    preparedJmethodID = env->GetMethodID(jPlayerClass, "onPrepared", "()V");
}

void FFJniCallback::onErrorListener(Thread_Mode threadMode, int errorCode, char *msg) {

    if (threadMode == THREAD_MAIN) {
        //创建回调到java方法
        jstring errorStr = env->NewStringUTF(msg);
        env->CallVoidMethod(jPlayerObject, errorJmethodID, errorCode, errorStr);
        env->DeleteLocalRef(errorStr);
    } else if (threadMode == THREAD_CHILD) {
        JNIEnv *p_env = NULL;
        if (vm->AttachCurrentThread(&p_env, NULL) != JNI_OK) {
            XLOGE("Get child thread env error");
            return;
        }
        jstring message = env->NewStringUTF(msg);
        env->CallVoidMethod(jPlayerObject, errorJmethodID, errorCode, message);
        env->DeleteLocalRef(message);
        vm->DetachCurrentThread();
    }

}

void FFJniCallback::onSuccessListener(Thread_Mode threadMode) {
    //创建回调到java方法
    if (threadMode == THREAD_MAIN) {
        env->CallVoidMethod(jPlayerObject, successJmethodID);
    } else if (threadMode == THREAD_CHILD) {
        JNIEnv *p_env = NULL;
        if (!vm->AttachCurrentThread(&p_env, 0)) {
            XLOGE("Get child thread env error");
        }
        p_env->CallVoidMethod(jPlayerObject, successJmethodID);
        vm->DetachCurrentThread();
    }


}

void FFJniCallback::onPerpared(Thread_Mode threadMode) {
    //创建回调到java方法
    if (threadMode == THREAD_MAIN) {
        env->CallVoidMethod(jPlayerObject, preparedJmethodID);
    } else if (threadMode == THREAD_CHILD) {
        JNIEnv *p_env = NULL;
        if (!vm->AttachCurrentThread(&p_env, 0)) {
            XLOGE("Get child thread env error");
        }
        p_env->CallVoidMethod(jPlayerObject, preparedJmethodID);
        vm->DetachCurrentThread();
    }

}

jobject FFJniCallback::initAudioTrack() {

    jclass audioTrackClass = env->FindClass("android/media/AudioTrack");
    jmethodID initMethodID = env->GetMethodID(audioTrackClass, "<init>", "(IIIIII)V");
    jmethodID minBufferSizeID = env->GetStaticMethodID(audioTrackClass, "getMinBufferSize",
                                                       "(III)I");
    int bufferSize = env->CallStaticIntMethod(audioTrackClass, minBufferSizeID, SAMPLE_RATE,
                                              CHANNEL_CONFIG, AUDIO_FORMAT);
    jobject audioTrack = env->NewObject(audioTrackClass, initMethodID,
                                        STREAM_TYPE, SAMPLE_RATE, CHANNEL_CONFIG,
                                        AUDIO_FORMAT, bufferSize, MODE);
    jmethodID playMethodID = env->GetMethodID(audioTrackClass, "play", "()V");
    env->CallVoidMethod(audioTrack, playMethodID);
    return audioTrack;

}

FFJniCallback :: ~FFJniCallback(){
    env->DeleteGlobalRef(jPlayerObject);
    jPlayerObject = NULL;
}