//
// Created by zjh on 2022/3/27.
//

#include "BitmapHelper.h"


jobject BitmapHelper::createBitmap(JNIEnv* env,int newWidth, int newHeight) {
    //    //创建一个新的bitmap
    const char *class_name = "android/graphics/Bitmap";
    const char *sign_method = "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;";
    const char *config_name = "android/graphics/Bitmap$Config";
    jclass config_class = env->FindClass(config_name);
    jfieldID ARGB_8888 = env->GetStaticFieldID(config_class, "ARGB_8888",
                                               "Landroid/graphics/Bitmap$Config;");
    jobject ARGB_8888_object = env->GetStaticObjectField(config_class, ARGB_8888);
    jclass clazz = env->FindClass(class_name);
    jmethodID methodID = env->GetStaticMethodID(clazz, "createBitmap", sign_method);

    jobject new_bitmap_object = env->CallStaticObjectMethod(clazz, methodID,newWidth,
                                                        newHeight, ARGB_8888_object);
    return new_bitmap_object;
}