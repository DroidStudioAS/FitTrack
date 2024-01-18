package com.aa.fittracker.network;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.aa.fittracker.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingServ extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle the incoming message and show a notification
        if (remoteMessage.getData().size() > 0) {
            showNotification(remoteMessage.getData().get("message"));
        }
    }

    private void showNotification(String message) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "channel_id")
                        .setSmallIcon(R.drawable.centerpiece)
                        .setContentTitle("My App")
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // NotificationId allows you to update the notification later on.
        int notificationId = 1;
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
