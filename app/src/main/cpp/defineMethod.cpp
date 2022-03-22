//
// Created by CL002 on 2022-3-17.
//

#include "defineMethod.h"
#include <string>
#include <android/bitmap.h>
#include <opencv2/core/mat.hpp>
#include <opencv2/imgproc.hpp>
#include <android/log.h>
#include <opencv2/highgui.hpp>


using namespace cv;

void bitmap2Mat(JNIEnv* env, jobject jbitmap,Mat& dst) {
    AndroidBitmapInfo info;
    void* pixels;
    AndroidBitmap_getInfo(env,jbitmap,&info);
    AndroidBitmap_lockPixels(env,jbitmap,&pixels);

    dst.create(info.height,info.width,CV_8UC4);
    if(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888){
        Mat temp(info.height,info.width,CV_8UC4,pixels);
        temp.copyTo(dst);
    } else if(info.format == ANDROID_BITMAP_FORMAT_RGB_565){
        Mat temp(info.height,info.width,CV_8UC2,pixels);
        cvtColor(temp,dst,COLOR_BGR5652BGRA);
    }

    AndroidBitmap_unlockPixels(env,jbitmap);

}

void mat2bitmap(JNIEnv *env, Mat mat, jobject bitmap) {
    AndroidBitmapInfo info;
    void* addrPtr;
    AndroidBitmap_getInfo(env,bitmap,&info);
    AndroidBitmap_lockPixels(env,bitmap,&addrPtr);
    if(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888){
        Mat tmp(info.height, info.width, CV_8UC4, addrPtr);
        if (mat.type() == CV_8UC1) {
            __android_log_print(ANDROID_LOG_ERROR,"Mat","mat.type() == CV_8UC1");
            cvtColor(mat, tmp, COLOR_GRAY2RGBA);
        } else if (mat.type() == CV_8UC2) {
            __android_log_print(ANDROID_LOG_ERROR,"Mat","mat.type() == CV_8UC2");
            cvtColor(mat, tmp, COLOR_BGR5652BGRA);
        }else if (mat.type() == CV_8UC3) {
            __android_log_print(ANDROID_LOG_ERROR,"Mat","mat.type() == CV_8UC3");
            cvtColor(mat, tmp, COLOR_RGB2RGBA);
        } else if (mat.type() == CV_8UC4) {
            __android_log_print(ANDROID_LOG_ERROR,"Mat","mat.type() == CV_8UC4");
            mat.copyTo(tmp);
        }
    } else if(info.format == ANDROID_BITMAP_FORMAT_RGB_565){
//        __android_log_print(ANDROID_LOG_ERROR,"Mat","mat.type()-->%d",mat.type());
        Mat tmp(info.height, info.width, CV_8UC2, addrPtr);
        if (mat.type() == CV_8UC1) {
            cvtColor(mat, tmp, COLOR_GRAY2BGR565);
        } else if (mat.type() == CV_8UC2) {
            mat.copyTo(tmp);
        }else if (mat.type() == CV_8UC3) {
            cvtColor(mat, tmp, COLOR_RGB2BGR565);
        } else if (mat.type() == CV_8UC4) {
            cvtColor(mat, tmp, COLOR_RGBA2BGR565);
        }
    }

    AndroidBitmap_unlockPixels(env,bitmap);

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
void generateGrayBitmap(JNIEnv* env,jobject thiz,jobject bitmap){

    Mat mat;
    bitmap2Mat(env,bitmap,mat);
    Mat gray;
    cvtColor(mat,gray,COLOR_BGRA2GRAY);
//    GaussianBlur(mat,gray,Size(15,15),0);
//    blur(mat,gray,Size(15,15));
    mat2bitmap(env,gray,bitmap);

};

/**
 * 逆世界
 */
extern "C"
jobject againstWorld(JNIEnv* env, jobject thiz, jobject bitmap){
    Mat mat;
    bitmap2Mat(env,bitmap,mat);
    Mat src;
    mat.copyTo(src);
    int src_height = mat.rows;
    int src_width = mat.cols;
    //二分之一
    int middle_height = src_height >> 1;
    //四分之一
    int quarter_height = middle_height >> 1;
    //处理上半部分
    for(int row = quarter_height; row >=0 ; row--){
        for(int col =0; col < src_width; col++){
            src.at<int>(middle_height - row,col) = mat.at<int>(middle_height + row,col);
        }
    }

    //处理下半部分
    for(int row = quarter_height; row >=0 ; row--){
        for(int col =0; col < src_width; col++){
            src.at<int>(middle_height + row,col) = mat.at<int>(middle_height - row,col);
        }
    }

    mat2bitmap(env,src,bitmap);

    return bitmap;
};

/**
 * 镜像
 */
extern "C"
jobject mirrorImage(JNIEnv* env, jobject thiz, jobject bitmap){
    Mat mat;
    bitmap2Mat(env,bitmap,mat);
    int src_height = mat.rows;
    int src_width = mat.cols;
    __android_log_print(ANDROID_LOG_ERROR,"Mat","mat.cols=%d",mat.cols);
    Mat src;
    src.create(Size(src_height,src_width << 1),mat.type());
    for (int row = 0; row < src_height; ++row) {
        for (int col =0;col <src_width*2;col++){
            int tempCol = col;
            if(col >src_width){
                tempCol = (src_width <<1) -col;
            }
            src.at<int>(row,col) = mat.at<int>(row,tempCol);
        }
    }
    __android_log_print(ANDROID_LOG_ERROR,"Mat","src.cols=%d",src.cols);
    mat2bitmap(env,src,bitmap);

    return bitmap;
};