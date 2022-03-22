package com.example.exercisendk;

import android.util.Log;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorUtil {
    private static ThreadPoolExecutor mExecutor;
    private static Future<?> mFuture;

    public static ThreadPoolExecutor getInstance(){
        if(mExecutor == null){
            synchronized (ExecutorUtil.class){
                if(mExecutor == null){
                    mExecutor = new ThreadPoolExecutor(1,1,0, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>());
                }
            }
        }

        return mExecutor;
    }

    public static void submitRunnable(Runnable runnable) {
        mFuture = getInstance().submit(runnable);
    }

    public static void cancelRunnable(){
        if(mFuture != null){
            Log.e("TAG","mFuture != null！");
            mFuture.cancel(true);
        }else {
            Log.e("TAG","mFuture == null！");
        }
    }
}
