package com.example.exercisendk.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exercisendk.R;
import com.me.support.utils.ToastUtils;

public class BaseActivity extends AppCompatActivity {

    private boolean isIntercept = false;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mRootLayout;
    private View mLoadingFrame;
    private View mToastFrame;

    private Runnable hideToastRunnable = new Runnable() {
        @Override
        public void run() {
            mToastFrame.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutInflater = LayoutInflater.from(this);

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mRootLayout = findViewById(android.R.id.content);
        initToastFrame();
        initLoadingFrame();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isIntercept) return true;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if (isIntercept) return;
        super.onBackPressed();
    }

    private void initToastFrame() {
        if (mRootLayout != null) {
            if (mRootLayout instanceof FrameLayout) {
                mToastFrame = mLayoutInflater.inflate(R.layout.toast_frame, null);
                mRootLayout.addView(mToastFrame);
                mToastFrame.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
            }
        }
    }

    private void initLoadingFrame() {
        mLoadingFrame = mLayoutInflater.inflate(R.layout.loading_frame, null);
        mRootLayout.addView(mLoadingFrame);
        mLoadingFrame.getLayoutParams().width = -1;
        mLoadingFrame.getLayoutParams().height = -1;
        mLoadingFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    protected void setIntercept(boolean isIntercept) {
        this.isIntercept = isIntercept;
    }

    protected void showLoadingFrame(final boolean show) {
        if (mLoadingFrame == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoadingFrame.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * 提示文字
     *
     * @param text 文字字符串
     */
    protected void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.getHandler().removeCallbacks(hideToastRunnable);
                ((TextView) mToastFrame.findViewById(R.id.toastText)).setText(text);
                mToastFrame.findViewById(R.id.toastImage).setVisibility(View.GONE);
                mToastFrame.setVisibility(View.VISIBLE);
                ToastUtils.getHandler().postDelayed(hideToastRunnable, ToastUtils.TOAST_TIME);
            }
        });
    }

}
