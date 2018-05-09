package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.MainActivity;
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.R;

public class MyFirebaseMessagingService  extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "CHANNEL_1";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            //To Do
        }

        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int requestCode = 0;

        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        int notificationId = 0;
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
