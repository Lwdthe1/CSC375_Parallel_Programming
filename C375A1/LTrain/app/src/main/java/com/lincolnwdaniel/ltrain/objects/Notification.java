package com.lincolnwdaniel.ltrain.objects;


import com.lincolnwdaniel.ltrain.utils.LTime;

import java.util.Random;

/**
 * Created by Lwdthe1 on 6/28/2015.
 */
public class Notification extends TouchObject{

    public static final int LIFE_SPAN_DAYS = 30;
    private final String TAG = getClass().getSimpleName();
    private static Random randGen = new Random();

    private static final int CODE_BAD_NOTIFICATION = -1;

    public Random getRandGen() {
        return randGen;
    }

    public boolean wasViewed() {
        return this.status == Status.VIEWED;
    }

    public boolean wasActedOn(){
        return this.status == Status.ACTED_ON;
    }
    public boolean wasDismissed(){
        return this.status == Status.DISMISSED;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class Status {
        public static final int VIEWED = 10;
        public static final int NOT_VIEWED = 21;
        public static final int ACTED_ON = 31;
        public static final int DISMISSED = 41;
    }
    public static class Code {
        public static final int SYSTEM_GENERAL = 110;
        public static final int SYSTEM_ERROR = 120;

        public static final int MESSENGER_GENERAL = 210;
        public static final int MESSENGER_GREETING_SENT = 220;
        public static final int MESSENGER_GREETING_NOT_SENT = 230;
        public static final int MESSENGER_NOT_SENDING_SPAMMY_GREETING = 231;
        public static final int MESSENGER_GREETING_SENDING_SOON = 240;
        public static final int EMAIL_CREDENTIALS_FAILED = 250;

    }

    public class Action {
        public static final int OPEN_SETTINGS = 110;
        public static final int OPEN_GREETING = 210;
    }

    private String text, title;
    private int greetingId, code, action, status;
    private String createdAt;

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public int getAction() {
        return action;
    }

    public int getGreetingId() {
        return greetingId;
    }

    public int getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isGood(){ return this.id != CODE_BAD_NOTIFICATION; }

    public long getCreatedAtAgo(LTime.ETime timeUnit) {
        if(timeUnit == null) timeUnit = LTime.ETime.AGO_HOUR;
        return LTime.timeAgo(timeUnit, getCreatedAt());
    }
}
