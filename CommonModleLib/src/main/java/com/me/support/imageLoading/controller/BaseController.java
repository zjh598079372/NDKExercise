package com.me.support.imageLoading.controller;

import android.widget.ImageView;

import com.example.commonlibrary.R;

public abstract class BaseController {

    public int mDefaultPlaceId = R.mipmap.ic_chuoli;
    public int mDefaultErrorId = R.mipmap.ic_chuoli;

    public abstract void init();

    public abstract <T> void loadImage(ImageView imageView, T url);

    public abstract <C,T> void loadImage(C clazz,ImageView imageView, T url);

    public abstract <T> void loadImage(ImageView imageView, T url, int errorId, int placeId);

    public abstract <C,T> void loadImage(C clazz,ImageView imageView, T url, int errorId, int placeId);

    public abstract <T> void loadCircleImage(ImageView imageView, T url);

    public abstract <C,T> void loadCircleImage(C clazz,ImageView imageView, T url);

    public abstract <T> void loadCircleImage(ImageView imageView, T url, int errorId, int placeId);

    public abstract <C,T> void loadCircleImage(C clazz,ImageView imageView, T url, int errorId, int placeId);

    public abstract <T> void loadRoundImage(ImageView imageView, T url, int radius);

    public abstract <C,T> void loadRoundImage(C clazz,ImageView imageView, T url, int radius);

    public abstract <T> void loadRoundImage(ImageView imageView, T url, int errorId, int placeId, int radius);

    public abstract <C,T> void loadRoundImage(C clazz,ImageView imageView, T url, int errorId, int placeId, int radius);



}
