package com.psguide.uttam.Notification.Service;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.psguide.uttam.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public String TITTLE ="title";
    public String  MESSAGE ="message";
    public String  ICONURI = "iconUrl";
    public String action = "action";
    public String actionDestination = "actionDestination";
    public String notType = "notType";
    public String TOPIC_ALL_USER = "ALL_USER";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.d("newTokenGenerate", "Success");


        //sendTokenToServer(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {


        super.onMessageReceived(remoteMessage);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Map<String, String> data = remoteMessage.getData();
            handleData(data);

        } else if (remoteMessage.getNotification() != null) {

            handleNotification(remoteMessage.getNotification());
        }// Check if message contains a notification payload.

    }

    private void handleNotification(RemoteMessage.Notification notification) {
        NotificationData notificationData = new NotificationData();

        notificationData.title = notification.getTitle();
        notificationData.message = notification.getBody();

        Intent resultIntent  = new Intent(getApplicationContext(), MainActivity.class);

        NotificationUtil notificationUtil = new NotificationUtil(getApplicationContext());

        notificationUtil.displayNotification(notificationData, resultIntent);
    }

    private void handleData(Map<String, String> data) {
        NotificationData notificationData = new NotificationData();

        notificationData.title = data.get(TITTLE);
        notificationData.message = data.get(MESSAGE);
        notificationData.iconUrl = data.get(ICONURI);
        notificationData.id = data.get("id");
        notificationData.action = data.get(action);
        notificationData.actionDestination = data.get(actionDestination);

        Intent resultIntent  = new Intent(getApplicationContext(), MainActivity.class);

        NotificationUtil notificationUtil = new NotificationUtil(getApplicationContext());

        notificationUtil.displayNotification(notificationData, resultIntent);



    }
}
