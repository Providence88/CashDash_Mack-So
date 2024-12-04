package com.mobdeve.s19.mack.jan.cashdash_mackso;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "CashDashChannel"; // Use the same channel ID you created in MainActivity

    @Override
    public void onReceive(Context context, Intent intent) {
        // Extract the data from the intent
        String title = intent.getStringExtra("title");
        String amount = intent.getStringExtra("amount");
        String category = intent.getStringExtra("category");
        String dueDate = intent.getStringExtra("dueDate");

        // Create notification manager and builder
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // If the device is running on Android Oreo or higher, create the notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CashDash Notifications";
            String description = "Notifications for bills and expenses";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;  // Make sure importance is set
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Build the content text with title, amount, category, and due date
        String contentText = "Reminder: " + title + "\n" +
                "Amount: " + amount + "\n" +
                "Category: " + category + "\n" +
                "Due Date: " + dueDate;

        // Build the notification using the default Android notification icon
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_notification_overlay) // Default Android notification icon
                .setContentTitle("Reminder: " + title) // Title of the notification
                .setContentText(contentText)  // Content with all necessary details
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText)) // Use BigTextStyle for longer content
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // Set notification priority
                .setAutoCancel(true);  // Auto dismiss the notification when clicked

        // Show the notification
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build()); // Notify with a unique ID (here we use 1, you can adjust as needed)
        }
    }
}
