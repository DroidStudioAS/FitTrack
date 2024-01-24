package com.aa.fittracker.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.aa.fittracker.MainActivity;
import com.aa.fittracker.R;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "FitTrackerChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    public void showNotification(Context context) {
        createNotificationChannel(context);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create an explicit intent for an activity in your app
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.centerpiece)
                    .setContentTitle("FitTracker")
                    .setContentText("Thank you for using FitTracker!")
                    .setStyle(new Notification.BigTextStyle().bigText("Thank you for using FitTracker! If You Enjoy The App, Please Leave Us A Review On Google Play"))
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true);
        }

        // For devices running Android 8.0 (Oreo) and above, use the NotificationCompat builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        Notification notification = builder.build();

        // Notify
        notificationManager.notify(2, notification);
    }

    public boolean notChanelCreated = false;
    public void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "FitTracker Channel";
            String description = "Channel for FitTracker Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            if(!notChanelCreated) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);

                // Register the channel with the system
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
                //set the bool
                notChanelCreated=true;
            }
        }
    }
    public void sendCustomNotification(Context context,String Title, String Content, String action){
        createNotificationChannel(context);
        //grab the notification manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Replace "https://www.paypal.com" with the PayPal URL you want to open
        String paypalUrl = "https://paypal.me/softwaredevelopmentA?country.x=RS&locale.x=en_US";
        // Create an implicit intent to open the PayPal URL
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paypalUrl));
        // Create a PendingIntent
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            builder = new Notification.Builder(context,CHANNEL_ID)
                    .setSmallIcon(R.drawable.centerpiece)
                    .setContentTitle(Title)
                    .setContentText(Content)
                    .setStyle(new Notification.BigTextStyle().bigText(Content))
                    .addAction(R.drawable.icon_action_add, action,contentIntent);
        }
        // For devices running Android 8.0 (Oreo) and above, use the NotificationCompat builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }


        Notification notification = builder.build();
        notificationManager.notify(1,notification);

    }
}
