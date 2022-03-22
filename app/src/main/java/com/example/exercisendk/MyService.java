package com.example.exercisendk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationUtil.showServiceNotification(this,MainActivity.class);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        NotificationUtil.cancelAll();
        super.onDestroy();
    }
}
