package com.me.support.recyclerView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.example.commonlibrary.R;


public class DefaultRefreshCreator extends RefreshViewCreator {
    // 加载数据的ImageView
    private View mRefreshIv;
    private ObjectAnimator rotationAnimator;

    @Override
    public View getRefreshView(ViewGroup parent, Context context) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_view, parent, false);
        mRefreshIv = refreshView.findViewById(R.id.refresh_iv);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {
        float rotate = ((float) currentDragHeight) / refreshViewHeight;
        // 不断下拉的过程中不断的旋转图片
        mRefreshIv.setRotation(rotate * 360);
    }

    @Override
    public void onRefreshing() {
        // 刷新的时候不断旋转
        if(rotationAnimator == null){
            rotationAnimator = ObjectAnimator.ofFloat(mRefreshIv,"Rotation",0,360);
            rotationAnimator.setDuration(350);
            rotationAnimator.setRepeatCount(-1);
            rotationAnimator.setInterpolator(new LinearInterpolator());
        }
        if(!rotationAnimator.isRunning()){
            rotationAnimator.start();
        }

    }

    @Override
    public void onStopRefresh() {
        // 停止加载的时候清除动画
        mRefreshIv.setRotation(0);
//        mRefreshIv.clearAnimation();
        if(rotationAnimator != null){
            rotationAnimator.cancel();
        }
        Log.e("onStopRefresh-->","调用了停止动画");
    }
}
