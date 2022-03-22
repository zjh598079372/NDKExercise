package com.me.support.imageLoading;

import android.widget.ImageView;

import com.me.support.imageLoading.controller.BaseController;
import com.me.support.imageLoading.controller.GlideController;

public class ImageLoadingManager extends BaseController {
    private BaseController mBaseController;

    private static class Hoder{
        private static final ImageLoadingManager INSTANCE = new ImageLoadingManager();
    }

    public static ImageLoadingManager getInstance(){
        return Hoder.INSTANCE;
    }

    @Override
    public void init() {
        mBaseController = new GlideController();
    }

    @Override
    public <T> void loadImage(ImageView imageView, T url) {

    }

    @Override
    public <C,T> void loadImage(C clazz,ImageView imageView, T url) {
        mBaseController.loadImage(clazz,imageView,url);
    }

    @Override
    public <T> void loadImage(ImageView imageView, T url, int errorId, int placeId) {
        mBaseController.loadImage(imageView,url,errorId,placeId);
    }

    @Override
    public <C, T> void loadImage(C clazz, ImageView imageView, T url, int errorId, int placeId) {

    }

    @Override
    public <T> void loadCircleImage(ImageView imageView, T url) {
        mBaseController.loadCircleImage(imageView,url);
    }

    @Override
    public <C, T> void loadCircleImage(C clazz, ImageView imageView, T url) {

    }

    @Override
    public <T> void loadCircleImage(ImageView imageView, T url, int errorId, int placeId) {
        mBaseController.loadCircleImage(imageView,url,errorId,placeId);
    }

    @Override
    public <C, T> void loadCircleImage(C clazz, ImageView imageView, T url, int errorId, int placeId) {

    }

    @Override
    public <T> void loadRoundImage(ImageView imageView, T url, int radius) {
        mBaseController.loadRoundImage(imageView,url,radius);
    }

    @Override
    public <C, T> void loadRoundImage(C clazz, ImageView imageView, T url, int radius) {

    }

    @Override
    public <T> void loadRoundImage(ImageView imageView, T url, int errorId, int placeId, int radius) {
        mBaseController.loadRoundImage(imageView,url,errorId,placeId,radius);
    }

    @Override
    public <C, T> void loadRoundImage(C clazz, ImageView imageView, T url, int errorId, int placeId, int radius) {

    }


    public void setDefaultPlaceId(int defaultPlaceId){
        mBaseController.mDefaultPlaceId = defaultPlaceId;
    }

    public void setDefaultErrorId(int defaultErrorId){
        mBaseController.mDefaultErrorId = defaultErrorId;
    }


}
