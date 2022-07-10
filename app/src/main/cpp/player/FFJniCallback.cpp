//
// Created by zjh on 2022/7/10.
//

#include "FFJniCallback.h"

FFJniCallback::FFJniCallback(JavaVM* vm,JNIEnv *env,jobject thiz):vm(vm),env(env),jPlayerObject(thiz){

    jPlayerClass = env->GetObjectClass(jPlayerObject);
    errorJmethodID = env->GetMethodID(jPlayerClass,"onErrorListener","(ILjava/lang/String;)V");
    successJmethodID = env->GetMethodID(jPlayerClass,"onSuccessListener","()V");
}

void FFJniCallback::onErrorListener(int errorCode, char *mes) {

    //创建回调到java方法
    jstring errorStr = env->NewStringUTF(mes);
    env->CallVoidMethod(jPlayerObject,errorJmethodID,errorCode,errorStr);
    env->DeleteLocalRef(errorStr);
}

void FFJniCallback::onSuccessListener() {
    //创建回调到java方法
    env->CallVoidMethod(jPlayerObject,successJmethodID);

}