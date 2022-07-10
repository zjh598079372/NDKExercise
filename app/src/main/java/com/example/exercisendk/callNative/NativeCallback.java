package com.example.exercisendk.callNative;

import com.me.support.utils.LogUtil;

public class NativeCallback {
    private JNICallbackListener mJNICallbackListener;

    public void JNICallback(int type,String message){
        LogUtil.e("NativeCallback-->"+message);
        if(mJNICallbackListener != null){
            mJNICallbackListener.playError(type,message);
        }
    }


    public JNICallbackListener getmJNICallbackListener() {
        return mJNICallbackListener;
    }

    public void setmJNICallbackListener(JNICallbackListener jNICallbackListener) {
        this.mJNICallbackListener = jNICallbackListener;
    }

    private interface JNICallbackListener{
        void playError(int type, String message);
    }
}
