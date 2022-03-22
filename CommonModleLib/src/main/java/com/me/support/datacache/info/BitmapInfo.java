package com.me.support.datacache.info;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.me.support.app.BaseApplication;
import com.me.support.utils.ExecutorUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;

public class BitmapInfo {
    private static BitmapInfo mBitmapInfo = new BitmapInfo();

    private String imagCacheDir = "imag";

    private BitmapInfo() {}

    public static BitmapInfo getInstance() {
        return mBitmapInfo;
    }

    /**
     * 保存Bitmap到本地
     *
     * @param bitmap bitmap对象
     * @param path   保存路径
     *
     *               用例示范：
     *               originPath 相机返回的url
     *               finalPath 要保存的路径
     *               调用该方法前，应先对Bitmap进行缩放、旋转处理
     *               if (requestCode == ActivityHelper.OPEN_SYSTEM_CAMERA) {   //系统相机拍照
     *               //如果是打开相机拍照得到的照片要先缩放再旋转
     *               int rotation = BitmapUtil.getRotateDegree(originPath);
     *               AppDataCache.withBitmap().saveBitmap(
     *               BitmapUtil.rotateBitmap(BitmapUtil.loadOuterImageByScale(originPath
     *               , BitmapUtil.getNeedScaleX(originPath
     *               , 1280))
     *               , rotation
     *               , false)
     *               , finalPath, Bitmap.CompressFormat.JPEG);
     *               BitmapUtil.saveBitmap(BitmapUtil.rotateBitmap(BitmapUtil.loadOuterImageByScale(originPath, BitmapUtil.getNeedScaleX(originPath, 1280)), rotation, false), finalPath, Bitmap.CompressFormat.JPEG);
     *               } else {
     *               //图库选择的照片只要缩放就行
     *               AppDataCache.withBitmap().saveBitmap(BitmapUtil.loadOuterImageByScale(originPath, BitmapUtil.getNeedScaleX(originPath, 1280)), finalPath, Bitmap.CompressFormat.JPEG);
     *               }
     **/
    public void saveBitmap(final Bitmap bitmap, String path, final Bitmap.CompressFormat format) {
        File file = null;
        try {
            file = new File(BaseApplication.getContext().getExternalFilesDir("").getAbsolutePath() + File.separator + imagCacheDir, path);
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        final File finalFile = file;
        if (finalFile == null) return;
        ExecutorUtil.submitRunnable(new Runnable() {
            @Override
            public void run() {
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(finalFile);
                    bitmap.compress(format, 90, out);
                    out.flush();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }


    /**
     * 以RGB_565的设定读取外部图片
     *
     * @param originPath 外部资源路径
     * @param scale    需求缩小倍数（2的次方数）
     * @return 按照设定解码后的Bitmap
     *
     * 用例示范：
     * AppDataCache.withBitmap()..readBitmapByScal(originPath, BitmapUtil.getNeedScaleX(originPath, 1280))
     **/
    public  Bitmap readBitmapByScal(String originPath, int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(originPath, options);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        if (scale != 0) {
            options.inSampleSize = scale;
        }
        options.inDither = false;
        options.inPurgeable = true;
        options.inJustDecodeBounds = false;
        return new SoftReference<Bitmap>(BitmapFactory.decodeFile(originPath, options)).get();
    }
}
