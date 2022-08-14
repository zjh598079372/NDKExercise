//
// Created by CL002 on 2022-3-17.
//

#include <jni.h>
#include <assert.h>
#include <stdint.h>
#include "bitmapMethod.h"
#include "../player/IPlayerPorxy.h"
#include "../player/videoPlay/FFPlayer.h"

#define NATIVE_BITMAP_CLASS "com/example/exercisendk/callNative/NativeBitmap"//指定要注册的类
#define NATIVE_PLAYER_CLASS "com/example/exercisendk/callNative/NativePlayer"

/**
* Table of methods associated with a single class.
*/
static JNINativeMethod bitmapMethods[] = {
        {"encrypt",            "(Ljava/lang/String;)Ljava/lang/String;",                (void *) encrypt},
        {"decrypt",            "(Ljava/lang/String;)Ljava/lang/String;",                (void *) decrypt},
        {"getAppPackgeName",   "(Landroid/content/Context;)Ljava/lang/String;",         (void *) getAppPackgeName},
        {"generateGrayBitmap", "(Landroid/graphics/Bitmap;I)Landroid/graphics/Bitmap;", (void *) generateGrayBitmap},
        {"againstWorld",       "(Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;",  (void *) againstWorld},
        {"mirrorImage",        "(Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;",  (void *) mirrorImage},
        {"rotationImage",      "(Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;",  (void *) rotationImage},
        {"reflectionImage",    "(Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;",  (void *) reflectionImage}

};

static JNINativeMethod playerMethods[] = {
        {"nPlay", "()V", (void *) play},
        {"nPrepared", "(Ljava/lang/String;)V", (void *) prepared},
        {"nPreparedAsync", "(Ljava/lang/String;)V", (void *) preparedAsync},
        {"nSetSurface", "(Landroid/view/Surface;)V", (void *) setSurface}
};

/*
* Register several native methods for one class.
*/
static int registerNativeMethods(JNIEnv *env, const char *className,
                                 JNINativeMethod *bitmapMethods, int numMethods) {
    jclass clazz;
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, bitmapMethods, numMethods) < 0) {
        env->UnregisterNatives(clazz);
        return JNI_FALSE;
    }

    return JNI_TRUE;
}


/*
* Register native methods for all classes we know about.
*/
static int registerNatives(JNIEnv *env) {
    if (!registerNativeMethods(env, NATIVE_BITMAP_CLASS, bitmapMethods,
                               sizeof(bitmapMethods) / sizeof(bitmapMethods[0])))
        return JNI_FALSE;
    if (!registerNativeMethods(env, NATIVE_PLAYER_CLASS, playerMethods,
                               sizeof(playerMethods) / sizeof(playerMethods[0])))
        return JNI_FALSE;
    return JNI_TRUE;
}

/*
* Set some test stuff up.
*
* Returns the JNI version on success, -1 on failure.
*/
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    jint result = -1;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    assert(env != NULL);

    if (!registerNatives(env)) {//注册
        return -1;
    }
    /* success -- return valid version number */
    result = JNI_VERSION_1_4;
//    IPlayerPorxy::Get()->Init(vm,env);
    FFPlayer::Get()->Init(vm,env);
    return result;
}