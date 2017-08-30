package com.lincolnwdaniel.ltrain.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.lincolnwdaniel.ltrain.objects.TouchObject;
import com.lincolnwdaniel.ltrain.utils.LTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln W Daniel on 7/7/2015.
 * <p/>
 * This class is the service that will be run in the background periodically.
 * <p/>
 * This will go through the user's TouchObject table in the database.
 * For each greeting, calculate the time, silence,
 * since the user has communicated with the greetee.
 * If the silence is greater than or equal to the TouchObject's wait time,
 * send the greeting to the greetee.
 * <p/>
 * At the end of this service,
 * the alarm broadcast receiver that invoked it will be rescheduled for it to run again later.
 */
public class StationService extends IntentService {
    public static String TAG_S = "MessengerAlarmServiceST";
    public String TAG = getClass().getSimpleName();
    Context mContext;
    Intent smsObserverServiceIntent;

    List<TouchObject> mDbTouchObjects;
    List<TouchObject> mDueTouchObjects;
    private boolean mIsGone = false;
    Thread mServiceTimeOutThread;

    //how long to wait until service times out
    public static final int SERVICE_TIMEOUT_MILLISEC = LTime.MILLISEC_MINUTE*10;

    public StationService() {
        super("StationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mContext = this;
        TouchNotification.Messenger.startForegroundService(this);
        startSmsObserverService();

        mDueTouchObjects = new ArrayList<>();
        //start the messenger timeout counter
        runMessenger();
    }

    private void runMessenger() {
        try {
            if(!mIsGone) startTimeoutThread();

        } catch (ServiceTimeOutException e) {
            //e.printStackTrace();
        } finally {
            if(mServiceTimeOutThread.isAlive()) {
                mServiceTimeOutThread.interrupt();
            }
            mIsGone = true;
            stopForeground(true);
            return;
        }
    }

    private void startTimeoutThread() throws ServiceTimeOutException {
        mServiceTimeOutThread = new Thread() {
            public void run() {
                try {
                    sleep(SERVICE_TIMEOUT_MILLISEC);
                } catch (InterruptedException e) {
                } finally {
                    if (!mIsGone) {
                        String msg = "Found timeout. Messenger service has timeout at " + LTime.getDateNow();
                        Log.v(TAG, msg);

                        stopForeground(true);
                        try {
                            throw new ServiceTimeOutException(msg);
                        } catch (ServiceTimeOutException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.v(TAG, "Found gone service. Service is gone! Messenger ending at " + LTime.getDateNow());
                    }
                }
            }
        };
        mServiceTimeOutThread.start();
    }

    private class ServiceTimeOutException extends Throwable{
        public ServiceTimeOutException(String message){
            super(message);
        }
    }

    private void startSmsObserverService() {
        if(smsObserverServiceIntent == null) {
            smsObserverServiceIntent = new Intent(mContext, StationService.class);
            mContext.startService(smsObserverServiceIntent);
        }
    }
}