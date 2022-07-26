package com.example.exercisendk;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        findViewById(R.id.playBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions permissions = Permissions.build("android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.READ_EXTERNAL_STORAGE");
                SoulPermission.getInstance().checkAndRequestPermissions(permissions, new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        ///storage/sdcard0/Android/data/com.example.exercisendk/files/xcreen_20180607_225659.mp4
                        final NativePlayer nativePlayer = new NativePlayer();
                        nativePlayer.setmJNICallbackListener(new NativePlayer.JNICallbackListener() {
                            @Override
                            public void onError(int type, String message) {
                                LogUtil.e(TAG + "type-->" + type + "\t+message-->" + message);
                                showToast(message);

                            }

                            @Override
                            public void onSuccess() {
                                LogUtil.e(TAG + "onSuccess-->");
                            }

                            @Override
                            public void onPrepared() {
                                nativePlayer.nPlay();
                            }
                        });

                        nativePlayer.nPrepared(getExternalFilesDir("").getAbsolutePath() + "/test1.mkv");
                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {

                    }
                });

            }
        });
    }
}