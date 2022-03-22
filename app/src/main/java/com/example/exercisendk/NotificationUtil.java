package com.example.exercisendk;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationUtil {

    private static NotificationManager mNotificationManager;
    private static final int SERVICE_NOTIFICATION = 31414;
    public static final int PENDINGCALL_NOTIFICATION = SERVICE_NOTIFICATION + 1;
    protected static String channelID = "PortSipService";
    protected static String callChannelID = "Call Channel";
    private static boolean isShowServiceNotifiCation = false;

    public static void showServiceNotification(Service context, Class comeInActivity) {
//        if(isShowServiceNotifiCation) return;
//        isShowServiceNotifiCation = true;
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            NotificationChannel callChannel = new NotificationChannel(callChannelID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mNotificationManager.createNotificationChannel(callChannel);
        }
        showNotification(context, comeInActivity);
    }

    ;

    private static void showNotification(Service context, Class comeInActivity) {
        Intent intent = new Intent(context, comeInActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0/*requestCode*/, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, channelID);
        } else {
            builder = new Notification.Builder(context);
        }
        builder.setContentText(context.getString(R.string.app_name))
//                .setContentTitle(context.getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent)
                .build();// getNotification()
        context.startForeground(SERVICE_NOTIFICATION, builder.build());
        Log.e("showNoti-->","NotificationUtil====61");
    }

    public static void showPendingCallNotification(Context context, String contenTitle, String contenText, Intent intent) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, callChannelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(contenTitle)
                .setContentText(contenText)
                .setAutoCancel(true)
                .setShowWhen(true)
                .setContentIntent(contentIntent)
                .setFullScreenIntent(contentIntent, true);
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        }
        mNotificationManager.notify(PENDINGCALL_NOTIFICATION, builder.build());
    }

    public static void cancelPendingcallNotification() {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(PENDINGCALL_NOTIFICATION);
        }
    }

    public static void cancelAll() {
        if (mNotificationManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                mNotificationManager.deleteNotificationChannel(channelID);
                mNotificationManager.deleteNotificationChannel(callChannelID);
            }
            mNotificationManager.cancelAll();
            Log.e("showNoti-->","NotificationUtil====94");
            mNotificationManager = null;
        }
        isShowServiceNotifiCation = false;
    }
}