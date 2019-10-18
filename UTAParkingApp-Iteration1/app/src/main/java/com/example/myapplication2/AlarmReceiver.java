package com.example.myapplication2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.DrawableRes;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int notificationId = intent.getIntExtra("NotificationId",0);
        String message = intent.getStringExtra("todo");


        //When notification tapped, Called Remainder class
        Intent mainIntent = new Intent(context, Reminder.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,mainIntent,0);


        NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        //Prepare notification


        Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("It's time")
            .setContentText(message)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
                    ;


        //Notify

        myNotificationManager.notify(notificationId, builder.build());

    }
}

