package com.me.support.recyclerView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class WrapRecyclerView extends RecyclerView {
    private Adapter mAdapter;
    private WrapRecyclerViewAdapter mWrapAdapter;


    public WrapRecyclerView(@NonNull Context context) {
        super(context);
    }

    public WrapRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        // 为了防止多次设置Adapter
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
            mAdapter = null;
        }

        mAdapter = adapter;

        if(adapter instanceof WrapRecyclerViewAdapter){
            mWrapAdapter = (WrapRecyclerViewAdapter) adapter;
        }else {
            mWrapAdapter = new WrapRecyclerViewAdapter(adapter);
        }

        super.setAdapter(mWrapAdapter);

        mAdapter.registerAdapterDataObserver(mDataObserver);
        //解决GridLayout时头部和底部单独占一行
        mWrapAdapter.adjustSpanSize(this);
    }

    /**
     * 添加头部
     * @param headerView
     */
    public void addHeaderView(View headerView){
        if(mWrapAdapter != null){
            mWrapAdapter.addHeaderView(headerView);
        }
    }

    /**
     * 添加尾部
     * @param footerView
     */
    public void addFooterView(View footerView){
        if(mWrapAdapter != null){
            mWrapAdapter.addFooterView(footerView);
        }
    }

    /**
     * 删除头部
     * @param headerView
     */
    public void removeHeaderView(View headerView){
        if(mWrapAdapter != null){
            mWrapAdapter.removeHeaderView(headerView);
        }
    }

    /**
     * 删除尾部
     * @param footerView
     */
    public void removeFooterView(View footerView){
        if (mWrapAdapter != null) {
            mWrapAdapter.removeFooterView(footerView);
        }
    }

    private View mEmptyView, mLoadingView;

    // 省略...上一期已有代码

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapAdapter != mAdapter)
                mWrapAdapter.notifyDataSetChanged();

            dataChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapAdapter != mAdapter)
                mWrapAdapter.notifyItemRemoved(positionStart);
            dataChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemMoved没效果
            if (mWrapAdapter != mAdapter)
                mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapAdapter != mAdapter)
                mWrapAdapter.notifyItemChanged(positionStart);
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapAdapter != mAdapter)
                mWrapAdapter.notifyItemChanged(positionStart, payload);
            dataChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemInserted没效果
            if (mWrapAdapter != mAdapter)
                mWrapAdapter.notifyItemInserted(positionStart);
            dataChanged();
        }
    };

    /**
     * 添加一个空列表数据页面
     */
    public void addEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * 添加一个正在加载数据的页面
     */
    public void addLoadingView(View loadingView) {
        this.mLoadingView = loadingView;
    }

    /**
     * Adapter数据改变的方法
     */
    private void dataChanged() {
        if (mAdapter.getItemCount() == 0) {
            // 没有数据
            if (mEmptyView != null) {
                mEmptyView.setVisibility(VISIBLE);
            } else {
                mEmptyView.setVisibility(GONE);
            }
        }
    }

}
