package com.example.yuichi_oba.ecclesia.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by yuichi_develop on 2017/11/06.
 */

public class AlarmReceiver extends BroadcastReceiver {

  public static String NOTIFICATION_ID = "notificationId";
  public static String NOTIFICATION_CONTENT = "content";

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.d("call", "call AlramReceiver.onReceive()");
    NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    int id = intent.getIntExtra(NOTIFICATION_ID, 0);
    String content = intent.getStringExtra(NOTIFICATION_CONTENT);
    notificationManager.notify(id, buildNotification(context, content));
  }

  private Notification buildNotification(Context context, String content) {
    Notification.Builder builder = new Notification.Builder(context);
    builder.setContentTitle("Notification!!")
        .setContentText(content)
        .setVibrate(new long[]{100, 0, 100, 0, 100, 0})
        .setPriority(Notification.PRIORITY_HIGH)
        .setSmallIcon(android.R.drawable.sym_def_app_icon);


    return builder.build();
  }
}
