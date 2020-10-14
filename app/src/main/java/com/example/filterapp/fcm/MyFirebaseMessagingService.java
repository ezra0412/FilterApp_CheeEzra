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


       if (remoteMessage.getData().get("from123").equals("1")){
           intent.putExtra("from123",remoteMessage.getData().get("from123"));
           intent.putExtra("staffID",remoteMessage.getData().get("staffID"));
       }

       else if (remoteMessage.getData().get("from123").equals("2")){
           intent.putExtra("from123",remoteMessage.getData().get("from123"));
           intent.putExtra("staffID",remoteMessage.getData().get("staffID"));
        }

       else if (remoteMessage.getData().get("from123").equals("3")){
           intent.putExtra("from123",remoteMessage.getData().get("from123"));
           intent.putExtra("staffID",remoteMessage.getData().get("staffID"));
       }

       else if (remoteMessage.getData().get("from123").equals("11")){
           intent.putExtra("from123",remoteMessage.getData().get("from123"));
           intent.putExtra("staffID",remoteMessage.getData().get("staffID"));
       }

       else if (remoteMessage.getData().get("from123").equals("4")){
           intent.putExtra("from123",remoteMessage.getData().get("from123"));
           intent.putExtra("staffID",remoteMessage.getData().get("customerID"));

       }

       else if (remoteMessage.getData().get("from123").equals("5")){
           intent.putExtra("from123",remoteMessage.getData().get("from123"));
           intent.putExtra("staffID",remoteMessage.getData().get("customerID"));

       }

       else if (remoteMessage.getData().get("from123").equals("6")){
           intent.putExtra("from123",remoteMessage.getData().get("from123"));
           intent.putExtra("staffID",remoteMessage.getData().get("filterID"));

       }

       else if (remoteMessage.getData().get("from123").equals("7")){
           intent.putExtra("from123",remoteMessage.getData().get("from123"));
           intent.putExtra("staffID",remoteMessage.getData().get("filterID"));
           intent.putExtra("staffID",remoteMessage.getData().get("serviceID"));

       }else
           intent.putExtra("from123",remoteMessage.getData().get("from123"));


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "Ashita")
                        .setContentTitle(title)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(body));


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
