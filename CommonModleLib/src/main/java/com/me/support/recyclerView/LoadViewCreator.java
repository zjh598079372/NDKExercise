package com.me.support.recyclerView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract class LoadViewCreator {

    /**
     * 获取加载更多的View
     * @param parent
     * @param context
     * @return
     */
    public abstract View getLoadView(ViewGroup parent, Context context);

    /**
     * 正在上拉加载
     * @param currentDragHeight   当前上拉滑动的高度
     * @param loadViewHeight      加载更多View的总高度
     * @param currentLoadStatus   加载View当前的状态
     */
    public abstract void onPush(int currentDragHeight, int loadViewHeight, int currentLoadStatus);

    /**
     * 正在加载更多
     */
    public abstract void onLoading();

    /**
     * 停止加载更多
     */
    public abstract void stopLoad();
}
