package com.seagate.ashareral;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification()!=null){
            Log.d("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onDeletedMessages() {

    }
}
