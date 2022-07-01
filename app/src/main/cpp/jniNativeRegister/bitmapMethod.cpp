//
// Created by CL002 on 2022-3-17.
//

#include "bitmapMethod.h"
#include <string>
#include <android/bitmap.h>
#include <../opencv2/core/mat.hpp>
#include <../opencv2/imgproc.hpp>
#include <android/log.h>
#include <../opencv2/highgui.hpp>
#include "../bitmap/BitmapHelper.h"
#include <math.h>
#include "XLog.h"



using namespace cv;

void bitmap2Mat(JNIEnv *env, jobject jbitmap, Mat &dst) {
    AndroidBitmapInfo info;
    void *pixels;
    AndroidBitmap_getInfo(env, jbitmap, &info);
    AndroidBitmap_lockPixels(env, jbitmap, &pixels);

    dst.create(info.height, info.width, CV_8UC4);
    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        Mat temp(info.height, info.width, CV_8UC4, pixels);
        temp.copyTo(dst);
    } else if (info.format == ANDROID_BITMAP_FORMAT_RGB_565) {
        Mat temp(info.height, info.width, CV_8UC2, pixels);
        cvtColor(temp, dst, COLOR_BGR5652BGRA);
    }

    AndroidBitmap_unlockPixels(env, jbitmap);

}

void mat2bitmap(JNIEnv *env, Mat mat, jobject &bitmap) {
    AndroidBitmapInfo info;
    void *addrPtr;
    AndroidBitmap_getInfo(env, bitmap, &info);
    AndroidBitmap_lockPixels(env, bitmap, &addrPtr);
    __android_log_print(ANDROID_LOG_ERROR, "Mat", "addrPtr ==%p", addrPtr);
    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        Mat tmp(info.height, info.width, CV_8UC4, addrPtr);
        if (mat.type() == CV_8UC1) {
            __android_log_print(ANDROID_LOG_ERROR, "Mat", "mat.type() == CV_8UC1");
            cvtColor(mat, tmp, COLOR_GRAY2RGBA);
        } else if (mat.type() == CV_8UC2) {
            __android_log_print(ANDROID_LOG_ERROR, "Mat", "mat.type() == CV_8UC2");
            cvtColor(mat, tmp, COLOR_BGR5652BGRA);
        } else if (mat.type() == CV_8UC3) {
            __android_log_print(ANDROID_LOG_ERROR, "Mat", "mat.type() == CV_8UC3");
            cvtColor(mat, tmp, COLOR_RGB2RGBA);
        } else if (mat.type() == CV_8UC4) {
            __android_log_print(ANDROID_LOG_ERROR, "Mat", "mat.type() == CV_8UC4");
            mat.copyTo(tmp);
        }
    } else if (info.format == ANDROID_BITMAP_FORMAT_RGB_565) {
//        __android_log_print(ANDROID_LOG_ERROR,"Mat","mat.type()-->%d",mat.type());
        Mat tmp(info.height, info.width, CV_8UC2, addrPtr);
        if (mat.type() == CV_8UC1) {
            cvtColor(mat, tmp, COLOR_GRAY2BGR565);
        } else if (mat.type() == CV_8UC2) {
            mat.copyTo(tmp);
        } else if (mat.type() == CV_8UC3) {
            cvtColor(mat, tmp, COLOR_RGB2BGR565);
        } else if (mat.type() == CV_8UC4) {
            __android_log_print(ANDROID_LOG_ERROR, "Mat565", "mat.size=%d", mat.size);
            cvtColor(mat, tmp, COLOR_RGBA2BGR565);
        }
    }
    __android_log_print(ANDROID_LOG_ERROR, "Mat", "addrPtr ==%p", addrPtr);
    AndroidBitmap_unlockPixels(env, bitmap);

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
jstring getAppPackgeName(JNIEnv *env, jobject thiz, jobject context) {
    std::string hello = "HelloWorld";
    jclass clazz = env->GetObjectClass(context);
    jmethodID methodId = env->GetMethodID(clazz, "getPackageName", "()Ljava/lang/String;");
    jobject packageName = env->CallObjectMethod(context, methodId);
    jstring str = static_cast<jstring>(packageName);

    return str;
};

extern "C"
jobject generateGrayBitmap(JNIEnv *env, jobject thiz, jobject bitmap,jint type) {

    Mat mat;
    bitmap2Mat(env, bitmap, mat);
    Mat gray;
    if(type == 0){
        cvtColor(mat, gray, COLOR_BGRA2GRAY);
    }else if(type == 1){
        GaussianBlur(mat,gray,Size(9,9),0);
    }else if(type == 2){
//        bilateralFilter(mat,gray,3,10,100);
        medianBlur(mat,gray,15);
    }

//
//    blur(mat,gray,Size(15,15));
    mat2bitmap(env, gray, bitmap);

    return bitmap;
};

/**
 * 逆世界
 */
extern "C"
jobject againstWorld(JNIEnv *env, jobject thiz, jobject bitmap) {
    Mat mat;
    bitmap2Mat(env, bitmap, mat);
    Mat src;
    mat.copyTo(src);
    int src_height = mat.rows;
    int src_width = mat.cols;
    //二分之一
    int middle_height = src_height >> 1;
    //四分之一
    int quarter_height = middle_height >> 1;
    //处理上半部分
    for (int row = quarter_height; row >= 0; row--) {
        for (int col = 0; col < src_width; col++) {
            src.at<int>(middle_height - row, col) = mat.at<int>(middle_height + row, col);
        }
    }

    //处理下半部分
    for (int row = quarter_height; row >= 0; row--) {
        for (int col = 0; col < src_width; col++) {
            src.at<int>(middle_height + row, col) = mat.at<int>(middle_height - row, col);
        }
    }

    mat2bitmap(env, src, bitmap);

    return bitmap;
};

/**
 * 镜像
 */
extern "C"
jobject mirrorImage(JNIEnv *env, jobject thiz, jobject bitmap) {
    AndroidBitmapInfo info;
    jint *pixels;
    AndroidBitmap_getInfo(env, bitmap, &info);
    AndroidBitmap_lockPixels(env, bitmap, reinterpret_cast<void **>(&pixels));
    int src_height = info.height;
    int src_width = info.width;
    int newWidth = src_width << 1;

    uint32_t* new_data_arr = new uint32_t[newWidth*src_height];
    int index = 0;
    for (int row = 0; row < src_height; ++row) {

        for (int col = 0; col < newWidth; col++) {
            int tempCol = col;
            if (col > src_width) {
                tempCol = newWidth - col;
            }
            new_data_arr[index++] = pixels[tempCol];

        }
        pixels = pixels +src_width;
    }
    jobject  new_bitmap = BitmapHelper::createBitmap(env,newWidth,src_height);
    uint32_t* result;
    AndroidBitmap_lockPixels(env,new_bitmap,(void**)&result);
    AndroidBitmap_unlockPixels(env,new_bitmap);
    memcpy(result,new_data_arr,sizeof(uint32_t)*newWidth*src_height);

    if(new_data_arr){
        delete new_data_arr;
    }
    AndroidBitmap_unlockPixels(env,bitmap);
    return new_bitmap;
};

extern "C"
jobject rotationImage(JNIEnv* env,jobject thiz, jobject bitmap){
    AndroidBitmapInfo info;
    AndroidBitmap_getInfo(env,bitmap,&info);
    uint32_t* addrptr;
    AndroidBitmap_lockPixels(env,bitmap,(void**) &addrptr);
    int width = info.width;
    int new_height = width;
    int height = info.height;
    int new_width = height;

    uint32_t* transforArr = new uint32_t [new_width*new_height];
    int index = 0;
    for(int row =0; row <width; row++){
        for(int col =height-1; col >= 0; col--){

             int temp_index = col*width+row;
            transforArr[index++] = addrptr[temp_index];
        }
    }

    jobject  new_bitmap = BitmapHelper::createBitmap(env,new_width,new_height);
    uint32_t* new_ptr;
    AndroidBitmap_lockPixels(env,new_bitmap,(void **)&new_ptr);
    AndroidBitmap_unlockPixels(env,new_bitmap);
    memcpy(new_ptr,transforArr,sizeof(uint32_t)* width*height);

    AndroidBitmap_unlockPixels(env,bitmap);
    if(transforArr){
        delete transforArr;
    }
    return new_bitmap;
}

extern "C"
jobject reflectionImage(JNIEnv* env,jobject thiz,jobject bitmap){
    AndroidBitmapInfo info;
    AndroidBitmap_getInfo(env,bitmap,&info);
    uint32_t* addrptr;
    AndroidBitmap_lockPixels(env,bitmap,(void **)&addrptr);
    int width = info.width;
    int height = info.height;
    int half_height = (height >> 1);
    int new_width = width;
    int new_height = height +half_height;
    jobject  new_bitmap = BitmapHelper::createBitmap(env,new_width,new_height);
    uint32_t* new_addrptr;
    AndroidBitmap_lockPixels(env,new_bitmap,(void**)&new_addrptr);
    AndroidBitmap_unlockPixels(env,new_bitmap);
    uint32_t* new_arr = new uint32_t [new_width*new_height];
    int index = 0;
    int origin_total = width*height -1;
    for(int row =0; row < new_height ; row++){
        for(int col = 0; col <new_width; col++){
            int temp_index = index;
            uint32_t value = 0;
            if(row >= height){
                temp_index = origin_total -(row -height)*width +col;
                value = addrptr[temp_index];
//                uint32_t a = (float)0xff * (1-(row -height)/((float)half_height));
//                __android_log_print(ANDROID_LOG_ERROR, "Mat", "a ==%d", a);
//                uint32_t r = (value >> 16 ) & 0xff;
//                uint32_t g = (value >> 8 ) & 0xff;
//                uint32_t b = value & 0xff;
//
//                value = (a << 24) | (r << 16) | (g<< 8) | b;
            }else{
                value = addrptr[temp_index];
            }

            new_addrptr[index ++] = value;
        }
    }


    memcpy(new_arr,new_addrptr,sizeof(uint32_t)*new_width*new_height);
    if(new_arr){
        delete new_arr;
    }

    AndroidBitmap_unlockPixels(env,bitmap);
    return new_bitmap;

}


