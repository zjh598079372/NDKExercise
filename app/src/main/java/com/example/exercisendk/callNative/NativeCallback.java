package com.example.exercisendk.callNative;

import com.me.support.utils.LogUtil;

public class NativeCallback {

    public void JNICallback(int type,String message){
        LogUtil.e("NativeCallback-->"+message);
    }
}
