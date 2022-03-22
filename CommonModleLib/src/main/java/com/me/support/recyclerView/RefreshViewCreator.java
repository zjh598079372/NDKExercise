package com.me.support.recyclerView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 下拉刷新的辅助类
 */
public abstract class RefreshViewCreator {

    /**
     * 获取下拉刷新的View
     * @param parent recyclerView
     * @param context 上下文
     * @return
     */
    public abstract View getRefreshView(ViewGroup parent, Context context);

    /**
     * 正在下拉
     * @param currentDragHeight  当前拖动的高度
     * @param refreshViewHeight  下拉刷新view的高度
     * @param currentRefreshStatus   当前的刷新状态
     */
    public abstract void onPull(int currentDragHeight, int refreshViewHeight,int currentRefreshStatus);

    /**
     * 正在刷新
     */
    public abstract void onRefreshing();

    /**
     * 停止刷新
     */
    public abstract void onStopRefresh();
}
