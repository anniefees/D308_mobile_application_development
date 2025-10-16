package com.annief.tracker.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlertHelper {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    /**
     * Schedule an alert for a trip start date
     */
    public static void scheduleStartAlert(Context context, long tripId, String tripName, String dateStr) {
        scheduleAlert(context, (int)(tripId * 2), tripName, dateStr, tripName + " starts today!");
    }

    /**
     * Schedule an alert for a trip end date
     */
    public static void scheduleEndAlert(Context context, long tripId, String tripName, String dateStr) {
        scheduleAlert(context, (int)(tripId * 2 + 1), tripName, dateStr, tripName + " ends today!");
    }

    /**
     * Schedule an alert for an event
     */
    public static void scheduleEventAlert(Context context, long eventId, String eventTitle, String dateStr) {
        scheduleAlert(context, (int)(eventId + 10000), eventTitle, dateStr, "Event: " + eventTitle);
    }

    /**
     * Generic method to schedule an alert
     */
    private static void scheduleAlert(Context context, int requestCode, String title, String dateStr, String message) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DATE_FORMAT.parse(dateStr));
            calendar.set(Calendar.HOUR_OF_DAY, 8); // Set to 8 AM
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            // Only schedule if the date is in the future
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                Toast.makeText(context, "Cannot set alert for past dates", Toast.LENGTH_SHORT).show();
                return;
            }

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlertReceiver.class);
            intent.putExtra("title", title);
            intent.putExtra("message", message);
            intent.putExtra("notificationId", requestCode);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );
                Toast.makeText(context, "Alert set for " + dateStr + " at 8:00 AM", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error setting alert: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Cancel an alert
     */
    public static void cancelAlert(Context context, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}