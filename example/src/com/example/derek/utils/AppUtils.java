package com.example.derek.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import com.example.derek.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by liudingyu on 14/11/27.
 */
public class AppUtils {

    /**
     * 使用v4包中得builder来创建操作消息栏通知
     */
    private final static int NOTIFY_ID = 17;

    private void cancelNotify(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFY_ID);
    }

    private void sendNotify(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PackageManager packageManager = context.getPackageManager();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher)).setContentTitle("content title")
                .setContentText("content text").setTicker("Ticker").setOngoing(true);
        Intent i = packageManager.getLaunchIntentForPackage(context.getPackageName());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, NOTIFY_ID, i, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        notificationManager.notify(NOTIFY_ID, builder.build());
    }


    public static Socket mSocket = null;
    /**
     * 在远端android环境下截获console信息并发往特定ip和destport
     */
    private void sendLogInfo() {
        StringBuilder builder = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("/system/bin/logcat");
            InputStream in = process.getInputStream();
            int len = 0;
            byte[] buff = new byte[1024];
            while ((len = in.read(buff)) != -1) {
                builder.append(new String(buff));
                sendMsg(buff, len);
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(byte[] buf, int len) {
        OutputStream out = null;
        try {
            if (mSocket == null) {
                mSocket = new Socket("10.2.1.12", 10002);
            }
            out = mSocket.getOutputStream();
            out.write(buf, 0, len);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
