package com.me.support.recyclerView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonlibrary.R;


public class DefaultLoadCreator extends LoadViewCreator {
    private ImageView mRefreshIv;
    private ObjectAnimator rotationAnimator;
    private TextView mTextView;
//    private View converView;
    private int mLoadViewHeight;

    @Override
    public View getLoadView(ViewGroup parent, Context context) {
        View loadView = LayoutInflater.from(context).inflate(R.layout.layout_load_more_view, parent, false);
        mRefreshIv = loadView.findViewById(R.id.refresh_iv);
        mTextView = loadView.findViewById(R.id.textView);
//        converView = loadView.findViewById(R.id.converView);
        return loadView;
    }

    @Override
    public void onPush(int currentDragHeight, int loadViewHeight, int currentLoadStatus) {
        mLoadViewHeight = loadViewHeight;
        if(mTextView.getVisibility() == View.GONE){
            mTextView.setVisibility(View.VISIBLE);
        }
        if(mRefreshIv.getVisibility() == View.GONE){
            mRefreshIv.setVisibility(View.VISIBLE);
        }
        if(currentDragHeight>loadViewHeight){
            mTextView.setText("手松开加载更多...");
        }else {
            mTextView.setText("上拉加载更多...");
        }
        float rotate = ((float) currentDragHeight) / loadViewHeight;
        // 不断下拉的过程中不断的旋转图片
        mRefreshIv.setRotation(rotate * 360);
//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) converView.getLayoutParams();
//        params.topMargin = currentDragHeight;
//        if (currentDragHeight>loadViewHeight){
//            params.topMargin = loadViewHeight;
//        }
//        converView.setLayoutParams(params);
    }

    @Override
    public void onLoading() {
// 刷新的时候不断旋转
        if(rotationAnimator == null){
            rotationAnimator = ObjectAnimator.ofFloat(mRefreshIv,"Rotation",0,360);
            rotationAnimator.setDuration(350);
            rotationAnimator.setRepeatCount(-1);
            rotationAnimator.setInterpolator(new LinearInterpolator());
        }
        if(!rotationAnimator.isRunning()){
            Log.e("onLoading-->","onLoading===");
            rotationAnimator.start();
        }
    }

    @Override
    public void stopLoad() {
        // 停止加载的时候清除动画
        mRefreshIv.setRotation(0);
//        mRefreshIv.clearAnimation();
        if(rotationAnimator != null){
            rotationAnimator.cancel();
        }
//        mRefreshIv.setVisibility(View.GONE);
//        mTextView.setVisibility(View.GONE);
//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) converView.getLayoutParams();
//        params.topMargin = 0;
//        converView.setLayoutParams(params);
        Log.e("onStopRefresh-->","调用了停止动画");
    }
}
