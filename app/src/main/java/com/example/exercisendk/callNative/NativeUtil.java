package com.example.exercisendk.callNative;

import android.content.Context;
import android.graphics.Bitmap;

public class NativeUtil {
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

    public static native void generateGrayBitmap(Bitmap bimap);

    public static native Bitmap againstWorld(Bitmap bitmap);

    public static native Bitmap mirrorImage(Bitmap bitmap);

    public static native Bitmap  createBitmap(int[] colors, int width, int height, Bitmap.Config config);

    private Bitmap.Config ARGB_8888;
}
