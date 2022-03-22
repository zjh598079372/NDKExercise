package com.me.support.datacache.info;

import android.content.Context;
import android.content.SharedPreferences;

import com.me.support.app.BaseApplication;


public class SharedPreferencesInfo {

    private static SharedPreferencesInfo mSharedPreferencesInfo = new SharedPreferencesInfo();
    private static String saveDir = "chouli";

    private SharedPreferencesInfo() {
    }

    public static SharedPreferencesInfo getInstance() {
        return mSharedPreferencesInfo;
    }

    public boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(saveDir, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public boolean getBoolean(String key) {
        SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(saveDir, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(saveDir, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(saveDir, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public void setSaveDir(String dir) {
        saveDir = dir;
    }
}
