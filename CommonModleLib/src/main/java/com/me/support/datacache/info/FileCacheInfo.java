package com.me.support.datacache.info;

import android.content.Context;
import android.util.Log;

import com.me.support.app.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCacheInfo {
    private static FileCacheInfo mFileCacheInfo = new FileCacheInfo();
    private String fileCacheDir = "chouli";
    private FileOutputStream mFos;
    private ByteArrayOutputStream mArr;
    private FileInputStream mFis;

    public static FileCacheInfo getInstance() {
        return mFileCacheInfo;
    }

    public void putString(Context context, String key, String result) {
        try {
            File file = new File(context.getExternalFilesDir("").getAbsolutePath() + File.separator + fileCacheDir, key);
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            if (!file.exists()) {

                file.createNewFile();
            }
            mFos = new FileOutputStream(file);
            mFos.write(result.getBytes());
            mFos.flush();
            Log.e("FileCacheInfo", "本地文件缓存成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileCacheInfo", "本地文件缓存失败");
        } finally {
            try {
                if (mFos != null) {
                    mFos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void putString(String key, String result) {
        putString(BaseApplication.getContext(), key, result);
    }

    public String getString(Context context, String key) {
        String result = "";
        try {
            File file = new File(context.getExternalFilesDir("").getAbsolutePath() + File.separator + fileCacheDir, key);
            if (file != null) {
                int len = 0;
                mArr = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];

                mFis = new FileInputStream(file);
                while ((len = mFis.read(buffer)) != -1) {
                    mArr.write(buffer, 0, len);
                }

                result = mArr.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mFis != null) {
                try {
                    mFis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (mArr != null) {
                try {
                    mArr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public String getString(String key) {
        return getString(BaseApplication.getContext(), key);
    }

    public void setFileCacheDir(String fileCacheDir) {
        this.fileCacheDir = fileCacheDir;
    }
}
