//
// Created by CL002 on 2022-6-30.
//

#include <string>
#include "playerMethod.h"
#include "../include/XLog.h"
#include "../player/IPlayerPorxy.h"




jstring play(JNIEnv* env,jobject thiz,jstring filePath){

    const char* url = env->GetStringUTFChars(filePath,0);
    XLOGE("url-->value-->%s",url);
    IPlayerPorxy::Get()->open(url);
    std::string hello = "HelloWorld";
    return env->NewStringUTF(hello.c_str());
}