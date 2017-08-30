package com.lincolnwdaniel.ltrain.services;

import com.lincolnwdaniel.ltrain.objects.TouchObject;

import java.util.HashMap;

/**
 * Created by Lwdthe1 on 6/28/2015.
 */
public class User extends TouchObject {
    public final String TAG = getClass().getSimpleName();

    public static final String KEY_PHONE_NUMBER = "phone_number";
    public static final String OWNER_OPEN_COUNT = "ownerOpenCount";

    protected String phoneNumber;
    protected String name;
    protected String email;

    private String emailPassword;
    /*private TouchEmailer emailer;
    private TouchEmailReader emailReader;*/
    private static int ownerOpenCount = -1;

    private int greetingSleepStart24Hr = -1;
    private int greetingSleepEnd24Hr = -1;

    public User(String phoneNumber){
        super();
        this.phoneNumber = phoneNumber;
    }



    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setName(String name) { this.name = name; }

    public String getPhone() { return this.phoneNumber; }

    public String getName() {
        if(name == null) this.name = "";
        return name;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }


    /*public void setEmailer(TouchEmailer emailer) {
        this.emailer = emailer;
    }

    public void setEmailReader(TouchEmailReader emailReader) {
        this.emailReader = emailReader;
    }

    public TouchEmailer getEmailer() {
        if(this.emailer == null){
            this.emailer = new TouchEmailer(getEmail());
        }
        return emailer;
    }

    public TouchEmailReader getEmailReader() {
        if(this.emailer == null){
            this.emailer = new TouchEmailer(getEmail());
        }
        return emailReader;
    }*/


}
