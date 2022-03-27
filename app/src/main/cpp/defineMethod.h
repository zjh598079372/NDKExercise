//
// Created by CL002 on 2022-3-17.
//

#ifndef EXERCISENDK_DEFINEMETHOD_H
#define EXERCISENDK_DEFINEMETHOD_H
#include <jni.h>


extern "C"
JNIEXPORT jstring JNICALL encrypt(JNIEnv *env, jobject thiz, jstring plaintext);

extern "C"
JNIEXPORT jstring JNICALL decrypt(JNIEnv *env, jobject thiz, jstring ciphertext);

extern "C"
jstring getAppPackgeName(JNIEnv* env,jobject thiz,jobject context);

extern "C"
jobject generateGrayBitmap(JNIEnv* env,jobject thiz,jobject bitmap,jint type);

extern "C"
jobject againstWorld(JNIEnv* env,jobject thiz, jobject bitmap);

extern "C"
jobject mirrorImage(JNIEnv* env,jobject thiz, jobject bitmap);

extern "C"
jobject rotationImage(JNIEnv* env,jobject thiz, jobject bitmap);

extern "C"
jobject reflectionImage(JNIEnv* env,jobject thiz, jobject bitmap);

class defineMethod{

};


#endif //EXERCISENDK_DEFINEMETHOD_H
