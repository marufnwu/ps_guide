package com.psguide.uttam.Notification.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.psguide.uttam.MainActivity;
import com.psguide.uttam.MessageActivity;
import com.psguide.uttam.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class NotificationUtil {


    private static final String CHANNEL_ID = "psGuide";
    private static final String CHANNEL_NAME = "psGuide";
    private static int NOTIFICATION_ID = 20001;
    private Context context;
    private Notification notification;

    public NotificationUtil(Context context) {
        this.context = context;
    }

    @SuppressLint("WrongConstant")
    public void displayNotification(NotificationData notificationData, Intent resultIntent1) {

        String tittle = notificationData.title;
        String message = notificationData.message;
        String iconUrl = notificationData.iconUrl;
        Bitmap iconBitmap = null;

        PendingIntent resultPendingIntent;

        Log.d("intentSelect", new Gson().toJson(notificationData));

        if (notificationData.action!=null && notificationData.action.equals("openUrl")){

            Log.d("intentSelect", "Url");
            Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
            notificationIntent.setData(Uri.parse(notificationData.actionDestination));

            resultPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        }else if (notificationData.action!=null && notificationData.action.equals("openActivity")
                            && notificationData.id !=null){

            Log.d("intentSelect", "Message");

            Intent resultIntent = new Intent(context, MessageActivity.class);
            resultIntent.putExtra("id", notificationData.id);
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            // Get the PendingIntent containing the entire back stack
             resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }else {
            Log.d("intentSelect", "Mainactivity");

            Intent resultIntent = new Intent(context, MainActivity.class);
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            // Get the PendingIntent containing the entire back stack
            resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }


        if(iconUrl!=null){
            iconBitmap = getBitmapFromURL(iconUrl);
        }
        int icon = R.mipmap.ic_launcher_foreground;

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context, CHANNEL_ID);

        mBuilder.setContentIntent(resultPendingIntent).setOngoing(true);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (iconBitmap == null){

            Log.d("NotificationLog", "iconBitmap null");

            //show without image
          notification =   mBuilder.setSmallIcon(icon)
                    .setContentTitle(tittle)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                    .build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;

        }else {

            Log.d("NotificationLog", "iconBitmap not null");


            notification = mBuilder.setSmallIcon(icon)
                    .setTicker(tittle)
                    .setWhen(0)
                    .setContentTitle(tittle)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                    .setBigContentTitle(tittle)
                    .bigPicture(iconBitmap))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setLargeIcon(iconBitmap)

                    .build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_NAME;
            String description = "Ps Guide Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setSound(soundUri, audioAttributes);


            notificationManager.createNotificationChannel(channel);
        }else {
            mBuilder.setSound(soundUri);
        }
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        NOTIFICATION_ID = m;
        notificationManager.notify(NOTIFICATION_ID, notification);

    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

    }
}
