package com.example.exercisendk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.exercisendk.scanKit.ScanKitDelegate;
import com.huawei.hms.hmsscankit.OnResultCallback;
import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.scankit.drawable.ScanDrawable;

public class ScanActivity extends AppCompatActivity {

    private final int SCAN_FRAME_SIZE = 320;
    private FrameLayout frameLayout;
    private RemoteView mRemoteView;
    private ScanDrawable mScanDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        frameLayout = findViewById(R.id.frameLayout);

        //1、获取屏幕的宽度
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int mScreenWidth = displayMetrics.widthPixels;
        int mScreenHeight = displayMetrics.heightPixels;
        float density = displayMetrics.density;
        int scanFrameSize = (int) (SCAN_FRAME_SIZE * density);
        Rect rect = new Rect();
        rect.left = (mScreenWidth / 2 - scanFrameSize / 2);
        rect.right = (mScreenWidth / 2 + scanFrameSize / 2);
        rect.top = (mScreenHeight / 2 - scanFrameSize / 2);
        rect.bottom = (mScreenHeight / 2 + scanFrameSize / 2);

        //2、初始化RemoteView, 并且设置回调监听，这里可能设置扫码选项
        mRemoteView = new RemoteView.Builder().
                setContext(this).
                setBoundingBox(new Rect(0, 0, mScreenWidth, mScreenHeight)). //此处设置了全屏扫码，如果要设置扫码框扫码，可以用上面的rect
                setFormat(HmsScan.ALL_SCAN_TYPE).build();
        mRemoteView.onCreate(savedInstanceState);
        mRemoteView.setOnResultCallback(new OnResultCallback() {
            public void onResult(HmsScan[] var1) {
                if (var1 != null && var1.length > 0 && var1[0] != null && !TextUtils.isEmpty(var1[0].originalValue)) {
                    Log.i("ScanKitActivity", "onResult: obtain scanResult");
                    Intent var2;
                    Intent intent = new Intent();
                    intent.putExtra("SCAN_RESULT", var1[0]);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

            }
        });

        // 3. 添加 RemoteView 至布局.
        frameLayout.addView(mRemoteView);
        ImageView imageView = new ImageView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.width = rect.right - rect.left;
        params.height = rect.bottom - rect.top;
        params.gravity = Gravity.CENTER;
        imageView.setLayoutParams(params);
        mScanDrawable = new ScanDrawable(getResources());
        mScanDrawable.setBounds(rect);
        imageView.setBackground(mScanDrawable);
        mScanDrawable.start();
        frameLayout.addView(imageView);

        //4、添加DelegateView
        ScanKitDelegate.ScanKitDelegateBuilder builder = new ScanKitDelegate.ScanKitDelegateBuilder(ScanActivity.this);
        View scanKitView = builder.setTitle("扫描二维码").
                setFlashLightMarginTop((int) (rect.bottom + density * 5)).
                setRemoteView(mRemoteView).
                build().
                show();
        frameLayout.addView(scanKitView);


    }

    protected void onStart() {
        super.onStart();
        mRemoteView.onStart();
        Log.i("ScanKitActivity", "ScankitActivity onStart");
    }

    protected void onResume() {
        super.onResume();
        mRemoteView.onResume();

        Log.i("ScanKitActivity", "ScankitActivity onResume");
    }

    protected void onPause() {
        super.onPause();
        mRemoteView.onPause();
        mScanDrawable.stop();
        Log.i("ScanKitActivity", "ScankitActivity onPause");
    }

    protected void onDestroy() {
        super.onDestroy();
        mRemoteView.onDestroy();
        Log.i("ScanKitActivity", "ScankitActivity onDestroy");
        OrientationEventListener var1;


    }

    protected void onStop() {
        super.onStop();
        mRemoteView.onStop();
        Log.i("ScanKitActivity", "ScankitActivity onStop");
    }

    protected void onActivityResult(int var1, int var2, Intent var3) {
        super.onActivityResult(var1, var2, var3);
        mRemoteView.onActivityResult(var1, var2, var3);
    }

    public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
        mRemoteView.onRequestPermissionsResult(var1, var2, var3, this);
        super.onRequestPermissionsResult(var1, var2, var3);
    }
}
