//
// Created by CL002 on 2022-6-30.
//

#include <jni.h>
#include "IPlayerPorxy.h"


void IPlayerPorxy::Init(JavaVM* vm,JNIEnv *env) {
    //创建player
    if(!iPlayer){
        iPlayer = new IPlayer();
    }
//    globalVm = reinterpret_cast<JavaVM *>(env->NewGlobalRef(reinterpret_cast<jobject>(vm)));


}

bool IPlayerPorxy::open(JNIEnv* env, const jobject thiz,const char *url) {
    if(!fFJniCallback){
        fFJniCallback = new FFJniCallback(globalVm,env,thiz);
    }
    av_register_all();
    avformat_network_init();
    int result;
    result = avformat_open_input(&avFormatContext,url,NULL,NULL);
    result = avformat_find_stream_info(avFormatContext,NULL);
    XLOGE("result-->value-->%d",result);
    XLOGE("result-->value-->%s",strerror(result));
    if(result){
        fFJniCallback->onErrorListener(result,strerror(result));
    } else{
        fFJniCallback->onSuccessListener();
    }
//    strerror(result);
//    std::string hello = "HelloWorld";

    avformat_close_input(&avFormatContext);
    avformat_network_deinit();
    return result;
}
