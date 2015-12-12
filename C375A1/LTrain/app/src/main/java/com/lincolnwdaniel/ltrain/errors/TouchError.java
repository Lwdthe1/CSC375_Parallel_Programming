package com.lincolnwdaniel.intouch.errors;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Lwdthe1 on 7/4/2015.
 */
public class TouchError {
    private int code;
    private String title = "Error title";
    private String text = "Error text";
    private Activity activity;
    private Context context;

    public TouchError (Context context){
        this.context = context;
    }

    public TouchError setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public TouchError setText(String text) {
       this.text = text;
        return this;
    }

    public TouchError setCode(int code) {
        this.code = code;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Context getContext() {
        return context;
    }
}
