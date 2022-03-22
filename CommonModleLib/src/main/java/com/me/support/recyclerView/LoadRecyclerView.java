package com.me.support.recyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

public class LoadRecyclerView extends RefreshRecyclerView {
    private static final int LOAD_STATUS_NORMAL = 0x0011;   //默认状态
    private static final int LOAD_STATUS_PUSH_DOWN_LOAD = 0x0022;   //手指按下正在上拉的状态
    private static final int LOAD_STATUS_LOADING = 0x0033;      //正在加载更多的状态
    private static final int LOAD_STATUS_LOOSER_PUSH_DOWN_LOAD = 0x0044;    //手指松开的状态
    private LoadViewCreator mLoadViewCreator;   //加载更多的辅助类
    private View mLoadView;     //加载更多的View
    private int mFingerDownY;   //手指按下的Y值
    private int mCurrentLoadStatus;     //当前的加载状态
    private boolean mCurrentDrag = false;   //当前是否在滑动
    private float mDragIndex = 0.35f;       //阻尼系数
    private int mLoadViewHeight;        //加载更多View的高度
    private ValueAnimator valueAnimator;

    public LoadRecyclerView(@NonNull Context context) {
        super(context);
    }

    public LoadRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addLoadViewCreator(LoadViewCreator loadViewCreator) {
        mLoadViewCreator = loadViewCreator;
        addLoadView();
    }

    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addLoadView();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFingerDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                restoreLoadView();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 重置当前加载更多view的状态
     */
    private void restoreLoadView() {
        Log.e("mCurrentLoadStatus","mCurrentLoadStatus="+mCurrentLoadStatus);
        if(mCurrentLoadStatus == LOAD_STATUS_LOOSER_PUSH_DOWN_LOAD){
            mCurrentLoadStatus = LOAD_STATUS_LOADING;
            if (mLoadViewCreator != null){
                mLoadViewCreator.onLoading();
            }
            if(mMoreLoadListener != null){
                mMoreLoadListener.onMoreLoad();
            }
        }else {
            closeMoreLoadAnimator();
        }

    }

    /**
     * 设置上拉加载更多view的MarginBottom动画
     */
    public void closeMoreLoadAnimator(){
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mLoadView.getLayoutParams();
        final int currentMarginBottom = marginLayoutParams.bottomMargin;
        Log.e("currentMarginBottom","currentMarginBottom="+currentMarginBottom);
        int finalMarginBottom = 0;
        valueAnimator = ValueAnimator.ofFloat(currentMarginBottom,finalMarginBottom);
        valueAnimator.setDuration(currentMarginBottom*2);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentMargin = (float) animation.getAnimatedValue();
                Log.e("currentMargin","currentMargin==="+currentMargin);
                setLoadViewBottomMargin((int) currentMargin);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onStopLoad();
            }
        });

        if(!valueAnimator.isRunning()){
            valueAnimator.start();
        }
        mCurrentDrag = false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_MOVE:
                if(canScrollDown() || mCurrentLoadStatus == LOAD_STATUS_LOADING){
                    return super.onTouchEvent(e);
                }

                //解决加载更多时，自动滑动的问题
                if(mCurrentDrag){
                    scrollToPosition(getAdapter().getItemCount()-1);
                }

                if(mLoadView !=null ){
                    mLoadViewHeight = mLoadView.getMeasuredHeight();
                    Log.e("onLayout-->","mLoadViewHeight="+mLoadViewHeight);
                }

                //获取手指滑动拖拽的距离
                int distanY = (int) ((mFingerDownY - e.getRawY()) * mDragIndex);
                if(distanY>0){
                    Log.e("onLayout-->","distanY="+distanY);
                    setLoadViewBottomMargin(distanY);
                    upDateLoadStatus(distanY);
                    mCurrentDrag = true;
                }
                break;
        }
        return super.onTouchEvent(e);

    }

    private void upDateLoadStatus(int distanY) {
        if(distanY<=0){
            mCurrentLoadStatus = LOAD_STATUS_NORMAL;
        }else if(distanY<mLoadViewHeight){
            mCurrentLoadStatus = LOAD_STATUS_PUSH_DOWN_LOAD;
        }else {
            mCurrentLoadStatus = LOAD_STATUS_LOOSER_PUSH_DOWN_LOAD;
        }

        if(mLoadViewCreator != null){
            mLoadViewCreator.onPush(distanY,mLoadViewHeight,mCurrentLoadStatus);
        }
    }

    /**
     * 停止加载更多
     */
    public void onStopLoad(){
        mCurrentLoadStatus = LOAD_STATUS_NORMAL;
        if (mLoadViewCreator != null) {
            mLoadViewCreator.stopLoad();
        }

    }


    /**
     * 设置底部View的Margin
     * @param marginBottom
     */
    private void setLoadViewBottomMargin(int marginBottom) {
        if(marginBottom <0){
            marginBottom = 0;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mLoadView.getLayoutParams();
        marginLayoutParams.bottomMargin = marginBottom;
        mLoadView.setLayoutParams(marginLayoutParams);
    }


    /**
     * 在底部添加加载更多的View
     */
    private void addLoadView() {
        if (getAdapter() != null && mLoadViewCreator != null) {
            View loadView = mLoadViewCreator.getLoadView(this, getContext());
            if (loadView != null) {
                addFooterView(loadView);
                mLoadView = loadView;
            }
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     * 判断是不是滚动到了最底部，这个是从SwipeRefreshLayout里面copy过来的源代码
     */
    public boolean canScrollDown() {
        return ViewCompat.canScrollVertically(this, 1);
    }


    private onMoreLoadListener mMoreLoadListener;

    public void setmMoreLoadListener(onMoreLoadListener moreLoadListener) {
        this.mMoreLoadListener = moreLoadListener;
    }

    public interface onMoreLoadListener{
        void onMoreLoad();
    }

}
