package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

public class AlarmManagerSetup {

    public static void setAlarm(Context context, long timeInMillis, String title, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Check if the app has permission to schedule exact alarms
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                requestExactAlarmPermission(context);
                return;
            }
        }

        // Proceed to set the alarm
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Schedule the exact alarm
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    private static void requestExactAlarmPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
        context.startActivity(intent);
        Toast.makeText(context, "Permission to schedule exact alarms is required", Toast.LENGTH_SHORT).show();
    }
}
