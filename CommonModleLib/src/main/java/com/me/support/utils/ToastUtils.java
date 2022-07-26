package com.me.support.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.me.support.app.BaseApplication;

public class ToastUtils {
    public static final long TOAST_TIME = 2000;

    private static Toast mToast = null;
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast(Context context, String text, int duration) {
        mHandler.removeCallbacks(runnable);
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
        }
        mHandler.postDelayed(runnable, 3000);
        mToast.show();
    }

    public static void showToast(Context context, int resId, int duration) {
        showToast(context, context.getResources().getString(resId), duration);
    }

    public static void showToast(Context context, String text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getResources().getString(resId), Toast.LENGTH_SHORT);
    }

    public static void showToast(String text) {
        showToast(BaseApplication.getContext(), text, Toast.LENGTH_SHORT);
    }

    public static void showToast( int resId) {
        showToast(BaseApplication.getContext(), BaseApplication.getContext().getResources().getString(resId), Toast.LENGTH_SHORT);
    }

    public static Handler getHandler() {
        return mHandler;
    }
}
