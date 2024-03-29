package com.example.exercisendk;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

import com.example.exercisendk.base.BaseActivity;
import com.example.exercisendk.callNative.NativePlayer;
import com.me.support.utils.LogUtil;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;

public class VideoActivity extends BaseActivity {

    private static final String TAG = VideoActivity.class.getSimpleName() + "--->";
    private SurfaceView mSurfaceView;
    private NativePlayer nativePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mSurfaceView = findViewById(R.id.surfaceView);
        nativePlayer = new NativePlayer();
        findViewById(R.id.playBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions permissions = Permissions.build("android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.READ_EXTERNAL_STORAGE");
                SoulPermission.getInstance().checkAndRequestPermissions(permissions, new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        ///storage/sdcard0/Android/data/com.example.exercisendk/files/xcreen_20180607_225659.mp4

                        nativePlayer.setmJNICallbackListener(new NativePlayer.JNICallbackListener() {
                            @Override
                            public void onError(int type, String message) {
                                LogUtil.e(TAG + "type-->" + type + "\t+message-->" + message);
                                showLoadingFrame(false);
                                showToast(message);

                            }

                            @Override
                            public void onSuccess() {
                                LogUtil.e(TAG + "onSuccess-->");
                            }

                            @Override
                            public void onPrepared() {
                                LogUtil.e(TAG + "onPrepared-->");
                                showLoadingFrame(false);
                                nativePlayer.nSetSurface(mSurfaceView.getHolder().getSurface());
                                nativePlayer.nPlay();
                            }
                        });

//                        nativePlayer.nPreparedAsync(getExternalFilesDir("").getAbsolutePath() + "/test.mkv");
                        nativePlayer.nPreparedAsync("rtmp://192.168.1.165/myapp/stream");
//                        nativePlayer.nPreparedAsync("rtmp://127.0.0.1:1935/myapp/stream");

                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {

                    }
                });

            }
        });
    }

    @Override
    protected void onDestroy() {
        nativePlayer.nRelease();
        super.onDestroy();
    }
}