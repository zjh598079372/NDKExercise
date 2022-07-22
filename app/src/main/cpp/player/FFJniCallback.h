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
    JavaVM* vm;
    JNIEnv *env;
    jobject jPlayerObject;
    jclass jPlayerClass;
    jmethodID errorJmethodID;
    jmethodID successJmethodID;
    jmethodID preparedJmethodID;

    FFJniCallback(JavaVM* vm,JNIEnv *env, jobject thiz);

    void onErrorListener(Thread_Mode  threadMode,int errorCode, char* msg );
    void onSuccessListener(Thread_Mode  threadMode);
    void onPerpared(Thread_Mode threadMode);


};


#endif //EXERCISENDK_FFJNICALLBACK_H
