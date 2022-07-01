package com.example.exercisendk.callNative;

import android.content.Context;
import android.graphics.Bitmap;

public class NativeBitmap {
    /**
     * encrypt the plaintext
     *
     * @param plaintext
     * @return
     */
    public static native String encrypt(String plaintext);

    /**
     * decrypt the ciphertext
     *
     * @param ciphertext
     * @return
     */
    public static native String decrypt(String ciphertext);


    public static native String getAppPackgeName(Context context);

    public static native Bitmap generateGrayBitmap(Bitmap bimap,int type);

    public static native Bitmap againstWorld(Bitmap bitmap);

    public static native Bitmap mirrorImage(Bitmap bitmap);

    public static native Bitmap rotationImage(Bitmap bitmap);

    public static native Bitmap reflectionImage(Bitmap bitmap);

    public static native Bitmap  createBitmap(int[] colors, int width, int height, Bitmap.Config config);

    public static final int TYPE_GRAY = 0; //灰度
    public static final int TYPE_GUASI = 1; //高斯
    public static final int TYPE_BILA = 2;  //双边滤波


}
