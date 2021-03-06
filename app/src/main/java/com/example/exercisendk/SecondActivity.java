package com.example.exercisendk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.exercisendk.base.BaseActivity;
import com.example.exercisendk.callNative.NativeBitmap;
import com.me.support.utils.LogUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class SecondActivity extends BaseActivity {

    BigImageView bigImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        bigImageView = findViewById(R.id.bigImageView);
        final Bitmap src_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.arrow);
//        Bitmap.createBitmap()
        ((ImageView)findViewById(R.id.image)).setImageBitmap(src_bitmap);
        findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NativeUtil.generateGrayBitmap(src_bitmap);
//                ((ImageView)findViewById(R.id.image)).setImageBitmap(src_bitmap);
//                ((ImageView)findViewById(R.id.image)).setImageBitmap(NativeUtil.againstWorld(src_bitmap));
                Bitmap newBitmap = NativeBitmap.mirrorImage(src_bitmap);
                ((ImageView)findViewById(R.id.image)).setImageBitmap(newBitmap);
            }
        });

        findViewById(R.id.tv_intercept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e("getAppPackgeName-->"+ NativeBitmap.getAppPackgeName(SecondActivity.this));

                Bitmap src_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_comfort_level);

                int src_width = src_bitmap.getWidth();
                int src_height = src_bitmap.getHeight();
                Bitmap dst_bitmap = Bitmap.createBitmap(src_width << 2,src_height << 2,src_bitmap.getConfig());
//                int[] pixels = new int[src_width*src_height];
//                src_bitmap.getPixels(pixels,0,src_width,0,0,src_width,src_height);
//                dst_bitmap.setPixels(pixels,0,src_width,0,0,src_width,src_height);
//                ((ImageView)findViewById(R.id.image)).setScaleX(-1);
//                ((ImageView)findViewById(R.id.image)).setImageBitmap(dst_bitmap);
                Matrix matrix = new Matrix();
                matrix.setScale(1,-1);
                matrix.postTranslate(src_width,0);
                ((ImageView)findViewById(R.id.image)).setImageBitmap(Bitmap.createBitmap(src_bitmap,0,0,src_width ,src_height,matrix ,false));

            }
        });

//        BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
    }

//    public String getPackageName(Context context) {
//        return "";
//    }

    public String getSHA1Signature(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            byte[] cert = info.signatures[0].toByteArray();

            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
