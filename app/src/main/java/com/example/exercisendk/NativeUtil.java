package com.example.exercisendk;

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

    public static native Bitmap generateGrayBitmap(Bitmap bimap);
}