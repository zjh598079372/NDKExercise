//
// Created by zjh on 2022/3/27.
//

#ifndef EXERCISENDK_BITMAPHELPER_H
#define EXERCISENDK_BITMAPHELPER_H
#include <jni.h>

class BitmapHelper {
public:
    static jobject createBitmap(JNIEnv* env, int width, int height);
};


#endif //EXERCISENDK_BITMAPHELPER_H
