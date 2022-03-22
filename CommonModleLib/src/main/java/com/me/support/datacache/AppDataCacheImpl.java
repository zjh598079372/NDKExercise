package com.me.support.datacache;


import com.me.support.datacache.info.BitmapInfo;
import com.me.support.datacache.info.DbInfo;
import com.me.support.datacache.info.FileCacheInfo;
import com.me.support.datacache.info.SharedPreferencesInfo;

public class AppDataCacheImpl extends AppDataCache {

    @Override
    public SharedPreferencesInfo getSharedPreferencesInfo() {
        return SharedPreferencesInfo.getInstance();
    }

    @Override
    public FileCacheInfo getFileCacehInfo() {
        return FileCacheInfo.getInstance();
    }

    @Override
    public DbInfo getDbInfo() {
        return DbInfo.getInstance();
    }

    @Override
    public BitmapInfo getBitmapInfo() {
        return BitmapInfo.getInstance();
    }
}
