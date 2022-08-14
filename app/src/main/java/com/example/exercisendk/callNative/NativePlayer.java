package com.example.exercisendk.callNative;

import android.view.Surface;

import com.me.support.utils.LogUtil;

public class NativePlayer {
    public static final String TAG = NativePlayer.class.getSimpleName() + "--->";

    private JNICallbackListener mJNICallbackListener;

    public JNICallbackListener getmJNICallbackListener() {
        return mJNICallbackListener;
    }

    public void setmJNICallbackListener(JNICallbackListener jNICallbackListener) {
        this.mJNICallbackListener = jNICallbackListener;
    }

    public interface JNICallbackListener {
        void onError(int type, String message);

        void onSuccess();

        void onPrepared();
    }

    public void onErrorListener(int type, String message) {
        LogUtil.e(TAG + message);
        if (mJNICallbackListener != null) {
            mJNICallbackListener.onError(type, message);
        }
    }

    public void onSuccessListener() {
        LogUtil.e(TAG + "onSuccessListener");
        if (mJNICallbackListener != null) {
            mJNICallbackListener.onSuccess();
        }
    }

    public void onPrepared() {
        LogUtil.e(TAG + "onPrepared");
        if (mJNICallbackListener != null) {
            mJNICallbackListener.onPrepared();
        }
    }


    /**************================== 视频播放方法=====================*************************/

    /**
     * 播放前的同步准备
     *
     * @param url
     */
    public native void nPrepared(String url);

    /**
     * 播放前的异步准备
     *
     * @param url
     */
    public native void nPreparedAsync(String url);

    /**
     * 真正的播放
     */
    public native void nPlay();

    /**
     * 设置播放视频的surface
     *
     */
    public native void nSetSurface(Surface surface);
}
