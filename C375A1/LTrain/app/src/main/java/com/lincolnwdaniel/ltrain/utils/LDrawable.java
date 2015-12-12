package com.lincolnwdaniel.ltrain.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by lwdthe1 on 9/3/2015.
 */
public class LDrawable {
    public static Drawable getDrawable(Context context, String drawableName){
        Resources r = context.getResources();
        String pN = context.getPackageName();
        if(drawableName == null || drawableName.isEmpty()
                || !exists(context, drawableName)) drawableName = "ic_launcher";
        return r.getDrawable(r.getIdentifier(drawableName, "drawable", pN));
    }

    public static boolean exists(Context context, String drawableMame){
        int checkExistence = context.getResources().getIdentifier(drawableMame, "drawable", context.getPackageName());

        boolean result;
        if ( checkExistence != 0 ) {  // the resouce exists...
            result = true;
        }
        else {  // checkExistence == 0  // the resouce does NOT exist!!
            result = false;
        }
        return result;
    }
}
