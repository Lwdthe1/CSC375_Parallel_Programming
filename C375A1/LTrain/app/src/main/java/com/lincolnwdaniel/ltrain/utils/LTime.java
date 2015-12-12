package com.lincolnwdaniel.ltrain.utils;

import android.text.format.DateUtils;


import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import static android.text.format.DateUtils.getRelativeTimeSpanString;

/**
 * Created by Lwdthe1 on 7/9/2015.
 */
public class LTime {
    public static final int MILLISEC_SECOND = 1000;
    public static final int MILLISEC_MINUTE = 60000;
    public static final int MILLISEC_MINUTE_HALF = 30000;
    public static final int MILLISEC_HOUR = 3600000;
    public static final int MILLISEC_HOUR_HALF = 1800000;
    public static final int MILLISEC_DAY = 86400000;
    public static final int MILLISEC_DAY_HALF = 43200000;
    public static final double MILLISEC_MONTH = 2628000000.0;
    public static final double MILLISEC_YEAR = 31536000000.0;
    public static final int MIN_HOUR = 60;// there are 60 minutes in an hour
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String INVALID_DATE = "0";

    public static int compare(){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);

            String str1 = "12/10/2013";
            Date date1 = formatter.parse(str1);

            String str2 = "13/10/2013";
            Date date2 = formatter.parse(str2);

            if (date1.compareTo(date2)<0)
            {
                System.out.println("date2 is Greater than my date1");
            }
            return date1.compareTo(date2);

        }catch (ParseException e1){
            e1.printStackTrace();
            return -2;
        }
    }

    /**
     *
     * @param type the time units to be returned
     * @param timeY4M2D2H2M2S2 the past time. example: 2015-07-09 18:48:13
     * @return Time ago between the time now and the provided time
     */
    public static long timeAgo(ETime type, String timeY4M2D2H2M2S2) {
        DateFormat format = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        Date past = null;
        try {
            past = format.parse(timeY4M2D2H2M2S2);
        } catch (ParseException e) {}
        Date now = new Date();

        if(past!=null) {
            switch (type) {
                case AGO_MILLISEC:
                    return TimeUnit.MILLISECONDS.toMillis(now.getTime() - past.getTime());
                case AGO_MINUTE:
                    return TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
                case AGO_HOUR:
                    return TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
                case AGO_DAY:
                    return TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
                default:
                    return -1;
            }
        } else return -1;
    }

    public static String toRelativeTimeAgo(String time) {
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS).parse(time);
        } catch (ParseException e) {}

        long now = new Date().getTime();
        StringBuilder stringBuilder = new StringBuilder();
        String timeAgo = stringBuilder
                .append(getRelativeTimeSpanString(
                        date.getTime(),
                        now,
                        DateUtils.SECOND_IN_MILLIS))
                .toString();
        if(timeAgo.contains(" hours")) timeAgo = timeAgo.replace(" hours", "hr");
        else if(timeAgo.contains(" minutes")) timeAgo = timeAgo.replace(" minutes", "m");
        else if(timeAgo.contains(" seconds")) timeAgo = timeAgo.replace(" seconds", "s");
        else if(timeAgo.contains(" hour")) timeAgo = timeAgo.replace(" hour", "hr");
        else if(timeAgo.contains(" minute")) timeAgo = timeAgo.replace(" minute", "m");
        else if(timeAgo.contains(" second")) timeAgo = timeAgo.replace(" second", "s");
        timeAgo = timeAgo.replace("ago", "");
        return timeAgo;
    }

    public static String formatDate(long dateMillisecs, String format) {
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)
        DateFormat dateFormat = new SimpleDateFormat(format);
        // Get the date today using Calendar object.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateMillisecs);
        Date date = calendar.getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        return dateFormat.format(date);
    }

    public static String getDateNow() {
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String dateNow = dateFormat.format(today);
        return dateNow;
    }

    public static int getCurrentHourOfDay(ETime hourOfDayType, ETime locale) {
        switch(locale){
            case LOCALE:
                return getHourOfDay(hourOfDayType);
            case NOT_LOCALE:
                return getHourOfDay(hourOfDayType);
            default:
                return -1;
        }
    }

    private static int getHourOfDay(ETime timeFormat) {
        switch(timeFormat){
            case HOUR_OF_DAY_12:
                return Calendar.getInstance().get(Calendar.HOUR);
            case HOUR_OF_DAY_24:
                int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                //give iet 24 so math is easy throughout app
                if(hourOfDay == 0) hourOfDay = 24;
                return hourOfDay;
        }
        return -1;
    }

    /**
     * @param date is the date object being converted
     *
     */
    public static String toDateHour(Date date) {
        Format dateFormatter = new SimpleDateFormat("MM/dd/yy");
        Format hourFormatter = new SimpleDateFormat("HH");
        String dateF = dateFormatter.format(date);
        String hourF = hourFormatter.format(date);
        if(Integer.parseInt(hourF) < 10){
            hourF = hourF.substring(1) + "am";
        } else if(Integer.parseInt(hourF) > 12){
            int hourFInt = Integer.parseInt(hourF) - 12;
            hourF = hourFInt + "pm";
        }

        return dateF + " " + hourF;
    }

    public static String toDateOnly(Date date) {
        Format dateFormatter = new SimpleDateFormat("MM/dd/yy");
        String dateF = dateFormatter.format(date);

        return dateF;
    }

    public enum ETime {
        LOCALE, HOUR_OF_DAY_12, HOUR_OF_DAY_24, NOT_LOCALE,
        AGO_MILLISEC, AGO_MINUTE, AGO_HOUR, AGO_DAY;
    }

}
