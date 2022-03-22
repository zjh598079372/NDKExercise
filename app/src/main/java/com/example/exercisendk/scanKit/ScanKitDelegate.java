package com.example.exercisendk.scanKit;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.exercisendk.R;
import com.huawei.hms.hmsscankit.RemoteView;
import java.lang.reflect.Field;

public class ScanKitDelegate {
    private ScanKitDelegate() {
    }

    private Activity activity;
    private RemoteView mRemoteView;
    private boolean mShowBackBtn = true;
    private boolean mShowTitle = true;
    private boolean mShowPickSelectImg = true;
    private boolean mShowCoverLayout = false;
    private boolean mShowFlashLight = true;
    private String mTitle = "扫码";
    private int mPicImgResId = R.mipmap.ic_launcher;
    private int mFlashLightMarginTop = 300;
    private int mFlashLightResId = R.mipmap.ic_launcher;


    public View show() {
        View view = View.inflate(activity, R.layout.scankit_customed_layout, null);

        //1、设置titleBar的TopMargin
        RelativeLayout.LayoutParams titleBarparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titleBarparams.topMargin = getStatusBarHeight(activity);
        ((FrameLayout) view.findViewById(R.id.titleBarFL)).setLayoutParams(titleBarparams);

        //2、设置左边的返回按钮的点击事件
        view.findViewById(R.id.btnFL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        //3、设置中间标题
        ((TextView) view.findViewById(R.id.titleTV)).setText(mTitle);
        view.findViewById(R.id.titleTV).setVisibility(mShowTitle ? View.VISIBLE : View.GONE);

        //4、设置右边的图片选择
        view.findViewById(R.id.photoFL).setVisibility(mShowPickSelectImg ? View.VISIBLE : View.GONE);
        ((FrameLayout) view.findViewById(R.id.photoFL)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRemoteView.selectPictureFromLocalFile();
            }
        });

        //5、设置闪光灯的MarginTop和点击事件
        RelativeLayout.LayoutParams flashLightparams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        flashLightparams.topMargin = mFlashLightMarginTop;
        ((LinearLayout) view.findViewById(R.id.flashLightLL)).setLayoutParams(flashLightparams);
        ((Button) view.findViewById(R.id.flashLightBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Button) v).setSelected(!mRemoteView.getLightStatus());
                mRemoteView.switchLight();
            }
        });

        return view;
    }


    public static class ScanKitDelegateBuilder {
        private RemoteView remoteView;
        private boolean showBackBtn = true;
        private boolean showTitle = true;
        private boolean showPickSelectImg = true;
        private boolean showCoverLayout = false;
        private boolean showFlashLight = true;
        private String title;
        private int picImgResId;
        private int flashLightResId;
        private int flashLightMarginTop;
        private Activity activity;

        public ScanKitDelegateBuilder(Activity activity) {
            this.activity = activity;
        }

        public ScanKitDelegateBuilder showBackBtn(boolean showBackBtn) {
            this.showBackBtn = showBackBtn;
            return this;
        }

        public ScanKitDelegateBuilder showTitle(boolean showTitle) {
            this.showTitle = showTitle;
            return this;
        }

        public ScanKitDelegateBuilder showPickSelectImg(boolean showPickSelectImg) {
            this.showPickSelectImg = showPickSelectImg;
            return this;
        }

        public ScanKitDelegateBuilder showCoverLayout(boolean showCoverLayout) {
            this.showCoverLayout = showCoverLayout;
            return this;
        }

        public ScanKitDelegateBuilder showFlashLight(boolean showFlashLight) {
            this.showFlashLight = showFlashLight;
            return this;
        }

        public ScanKitDelegateBuilder setTitle(String title) {
            this.title = title;
            return this;
        }


        public ScanKitDelegateBuilder setPicImgResId(int picImgResId) {
            this.picImgResId = picImgResId;
            return this;
        }

        public ScanKitDelegateBuilder setFlashLightResId(int flashLightResId) {
            this.flashLightResId = flashLightResId;
            return this;
        }

        public ScanKitDelegateBuilder setFlashLightMarginTop(int flashLightMarginTop) {
            this.flashLightMarginTop = flashLightMarginTop;
            return this;
        }

        public ScanKitDelegateBuilder setRemoteView(RemoteView remoteView) {
            this.remoteView = remoteView;
            return this;
        }


        public ScanKitDelegate build() {
            ScanKitDelegate scanKitDelegate = new ScanKitDelegate();
            scanKitDelegate.activity = activity;
            scanKitDelegate.mRemoteView = remoteView;
            scanKitDelegate.mShowBackBtn = showBackBtn;
            scanKitDelegate.mShowTitle = showTitle;
            scanKitDelegate.mShowPickSelectImg = showPickSelectImg;
            scanKitDelegate.mShowCoverLayout = showCoverLayout;
            scanKitDelegate.mShowFlashLight = showFlashLight;
            scanKitDelegate.mTitle = title;
            scanKitDelegate.mPicImgResId = picImgResId;
            scanKitDelegate.mFlashLightResId = flashLightResId;
            scanKitDelegate.mFlashLightMarginTop = flashLightMarginTop;
            return scanKitDelegate;
        }


    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }


}
