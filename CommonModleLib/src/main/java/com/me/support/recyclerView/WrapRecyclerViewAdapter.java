package com.me.support.recyclerView;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class WrapRecyclerViewAdapter extends RecyclerView.Adapter {
    private int BASE_ITEM_TYPE_HEADER = 100000;   //基本头部的开始位置
    private int BASE_ITEM_TYPE_FOOTER = 200000;   //基本底部的开始位置
    //存放头部的View
    private SparseArray<View> mHeadViews;
    //存放底部的View
    private SparseArray<View> mFooterViews;
    //RecyclerView.Adapter
    private RecyclerView.Adapter mAdapter;

    public WrapRecyclerViewAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mHeadViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isHeadViewType(viewType)) {
            return new RecyclerView.ViewHolder(mHeadViews.get(viewType)) {
            };
        } else if (isFootViewType(viewType)) {
            return new RecyclerView.ViewHolder(mFooterViews.get(viewType)) {
            };
        } else {
            return mAdapter.createViewHolder(parent, viewType);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isFooterPosition(position) || isHeaderPosition(position)) return;

        position = position - mHeadViews.size();
        mAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mHeadViews.size() + mAdapter.getItemCount() + mFooterViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return mHeadViews.keyAt(position);
        }

        if (isFooterPosition(position)) {
            position = position - mHeadViews.size() - mAdapter.getItemCount();
            return mFooterViews.keyAt(position);
        }
        position = position - mHeadViews.size();
        return mAdapter.getItemViewType(position);
    }

    //是否是底部view的位置
    private boolean isFooterPosition(int position) {
        return (position + 1 > mHeadViews.size() + mAdapter.getItemCount());

    }

    //是否是顶部view的位置
    private boolean isHeaderPosition(int position) {
        return position < mHeadViews.size();
    }

    /**
     * 是否是头部View类型
     *
     * @param key
     * @return
     */
    private boolean isHeadViewType(int key) {
        Log.e("index-->",mHeadViews.indexOfKey(key)+"___==="+key);
        return mHeadViews.indexOfKey(key) >= 0;
    }

    /**
     * 是否是底部view的类型
     *
     * @param key
     * @return
     */
    private boolean isFootViewType(int key) {
        return mFooterViews.indexOfKey(key) >= 0;
    }

    /**
     * 添加头部View
     *
     * @param headerView
     */
    public void addHeaderView(View headerView) {
        int index = mHeadViews.indexOfValue(headerView);
        if (index > -1) return;
        mHeadViews.put(BASE_ITEM_TYPE_HEADER++, headerView);
        notifyDataSetChanged();
    }

    /**
     * 添加底部View
     *
     * @param footerView
     */
    public void addFooterView(View footerView) {
        int index = mFooterViews.indexOfValue(footerView);
        if (index > -1) return;
        mFooterViews.put(BASE_ITEM_TYPE_FOOTER++, footerView);
        notifyDataSetChanged();
    }

    /**
     * 删除头部View
     *
     * @param headerView
     */
    public void removeHeaderView(View headerView) {
        int index = mHeadViews.indexOfValue(headerView);
        Log.e("remove-->","index=="+index);
        if (index < 0) return;
        mHeadViews.removeAt(index);
        Log.e("remove-->","135==mHeadViews="+mHeadViews.size());
        notifyDataSetChanged();
    }

    /**
     * 删除底部View
     *
     * @param footerView
     */
    public void removeFooterView(View footerView) {
        int index = mFooterViews.indexOfValue(footerView);
        if (index < 0) return;
        mFooterViews.removeAt(index);
        notifyDataSetChanged();

    }

    /**
     * 解决GridLayoutManager 头部或底部不能单独占一行的问题。
     * @param recyclerView
     */
    public void adjustSpanSize(RecyclerView recyclerView){
        if(recyclerView.getLayoutManager() instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter = isHeaderPosition(position) || isFooterPosition(position);
                    return isHeaderOrFooter ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

}
