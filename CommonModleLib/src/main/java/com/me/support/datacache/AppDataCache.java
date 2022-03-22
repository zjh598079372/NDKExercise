package com.me.support.datacache;


import com.me.support.datacache.info.BitmapInfo;
import com.me.support.datacache.info.DbInfo;
import com.me.support.datacache.info.FileCacheInfo;
import com.me.support.datacache.info.SharedPreferencesInfo;

public abstract class AppDataCache {

    public abstract SharedPreferencesInfo getSharedPreferencesInfo();

    public abstract FileCacheInfo getFileCacehInfo();

    public abstract DbInfo getDbInfo();

    public abstract BitmapInfo getBitmapInfo();

    private static AppDataCache mAppDataCache;

    protected AppDataCache() {
    }

    public static SharedPreferencesInfo withSp() {
        return getAppDataCacheInstance().getSharedPreferencesInfo();
    }

    public static FileCacheInfo withFile() {
        return getAppDataCacheInstance().getFileCacehInfo();
    }

    public static DbInfo withDb() {
        return getAppDataCacheInstance().getDbInfo();
    }

    public static BitmapInfo withBitmap(){
        return getAppDataCacheInstance().getBitmapInfo();
    }

    private static AppDataCache getAppDataCacheInstance() {
        if (mAppDataCache == null) {
            synchronized (AppDataCache.class) {
                if (mAppDataCache == null) {
                    mAppDataCache = new AppDataCacheImpl();
                }
            }

        }
        return mAppDataCache;
    }
}
