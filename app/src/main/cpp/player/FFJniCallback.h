//
// Created by zjh on 2022/7/10.
//

#ifndef EXERCISENDK_FFJNICALLBACK_H
#define EXERCISENDK_FFJNICALLBACK_H

#include <jni.h>


class FFJniCallback {
public:
    JavaVM* vm;
    JNIEnv *env;
    jobject jPlayerObject;
    jclass jPlayerClass;
    jmethodID errorJmethodID;
    jmethodID successJmethodID;

    FFJniCallback(JavaVM* vm,JNIEnv *env, jobject thiz);

    void onErrorListener(int errorCode, char* mes );
    void onSuccessListener();

};


#endif //EXERCISENDK_FFJNICALLBACK_H
