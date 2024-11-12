package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.sendNotification(title, message, context);
    }
}
