package com.me.support.recyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.me.support.app.AppManager;
import com.me.support.imageLoading.ImageLoadingManager;


public class MyViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> sparseArray;
    private Context mContext;

    public MyViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        sparseArray = new SparseArray<>();
        mContext = context;
    }

    public MyViewHolder setText(int id, String text) {
        TextView textView = getView(id);
        textView.setText(text);
        return this;
    }

    public MyViewHolder setTextColor(int id, int colorId) {
        TextView textView = getView(id);
        textView.setTextColor(colorId);
        return this;
    }

    public MyViewHolder setTextSize(int id, float size) {
        TextView textView = getView(id);
        textView.setTextSize(size);
        return this;
    }

    public MyViewHolder setViewVisibility(int id, boolean isGone) {
        View view = getView(id);
        if (isGone) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置正常的图片
     * @param viewId
     * @param type 资源ID,Bitmap,Drawable或网络url
     * @return
     */
    public <T>MyViewHolder setNormalImage(int viewId, T type) {
       return setNormalImage(AppManager.getInstance().currentActivity(),viewId,type);
    }

    /**
     * 设置正常的图片
     * @param viewId
     * @param type 资源ID,Bitmap,Drawable或网络url
     * @return
     */
    public <C,T>MyViewHolder setNormalImage(C clazz,int viewId, T type) {
        ImageView imageView = getView(viewId);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if(type instanceof Integer){
            imageView.setImageResource((Integer) type);
        }else if(type instanceof Drawable){
            imageView.setImageDrawable((Drawable) type);
        }else if(type instanceof Bitmap){
            imageView.setImageBitmap((Bitmap) type);
        }else if(type instanceof String){
//            ImageLoadingManager.getInstance().loadCircleImage(imageView,(String)type,errorId,placeId);
        }
        return this;
    }

    /**
     * 设置正常的图片
     * @param viewId
     * @param type 资源ID,Bitmap,Drawable或网络url
     * @return
     */
    public <T>MyViewHolder setNormalImage(int viewId, T type,int errorId, int placeId) {
        ImageView imageView = getView(viewId);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if(type instanceof Integer){
            imageView.setImageResource((Integer) type);
        }else if(type instanceof Drawable){
            imageView.setImageDrawable((Drawable) type);
        }else if(type instanceof Bitmap){
            imageView.setImageBitmap((Bitmap) type);
        }else if(type instanceof String){
            ImageLoadingManager.getInstance().loadCircleImage(imageView,(String)type,errorId,placeId);
        }
        return this;
    }

    /**
     * 通过资源ID设置圆型的图片
     * @param viewId
     * @param resId
     * @return
     */
    public MyViewHolder setImageCircleByResource(int viewId, int resId) {
        setImageCircle(viewId,resId,0,0);
        return this;
    }

    /**
     * 通过资源ID设置圆角的图片
     * @param viewId
     * @param resId
     * @param radius
     * @return
     */
    public MyViewHolder setImageRoundByResource(int viewId, int resId,int radius) {
        setImageRound(viewId,resId,0,0,radius);
        return this;
    }

    /**
     * 通过url设置图片
     * @param viewId
     * @param url
     * @return
     */
    public <T>MyViewHolder setNormalImage1(int viewId, T url) {
        return setNormalImage(viewId,url,0,0);
    }

    /**
     * 通过url设置图片
     * @param viewId
     * @param url
     * @return
     */
//    public <T>MyViewHolder setNormalImage(int viewId, T url, int errorId, int placeId) {
//        ImageView imageView = getView(viewId);
//        ImageLoadingManager.getInstance().loadImage(imageView,url,errorId,placeId);
//        return this;
//    }

    /**
     * 通过url设置圆型的图片
     * @param viewId
     * @param url
     * @return
     */
    public MyViewHolder setImageCircleByUrl(int viewId, String url) {
        return setImageCircle(viewId, url, 0, 0);
    }

    /**
     * 设置圆型的图片
     * @param viewId
     * @param url
     * @param errorId
     * @param placeId
     * @param <T> 可以是url或资源ID
     * @return
     */
    public <T>MyViewHolder setImageCircle(int viewId, T url, int errorId, int placeId) {
        ImageView imageView = getView(viewId);
        ImageLoadingManager.getInstance().loadCircleImage(imageView,url,errorId,placeId);
        return this;
    }

    /**
     * 通过url设置圆角的图片
     * @param viewId
     * @param url
     * @param radius
     * @return
     */
    public MyViewHolder setImageRoundByUrl(int viewId, String url, int radius) {
        return setImageRound(viewId, url, 0, 0, radius);
    }

    /**
     * 设置圆角的图片
     * @param viewId
     * @param url
     * @param errorId
     * @param placeId
     * @param radius
     * @param <T>
     * @return
     */
    public <T>MyViewHolder setImageRound(int viewId, T url, int errorId, int placeId, int radius) {
        ImageView imageView = getView(viewId);
        ImageLoadingManager.getInstance().loadRoundImage(imageView,url,errorId,placeId,radius);
        return this;
    }

    public void setOnItemClickListener(View.OnClickListener listener){
        if(listener != null){
            itemView.setOnClickListener(listener);
        }
    }

    public void setOnItemLongClickListener(View.OnLongClickListener longClickListener) {
        if(longClickListener != null){
            itemView.setOnLongClickListener(longClickListener);
        }
    }

    public void setOnViewClickListener(int viewId, View.OnClickListener listener){
        if(listener != null ){
            getView(viewId).setOnClickListener(listener);
        }
    }


    private <V extends View> V getView(int id) {
        V v;
        if (sparseArray != null && sparseArray.get(id) != null) {
            return (V) sparseArray.get(id);
        }

        v = (V) itemView.findViewById(id);
        sparseArray.put(id, v);
        return v;
    }
}
