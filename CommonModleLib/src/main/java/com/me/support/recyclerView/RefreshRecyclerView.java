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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

public class RefreshRecyclerView extends WrapRecyclerView {
    private RefreshViewCreator mRefreshViewCreator;     //下拉刷新的辅助类
    private View mRefreshView;      //下位刷新的View
    private int mFingerDownY;     //手指按下的Y值
    private int mRefreshViewHeight;     //下拉刷新View的高度
    private int mCurrentRefreshStatus;  //当前的状态
    private int REFRESH_STATUS_NORMAL = 0x0011;     //默认状态
    private int REFRESH_STATUS_PULL_DOWN_REFRESH = 0x0022;     //下拉滑动状态
    private int REFRESH_STATUS_REFRESHING = 0x0033;     //正在刷新状态
    private int REFRESH_STATUS_LOOSEN_REFRESHING = 0x0044;     //松开刷新状态
    private boolean mCurrentDrag = false;   //当前是否正在拖动
    private float mDragIndex = 0.35f;

    public RefreshRecyclerView(@NonNull Context context) {
        super(context);
    }

    public RefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addRefreshViewCreator(RefreshViewCreator refreshViewCreator) {
        mRefreshViewCreator = refreshViewCreator;
        addRefreshView();
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshView();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("dispatch-->", "ACTION_DOWN");
                mFingerDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentDrag) {
                    Log.e("dispatch-->", "ACTION_UP");
                    restoreRefreshView();
                }
                break;
//            case MotionEvent.ACTION_MOVE:
//                //如果能继续滑动或正处理刷新状态时，不做处理
//                if (canScrollUp() || mCurrentRefreshStatus == REFRESH_STATUS_REFRESHING) {
//                    return super.onTouchEvent(ev);
//                }
//                //下拉刷新自动滑动的问题
//                if (mCurrentDrag) {
//                    scrollToPosition(0);
//                }
//                //获取手指触摸拖拽的距离
//                int distanceY = (int) ((ev.getRawY() - mFingerDownY) * mDragIndex);
//                if (distanceY > 0) {
//                    Log.e("dispatch-->", "ACTION_MOVE");
//                    int marginTop = distanceY - mRefreshViewHeight + 1;
//                    setRefreshViewMarginTop(marginTop);
//                    updateRefreshStatus(marginTop);
//                    mCurrentDrag = true;
//                }
//                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void restoreRefreshView() {
        if(mRefreshView != null){
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mRefreshView.getLayoutParams();
            int currentTopMargin = layoutParams.topMargin;
            int finalTopMargin = -mRefreshViewHeight;
            if (mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING) {
                mCurrentRefreshStatus = REFRESH_STATUS_REFRESHING;
                if (mRefreshViewCreator != null) {
                    mRefreshViewCreator.onRefreshing();
                }
            }

            int distance = currentTopMargin - finalTopMargin;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(currentTopMargin, finalTopMargin);
            valueAnimator.setDuration(distance * 2);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float margin = (Float) animation.getAnimatedValue();
                    setRefreshViewMarginTop((int) margin);
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    onStopRefresh();
                }
            });
            valueAnimator.start();
            mCurrentDrag = false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Log.e("onTouchEvent-->","ACTION_DOWN");
//                mFingerDownY = (int) e.getRawY();
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.e("onTouchEvent-->","ACTION_UP");
//                if (mCurrentDrag) {
//                    restoreRefreshView();
//                }
//                break;
            case MotionEvent.ACTION_MOVE:
                //如果能继续滑动或正处理刷新状态时，不做处理
                Log.e("onTouchEvent-->","ACTION_MOVE");
                if (canScrollUp() || mCurrentRefreshStatus == REFRESH_STATUS_REFRESHING) {
                    return super.onTouchEvent(e);
                }
                //下拉刷新自动滑动的问题
                if (mCurrentDrag) {
                    scrollToPosition(0);
                }
                //获取手指触摸拖拽的距离
                int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);
                if(distanceY>0){
                    int marginTop = distanceY - mRefreshViewHeight;
                    setRefreshViewMarginTop(marginTop);
                    updateRefreshStatus(marginTop);
                    mCurrentDrag = true;

                }
                break;
        }

        return super.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        Log.e("onIntercept-->", "" + e.getAction());
        Log.e("onIntercept--11-->", "" + super.onInterceptTouchEvent(e));
        return super.onInterceptTouchEvent(e);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            if (mRefreshView != null && mRefreshViewHeight <= 0) {
                mRefreshViewHeight = mRefreshView.getMeasuredHeight();
                Log.e("onLayout-->","mRefreshViewHeight="+mRefreshViewHeight);
                if (mRefreshViewHeight > 0) {
                    //隐藏头部刷新的View  marginTop 多留出1px,防止无法判断滑动到头的问题
                    setRefreshViewMarginTop(-mRefreshViewHeight);
                }
            }
        }

    }

    private void setRefreshViewMarginTop(int marginTop) {
        if(mRefreshView != null){
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mRefreshView.getLayoutParams();
            if (marginTop < -mRefreshViewHeight ) {
                marginTop = -mRefreshViewHeight ;
            }
            layoutParams.topMargin = marginTop;
            mRefreshView.setLayoutParams(layoutParams);
        }
    }

    private void updateRefreshStatus(int marginTop) {
        if (marginTop <= -mRefreshViewHeight) {
            mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        } else if (marginTop < 0) {
            mCurrentRefreshStatus = REFRESH_STATUS_PULL_DOWN_REFRESH;
        } else {
            mCurrentRefreshStatus = REFRESH_STATUS_LOOSEN_REFRESHING;
        }

        if (mRefreshViewCreator != null) {
            mRefreshViewCreator.onPull(marginTop, mRefreshViewHeight, mCurrentRefreshStatus);
        }
    }

    private void addRefreshView() {
        if ((getAdapter() != null) && (mRefreshViewCreator != null)) {
            View refreshView = mRefreshViewCreator.getRefreshView(this, getContext());
            if (refreshView != null) {
                addHeaderView(refreshView);
                mRefreshView = refreshView;
            }
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     * 判断是不是滚动到了最顶部，这个是从SwipeRefreshLayout里面copy过来的源代码
     */
    public boolean canScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(this, -1) || this.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(this, -1);
        }
    }

    /**
     * 停止刷新
     */
    public void onStopRefresh() {
        mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        if (mRefreshViewCreator != null) {
            mRefreshViewCreator.onStopRefresh();
        }
    }
}
