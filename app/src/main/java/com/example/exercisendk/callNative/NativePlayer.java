package com.example.exercisendk.callNative;

import com.me.support.utils.LogUtil;

public class NativePlayer {
    public static final String TAG = NativePlayer.class.getSimpleName()+"--->";

    private JNICallbackListener mJNICallbackListener;

    public JNICallbackListener getmJNICallbackListener() {
        return mJNICallbackListener;
    }

    public void setmJNICallbackListener(JNICallbackListener jNICallbackListener) {
        this.mJNICallbackListener = jNICallbackListener;
    }

    public interface JNICallbackListener{
        void onError(int type, String message);
        void onSuccess();
    }

    public void onErrorListener(int type,String message){
        LogUtil.e(TAG+message);
        if(mJNICallbackListener != null){
            mJNICallbackListener.onError(type,message);
        }
    }

    public void onSuccessListener(){
        LogUtil.e(TAG+"onSuccessListener");
        if(mJNICallbackListener != null){
            mJNICallbackListener.onSuccess();
        }
    }

    /**
     * 视频播放方法
     */
    public native String nPlay(String url);
}
