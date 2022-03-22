//
// Created by CL002 on 2022-3-17.
//

#include "defineMethod.h"
#include <string>
#include <android/bitmap.h>
#include <opencv2/core/mat.hpp>
#include <opencv2/imgproc.hpp>


using namespace cv;

void bitmap2Mat(JNIEnv* env, jobject jbitmap, void** addrPtr,Mat* src) {
    AndroidBitmapInfo info;
    AndroidBitmap_getInfo(env,jbitmap,&info);
    AndroidBitmap_lockPixels(env,jbitmap,addrPtr);
    Mat mat(info.width,info.height,CV_8UC4);
    src = &mat;

    AndroidBitmap_unlockPixels(env,jbitmap);

}

extern "C"
JNICALL jstring decrypt(JNIEnv *env, jobject thiz, jstring ciphertext) {
    std::string hello = "HelloWorld";
    return env->NewStringUTF(hello.c_str());

}

extern "C"
JNICALL jstring encrypt(JNIEnv *env, jobject thiz, jstring plaintext) {
    std::string hello = "HelloWorld";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
jstring getAppPackgeName(JNIEnv* env,jobject thiz,jobject context){
    std::string hello = "HelloWorld";
    jclass clazz = env->GetObjectClass(context);
    jmethodID methodId = env->GetMethodID(clazz,"getPackageName","()Ljava/lang/String;");
    jobject packageName = env->CallObjectMethod(context,methodId);
    jstring str = static_cast<jstring>(packageName);

    return str;
};

extern "C"
jobject generateGrayBitmap(JNIEnv* env,jobject thiz,jobject bitmap){

    Mat src;
    void* ptr = NULL;
    bitmap2Mat(env,bitmap,&ptr,&src);

    cvtColor(src,src,COLOR_BGRA2GRAY);

    return bitmap;
};