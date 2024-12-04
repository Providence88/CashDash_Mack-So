package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;

public class NotificationUtils {
    private static final String CHANNEL_ID = "cashdash_channel";
    private static final String CHANNEL_NAME = "CashDash Notifications";
    private static final String TAG = "NotificationUtils";


    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for bills and expenses");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void sendNotification(Context context, String title, String amount, String category, String dueDate) {
        Log.d(TAG, "Sending notification with title: " + title);  // Log message before sending the notification

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText("Amount: ₱" + amount + "\nCategory: " + category + "\nDue: " + dueDate)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Amount: ₱" + amount + "\nCategory: " + category + "\nDue: " + dueDate))
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify((int) System.currentTimeMillis(), builder.build());
            Log.d(TAG, "Notification sent successfully.");  // Log message after sending the notification
        } else {
            Log.e(TAG, "Notification manager is null");  // Log an error if Notification Manager is null
        }
    }

}
