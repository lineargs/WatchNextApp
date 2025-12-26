package com.lineargs.watchnext.utils;

import android.text.TextUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReminderUtils {

    private static final String DATE_PATTERN = "dd MMM yyyy";

    public static int getReminderDelayInSeconds(String date) {
        if (TextUtils.isEmpty(date)) return 0;
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
        Date releaseDay = null;
        try {
            releaseDay = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            // Try standard API format if the normalized one fails
            try {
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                releaseDay = simpleDateFormat.parse(date);
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
        }
        
        if (releaseDay != null) {
            Calendar airTime = Calendar.getInstance();
            airTime.setTime(releaseDay);
            airTime.set(Calendar.HOUR_OF_DAY, 20);
            airTime.set(Calendar.MINUTE, 0);
            airTime.set(Calendar.SECOND, 0);
            airTime.set(Calendar.MILLISECOND, 0);
            
            long delayMillis = airTime.getTimeInMillis() - System.currentTimeMillis();
            return delayMillis > 0 ? (int) TimeUnit.MILLISECONDS.toSeconds(delayMillis) : 0;
        }
        return 0;
    }
    
    public static boolean shouldShowReminderFab(String date) {
        if (TextUtils.isEmpty(date)) return false;
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
        Date releaseDay = null;
        try {
            releaseDay = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            try {
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                releaseDay = simpleDateFormat.parse(date);
            } catch (ParseException e2) {
                return false;
            }
        }
        
        if (releaseDay == null) return false;
        
        Calendar airTime = Calendar.getInstance();
        airTime.setTime(releaseDay);
        airTime.set(Calendar.HOUR_OF_DAY, 20);
        airTime.set(Calendar.MINUTE, 0);
        airTime.set(Calendar.SECOND, 0);
        airTime.set(Calendar.MILLISECOND, 0);
        
        return System.currentTimeMillis() <= airTime.getTimeInMillis();
    }
}
