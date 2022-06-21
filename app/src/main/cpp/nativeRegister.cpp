//
// Created by CL002 on 2022-3-17.
//

#include <jni.h>
#include <assert.h>
#include <stdint.h>
#include "defineMethod.h"


#define JNIREG_CLASS "com/example/exercisendk/callNative/NativeUtil"//指定要注册的类

/**
* Table of methods associated with a single class.
*/
static JNINativeMethod gMethods[] = {
        {"encrypt", "(Ljava/lang/String;)Ljava/lang/String;", (void *) encrypt},
        {"decrypt", "(Ljava/lang/String;)Ljava/lang/String;", (void *) decrypt},
        {"getAppPackgeName", "(Landroid/content/Context;)Ljava/lang/String;", (void *) getAppPackgeName},
        {"generateGrayBitmap", "(Landroid/graphics/Bitmap;)V", (void *) generateGrayBitmap},
        {"againstWorld", "(Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;", (void *) againstWorld},
        {"mirrorImage", "(Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;", (void *) mirrorImage}
};

/*
* Register several native methods for one class.
*/
static int registerNativeMethods(JNIEnv* env, const char* className,
                                 JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}


/*
* Register native methods for all classes we know about.
*/
static int registerNatives(JNIEnv* env)
{
    if (!registerNativeMethods(env, JNIREG_CLASS, gMethods,
                               sizeof(gMethods) / sizeof(gMethods[0])))
        return JNI_FALSE;

    return JNI_TRUE;
}

/*
* Set some test stuff up.
*
* Returns the JNI version on success, -1 on failure.
*/
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env = NULL;
    jint result = -1;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    assert(env != NULL);

    if (!registerNatives(env)) {//注册
        return -1;
    }
    /* success -- return valid version number */
    result = JNI_VERSION_1_4;

    return result;
}