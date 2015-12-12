package com.lincolnwdaniel.ltrain.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


import com.lincolnwdaniel.ltrain.R;
import com.lincolnwdaniel.ltrain.activities.StationActivity;
import com.lincolnwdaniel.ltrain.objects.TouchObject;

import java.util.ArrayList;

/**
 * Created by Lwdthe1 on 7/14/2015.
 */
public class TouchNotification extends TouchObject {
    public static final int ID_GREETING_SENT = 1;
    public static final int ID_GREETING_NOT_SENT = 2;
    public static final int ID_GREETING_SENDING_SOON = 3;
    public static final int ID_MESSENGER_STICKY = 45806;

    public static final String KEY_ACTION = "notificationAction";
    public static final String ACTION_GREETING_EDIT = "editGreetingAction";
    public static final String ACTION_GREETING_DEACTIVATE = "deactivateGreetingAction";
    public static final String ACTION_GREETING_SNOOZE = "snoozeGreetingAction";
    public static final String KEY_NOTIFICATION_ID = "notificationId";
    public static final String ACTION_OPEN_GREETINGS_ACTIVITY = "openGreetingsActivity";

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private ArrayList<String> events;
    private String bigContentTitle;

    private int number = -1;

    public TouchNotification(Context context) {
        this.mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder =new NotificationCompat.Builder(mContext);
        events = new ArrayList<>();
    }

    public void setLights(int color) {
        mBuilder.setLights(color, 3000, 3000);//LED
    }

    public void setVibrate() {
        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});//Vibration
    }

    public NotificationCompat.Builder getBuilder(){
        return mBuilder;
    }
    public NotificationManager getManager(){
        return mNotificationManager;
    }
    public String getTitle() {
        return title;
    }

    public TouchNotification setTitle(String title) {
        this.title = title;
        return this;
    }

    public TouchNotification setBigContentTitle(String title){
        this.bigContentTitle = title;
        return this;
    }

    public TouchNotification setNumber(int number){
        this.number = number;
        return this;
    }

    private String title;

    public String getText() {
        return text;
    }

    public TouchNotification setText(String text) {
        this.text = text;
        return this;
    }

    private String text;

    public int getSmallIconRes() {
        return smallIconRes;
    }

    public TouchNotification setSmallIconRes(int smallIconRes) {
        this.smallIconRes = smallIconRes;
        return this;
    }

    private int smallIconRes;

    public void addEvent(String event){
        events.add(event);
    }

    public void send(){
        mBuilder
                .setSmallIcon(smallIconRes)
                .setContentTitle(title)
                .setContentText(text);
        if(number>-1) mBuilder.setNumber(number);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle(bigContentTitle);
        // Moves events into the expanded layout
        inboxStyle.addLine(text);
        for (String event: events) {
            inboxStyle.addLine(event);
        }
        // Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);


        // the id allows you to update the notification later on.
        mNotificationManager.notify( (int) get(KEY_NOTIFICATION_ID), mBuilder.build());
    }

    public void setContentOpenIntentToNotifications() {
        //Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(mContext, StationActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                mContext, 0, resultIntent, 0);
        mBuilder.setContentIntent(resultPendingIntent);
        setAutoCancel(true);
    }

    public void setAutoCancel(boolean on) {
        mBuilder.setAutoCancel(on);
    }

    public static void cancelNotification(Context context, int notificationId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(ns);
        notificationManager.cancel(notificationId);
    }

    public static class Messenger {
        public static void startForegroundService(Service context) {
            Intent notificationIntent = new Intent(context, StationActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle("Keeping You inTouch")
                    .setContentText("Live life. inTouch is working its magic")
                            //.setSubText("to keep you in touch with loved ones.")
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .build();
            context.startForeground(ID_MESSENGER_STICKY, notification);
        }
    }
}
