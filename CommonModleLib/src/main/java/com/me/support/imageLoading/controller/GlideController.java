package com.me.support.imageLoading.controller;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.me.support.app.AppManager;

public class GlideController extends BaseController {
    @Override
    public void init() {

    }

    @Override
    public <T> void loadImage(ImageView imageView, T url) {
        loadImage(imageView, url, 0, 0);
    }

    @Override
    public <C, T> void loadImage(C clazz, ImageView imageView, T url) {

    }

    @Override
    public <T> void loadImage(ImageView imageView, T url, int errorId, int placeId) {
        Glide.with(AppManager.getInstance().currentActivity()).load(url).placeholder(placeId == 0 ? mDefaultPlaceId : placeId)
                .error(errorId == 0 ? mDefaultErrorId : errorId).into(imageView);
    }

    @Override
    public <C, T> void loadImage(C clazz, ImageView imageView, T url, int errorId, int placeId) {

    }

    @Override
    public <T> void loadCircleImage(ImageView imageView, T url) {
        loadCircleImage(imageView, url, 0, 0);
    }

    @Override
    public <C, T> void loadCircleImage(C clazz, ImageView imageView, T url) {

    }

    @Override
    public <T> void loadCircleImage(ImageView imageView, T url, int errorId, int placeId) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);  //此处必须设置为CENTR_INSIDE类型，否则可能出现不是正常的圆型。
        /*Glide.with(context).load(url).placeholder(placeId).error(errorId).transform(new GlideCircleBitmapTransform()).into(imageView);*/
        Glide.with(AppManager.getInstance().currentActivity()).load(url).placeholder(placeId == 0 ? mDefaultPlaceId : placeId)
                .error(errorId == 0 ? mDefaultErrorId : errorId).transform(new CircleCrop()).into(imageView);
    }

    @Override
    public <C, T> void loadCircleImage(C clazz, ImageView imageView, T url, int errorId, int placeId) {

    }

    @Override
    public <T> void loadRoundImage(ImageView imageView, T url, int radius) {
        loadRoundImage(imageView, url, radius);
    }

    @Override
    public <C, T> void loadRoundImage(C clazz, ImageView imageView, T url, int radius) {

    }

    @Override
    public <T> void loadRoundImage(ImageView imageView, T url, int errorId, int placeId, int radius) {
        /*Glide.with(mContext).load(url).fitCenter().placeholder(placeId == 0 ? mDefaultPlaceId : placeId).error(errorId == 0 ?
         mDefaultErrorId : errorId).transform(new CenterCrop(), new GlideRoundBitmapTransform(radius)).into(imageView);*/
        Glide.with(AppManager.getInstance().currentActivity()).load(url).fitCenter().placeholder(placeId == 0 ? mDefaultPlaceId : placeId)
                .error(errorId == 0 ? mDefaultErrorId : errorId).transform(new CenterCrop(), new RoundedCorners(radius)).into(imageView);
    }

    @Override
    public <C, T> void loadRoundImage(C clazz, ImageView imageView, T url, int errorId, int placeId, int radius) {

    }

}
