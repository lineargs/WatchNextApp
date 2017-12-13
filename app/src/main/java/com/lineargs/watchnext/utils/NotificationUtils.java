package com.lineargs.watchnext.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.jobs.ReminderIntentService;
import com.lineargs.watchnext.jobs.ReminderTasks;
import com.lineargs.watchnext.ui.NotificationActivity;

/**
 * Created by goranminov on 01/12/2017.
 * <p>
 * Utilities class used for building our notifications
 */

public class NotificationUtils extends ContextWrapper {


    private static final String EPISODES_REMINDER_NOTIFICATION_CHANNEL_ID = "episode_reminder_channel";
    private static final String EPISODES_CHANNEL = "Episodes";
    private static final String SERIES_CHANNEL_GROUP = "Series";
    private static final String ID = "id", SERIE_ID = "serie_id";

    public NotificationUtils(Context base) {
        super(base);
    }

    public static void clearNotification(Context context, int id) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(id);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static NotificationChannel setEpisodeNotificationChannel() {
        NotificationChannel notificationChannel = new NotificationChannel(EPISODES_REMINDER_NOTIFICATION_CHANNEL_ID,
                EPISODES_CHANNEL, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setGroup(SERIE_ID);
        return notificationChannel;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static NotificationChannelGroup setSeriesNotificationChannelGroup() {
        return new NotificationChannelGroup(SERIE_ID, SERIES_CHANNEL_GROUP);
    }

    public static void episodeReminder(String text, int id, String title, Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null) {
                notificationManager.createNotificationChannelGroup(setSeriesNotificationChannelGroup());
                notificationManager.createNotificationChannel(setEpisodeNotificationChannel());
            }
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, EPISODES_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorGrey))
                .setSmallIcon(R.drawable.icon_tv_black)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(title)
                .setContentText(context.getString(R.string.notification_text, text))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.notification_text, text)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(id, context))
                .addAction(dismissNotification(context, id))
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        if (notificationManager != null) {
            notificationManager.notify(id, notificationBuilder.build());
        }
    }

    private static PendingIntent contentIntent(int id, Context context) {
        Intent startActivity = new Intent(context, NotificationActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(startActivity);
        Uri uri = DataContract.Episodes.buildEpisodeUriWithId(id);
        startActivity.setData(uri);
        return stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static NotificationCompat.Action dismissNotification(Context context, int id) {
        Intent ignoreReminderIntent = new Intent(context, ReminderIntentService.class);
        ignoreReminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);
        ignoreReminderIntent.putExtra(ID, id);
        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                id,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Action
                (R.drawable.icon_cancel_black, context.getString(R.string.notification_dismiss), ignoreReminderPendingIntent);
    }

    private static Bitmap largeIcon(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_tv_black);
    }
}
