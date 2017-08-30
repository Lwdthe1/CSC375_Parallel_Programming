package com.lincolnwdaniel.ltrain.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.lincolnwdaniel.ltrain.objects.TouchObject;


/**
 * Created by lwdthe1 on 1/9/2015.
 */
public class LAlert {
    public static final int INCOMPLETE_FORM = 1;
    protected static final int WRONG_USERNAME = 2;
    protected static final int WRONG_PASSWORD = 3;
    protected static final int INVALID_EMAIL = 4;
    public static final int NO_SUBJECT_SELECTED = 5;
    public static final int QUESTION_TITLE_TOO_LONG = 6;
    public static final int GREETING_TEXT_TOO_LONG = 7;

    public static void noInternet(final Context x) {
        //initiate a AlertDialog.Builder in this context to tell the user to turn on internet
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(x);
        //set the message of the AlertDialog
        alertDialogBuilder.setTitle("Connection Error");
        alertDialogBuilder.setMessage("Make sure you are connected to the internet and try again.")
                //set the AlertDialog's positive button's text and its onclicklistener
                .setPositiveButton("Internet Settings", new DialogInterface.OnClickListener() {
                    //handle the click event of this positive button
                    public void onClick(DialogInterface dialog, int id) {
                        //send the user to the phone's network settings page
                        Intent openNetworkSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        //close the dialog box
                        dialog.dismiss();
                        //start the intent
                        x.startActivity(openNetworkSettingsIntent);
                    }
                }).setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        //create and show the alert dialog
        alertDialogBuilder.create().show();
    }


    public static void noInternetExitDefault(final Context x) {
        //initiate a AlertDialog.Builder in this context to tell the user to turn on internet
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(x);
        //set the message of the AlertDialog
        alertDialogBuilder.setTitle("Connection Error");
        alertDialogBuilder.setMessage("Make sure you are connected to the internet and try again.")
                //set the AlertDialog's positive button's text and its onclicklistener
                .setPositiveButton("Internet Settings", new DialogInterface.OnClickListener() {
                    //handle the click event of this positive button
                    public void onClick(DialogInterface dialog, int id) {
                        //send the user to the phone's network settings page
                        Intent openNetworkSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        //close the dialog box
                        dialog.dismiss();
                        //start the intent
                        x.startActivity(openNetworkSettingsIntent);
                    }
                }).setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
                dialog.dismiss();
            }
        });
        //create and show the alert dialog
        alertDialogBuilder.create().show();
    }


    public static void dataRetrievalError(final Context x) {
        AlertDialog.Builder builder = new AlertDialog.Builder(x);
        builder.setTitle("Data Retrieval Error");
        builder.setMessage("We had trouble retrieving data from the internet.");
        builder.setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void noActionErrorAlert(final Context x, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(x);
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static AlertDialog buildChoicesAlertDialog(Context context, String title, int choices, DialogInterface.OnClickListener choiceListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setItems(choices, choiceListener);

        return builder.create();
    }

    /**
     * ***********************ACTION CONFIRMATION DIALOGS*********************************************
     */
}
