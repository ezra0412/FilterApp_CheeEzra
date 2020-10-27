package com.example.filterapp.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.filterapp.MainActivity;
import com.example.filterapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        sendRegistrationToServer(refreshedToken);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");


        Intent intent = new Intent(this, MainActivity.class);

        if (remoteMessage.getData().get("from123") != null) {

            switch (remoteMessage.getData().get("from123")) {
                case "1":
                case "2":
                case "3":
                case "11":
                    intent.putExtra("from123", remoteMessage.getData().get("from123"));
                    intent.putExtra("staffID", remoteMessage.getData().get("staffID"));
                    break;

                case "4":
                case "5":
                    intent.putExtra("from123", remoteMessage.getData().get("from123"));
                    intent.putExtra("customerID", remoteMessage.getData().get("customerID"));
                    break;

                case "6":
                    intent.putExtra("from123", remoteMessage.getData().get("from123"));
                    intent.putExtra("filterID", remoteMessage.getData().get("filterID"));
                    break;

                case "7":
                    intent.putExtra("from123", remoteMessage.getData().get("from123"));
                    intent.putExtra("filterID", remoteMessage.getData().get("filterID"));
                    intent.putExtra("serviceID", remoteMessage.getData().get("serviceID"));
                    break;

                default:
                    intent.putExtra("from123", remoteMessage.getData().get("from123"));
                    break;
            }
        }


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "Ashita")
                        .setContentTitle(title)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(body));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        int id = (int) System.currentTimeMillis();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Ashita", "Admin Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id, notificationBuilder.build());
    }


    private void sendRegistrationToServer(String token) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DocumentReference updateToken = db.collection("staffDetails").document(mAuth.getCurrentUser().getUid());
        updateToken.update("token", token);
    }


}
