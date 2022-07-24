//
// Created by zjh on 2022/7/10.
//

#ifndef EXERCISENDK_FFJNICALLBACK_H
#define EXERCISENDK_FFJNICALLBACK_H

#include <jni.h>
enum Thread_Mode{
    THREAD_CHILD,THREAD_MAIN
};

class FFJniCallback {
public:
    JavaVM* vm = 0;
    JNIEnv* env = 0;
    jobject jPlayerObject = 0;
    jclass jPlayerClass = 0;
    jmethodID errorJmethodID = 0;
    jmethodID successJmethodID = 0;
    jmethodID preparedJmethodID = 0;

    FFJniCallback(JavaVM* vm,JNIEnv *env, jobject thiz);
    ~FFJniCallback();

    void onErrorListener(Thread_Mode  threadMode,int errorCode, char* msg );
    void onSuccessListener(Thread_Mode  threadMode);
    void onPerpared(Thread_Mode threadMode);
    jobject initAudioTrack();
};


#endif //EXERCISENDK_FFJNICALLBACK_H
