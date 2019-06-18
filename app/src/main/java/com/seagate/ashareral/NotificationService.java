package com.seagate.ashareral;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.content.Context.ACTIVITY_SERVICE;

public class NotificationService extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        SharedPreferences preferences = context.getSharedPreferences(Utils.NOTIFICATION_KEY, Context.MODE_PRIVATE);

        FirebaseFirestore.getInstance().collection("notifications").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String timestamp = (String) document.get("timestamp");
                    String type = (String) document.get("type");
                    if (!preferences.contains(timestamp)) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(timestamp, timestamp);
                        editor.commit();
                        getData(type, timestamp);

                    }
                }

            }

        });
    }

    private void getData(String type, String timestamp) {
        String notificationTitle = "You have new " + type;
        FirebaseFirestore.getInstance().collection(type).document(timestamp).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                switch (type) {
                    case Utils.NEWS_KEY:
                        News news = task.getResult().toObject(News.class);
                        startNewsNotification(news);
                        break;
                    case Utils.EVENT_KEY:
                        String title= (String) task.getResult().get(Utils.EVENT_TITLE_KEY);
                        String date= (String) task.getResult().get(Utils.EVENT_DATE_KEY);
                        startEventNotification(date,title);

                }
            }
        });
    }

    private void startEventNotification(String timestamp,String title) {
        Intent intent=new Intent(context,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        intent.putExtra(Utils.EVENT_KEY,timestamp);
        intent.putExtra(Utils.NOTIFICATION_KEY,Utils.EVENT_KEY);

        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "RAL")
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle("You Have New Event !")
                .setContentText(title)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel(context);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(12345, builder.build());
    }

    private void startNewsNotification(News news) {

        Intent intent=new Intent(context,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        intent.putExtra(Utils.NEWS_KEY,news);
        intent.putExtra(Utils.NOTIFICATION_KEY,Utils.NEWS_KEY);

        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "RAL")
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle("You Have News")
                .setContentText(news.getTitle())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel(context);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(12345, builder.build());

    }



    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("RAL", "ral_chanel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static boolean isForeground(Context ctx){
        ActivityManager manager = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);

        List< ActivityManager.RunningTaskInfo > runningTaskInfo = manager.getRunningTasks(1);

        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        if(componentInfo.getPackageName().equals(ctx.getPackageName())) {
            return true;
        }
        return false;
    }
}
