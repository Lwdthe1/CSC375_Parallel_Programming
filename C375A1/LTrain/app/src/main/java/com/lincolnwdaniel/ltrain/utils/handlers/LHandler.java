package com.lincolnwdaniel.ltrain.utils.handlers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.lincolnwdaniel.ltrain.LTrainApplication;
import com.lincolnwdaniel.ltrain.utils.LToast;

/**
 * Created by lwdthe1 on 9/18/2015.
 */
public abstract class LHandler extends Handler {
    public static long id = 0;
    private String name;
    private int totalMessagesAndPostsPassed;
    private Context context;

    public static final int MESSAGE_ACTION_STARTED = 100;
    public static final int MESSAGE_ACTION_SUCCESS = 200;
    public static final int MESSAGE_ACTION_FAIL = 400;

    protected LHandler() {
    }

    public LHandler(Context context, String name){
        this.context = context;
        this.name = name;
        id++;
    }

    public static long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalMessagesAndPostsPassed() {
        return totalMessagesAndPostsPassed;
    }

    public Context getContext() {
        return context;
    }


    public abstract void handleTheMessage(Message msg);

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        handleTheMessage(msg);
    }

}
