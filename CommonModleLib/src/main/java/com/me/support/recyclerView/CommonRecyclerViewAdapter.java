package com.me.support.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> mDatas;
    private int mResourceId;
    private Context mContext;


    public CommonRecyclerViewAdapter(List<T> datas, int resourceId) {
        mDatas = datas;
        mResourceId = resourceId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mResourceId, parent, false);
        mContext = parent.getContext().getApplicationContext();
        return new MyViewHolder(view, mContext) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        convert(new MyViewHolder(holder.itemView,mContext), mDatas.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public abstract void convert(MyViewHolder holder, T data, int position);


}
