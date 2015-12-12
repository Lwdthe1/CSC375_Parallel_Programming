package com.lincolnwdaniel.ltrain.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.lincolnwdaniel.ltrain.activities.StationActivity;
import com.lincolnwdaniel.ltrain.objects.TouchObject;

/**
 * Created by Lwdthe1 on 6/28/2015.
 */
public class LaunchIntent {


    public static final String ACTION_OPEN_NOTIFICATION_GREETING = "open_notification_greeting";
    public static final String ACTION_OPEN_GREETING = "open_greeting_from_greetings_activity";

    public static boolean start(int launch, Activity activity, Context context){
        Intent intent = null;
        if(context == null) context = activity.getApplicationContext();

        switch(launch){
            case AppConstants.ACTIVITY_GREETINGS:
                intent = new Intent(context, StationActivity.class);
                break;
            case AppConstants.ACTIVITY_EMAIL_CONTACT_FROM_ADD_CONTACT_EMAIL:
                intent = new Intent(context, StationActivity.class);
                //make the login activity a new task
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //clear the old task, the MainActivity, from the history stack
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
        }

        if(intent!=null) {
            context.startActivity(intent);
            return true;
        } else return false;
    }


    public static void startSettingsActivity(Context context) {
        /*Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);*/
    }

    public static void openUser(Context context, String greeteeId) {
    }

    public static void startEditGreeting(Context context, int greetingId, String action) {
        Intent intent = new Intent(context, StationActivity.class);
        intent.putExtra(TouchObject.KEY_ID, greetingId);
        intent.setAction(action);
        context.startActivity(intent);
    }

}
