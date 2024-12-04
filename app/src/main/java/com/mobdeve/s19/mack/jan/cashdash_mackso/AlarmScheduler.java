package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmScheduler {

    public static void scheduleAlarm(Context context, int id, String title, String dueDate, String category, String amount) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Check if exact alarm permission is granted
        if (alarmManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            // Request permission to schedule exact alarms
            requestExactAlarmPermission(context);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(dateFormat.parse(dueDate));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("AlarmScheduler", "Invalid dueDate format: " + dueDate);
            return;
        }

        // Log to confirm the parsed time
        Log.d("AlarmScheduler", "Alarm scheduled for: " + calendar.getTime().toString());

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("dueDate", dueDate);
        intent.putExtra("category", category);
        intent.putExtra("amount", amount);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            // Log the time to verify the alarm time
            Log.d("AlarmScheduler", "Scheduling alarm at: " + calendar.getTimeInMillis());
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public static void cancelAlarm(Context context, int id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                id,
                intent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private static void requestExactAlarmPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
        context.startActivity(intent);
    }
}
