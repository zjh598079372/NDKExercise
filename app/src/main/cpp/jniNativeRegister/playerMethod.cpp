//
// Created by CL002 on 2022-6-30.
//

#include <string>
#include "playerMethod.h"
#include "../include/XLog.h"
extern "C"{
#include "../include/ffmpeg/libavformat/avformat.h"
}


/**
 * ffmpeg
 */
AVFormatContext* avFormatContext = NULL;



jstring play(JNIEnv* env,jobject thiz,jstring filePath){
    av_register_all();
    avformat_network_init();
    const char* url = env->GetStringUTFChars(filePath, 0);
    int result = avformat_open_input(&avFormatContext,url,NULL,NULL);
    XLOGE("result-->value-->%d",result);
    XLOGE("result-->value-->%s",strerror(result));
    strerror(result);
    std::string hello = "HelloWorld";
    return env->NewStringUTF(hello.c_str());
}