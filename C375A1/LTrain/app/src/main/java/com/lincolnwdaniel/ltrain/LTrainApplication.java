package com.lincolnwdaniel.ltrain;

import android.app.Application;

import com.lincolnwdaniel.ltrain.utils.LToast;

import java.util.Random;

/**
 * Created by lwdthe1 on 9/8/2015.
 */
public class LTrainApplication extends Application {
    public static Random randGen;
    @Override
    public void onCreate(){
        super.onCreate();
        randGen = new Random();
        //LToast.showL(getApplicationContext(), "All Aboard the LTrain!");
    }

    public static Random getRandGen(){
        return randGen;
    }
}
