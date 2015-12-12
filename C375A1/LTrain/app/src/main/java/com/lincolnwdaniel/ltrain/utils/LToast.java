package com.lincolnwdaniel.ltrain.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lwdthe1 on 1/8/2015.
 * this class is to be used throughout the application to show Toasts
 */
public class LToast {

    /**
     * @param x is the conteXt by which the toast will be shown.
     * @param msg is the toast message to be shown.
     * @param length is the desired length of the toast. Either long or short.
     * because this is a public static class method, it can be accessed across the application
     * by use of the class name. (i.g. AlertToast.show(ActivityName.this, "NO! That's Wrong!", long)
     */
    public static void show(Context x, String msg, String length){
        if(length.equalsIgnoreCase("long")){
            Toast.makeText(x.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(x.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showL(Context x, String msg){
        Toast.makeText(x.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showS(Context x, String msg){
        Toast.makeText(x.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
