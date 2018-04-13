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
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.jobs.ReminderIntentService;
import com.lineargs.watchnext.jobs.ReminderTasks;
import com.lineargs.watchnext.ui.MainActivity;
import com.lineargs.watchnext.ui.NotificationActivity;

/**
 * Created by goranminov on 01/12/2017.
 * <p>
 * Utilities class used for building our notifications
 */

public class NotificationUtils extends ContextWrapper {

    private static final String EPISODES_REMINDER_NOTIFICATION_CHANNEL_ID = "episode_reminder_channel";
    private static final String SYNC_ADAPTER_CHANNEL_ID = "sync_adapter_channel";
    private static final String EPISODES_CHANNEL = "Episodes";
    private static final String SYNC_ADAPTER_CHANNEL = "Adapter";
    private static final String SERIES_CHANNEL_GROUP = "Series";
    private static final String ADAPTER_CHANNEL_GROUP = "Sync";
    private static final String ID = "id", SERIE_ID = "serie_id", SYNC_ID = "sync_id";
    private static NotificationManager manager;

    public NotificationUtils(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(EPISODES_REMINDER_NOTIFICATION_CHANNEL_ID,
                    EPISODES_CHANNEL, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationChannel syncNotificationChannel = new NotificationChannel(SYNC_ADAPTER_CHANNEL_ID,
                    SYNC_ADAPTER_CHANNEL, NotificationManager.IMPORTANCE_LOW);
            syncNotificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationChannelGroup channelGroup = new NotificationChannelGroup(SERIE_ID, SERIES_CHANNEL_GROUP);
            NotificationChannelGroup syncChannelGroup = new NotificationChannelGroup(SYNC_ID, ADAPTER_CHANNEL_GROUP);

            getManager().createNotificationChannelGroup(channelGroup);
            getManager().createNotificationChannelGroup(syncChannelGroup);

            notificationChannel.setGroup(SERIE_ID);
            syncNotificationChannel.setGroup(SYNC_ID);

            getManager().createNotificationChannel(notificationChannel);
            getManager().createNotificationChannel(syncNotificationChannel);
        }
    }

    public static void clearNotification(int id) {
        manager.cancel(id);
    }

    public static void episodeReminder(String text, int id, String title, Context context) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, EPISODES_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorGrey))
                .setSmallIcon(smallIcon())
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
        manager.notify(id, notificationBuilder.build());
    }

    public static void syncReminder(int id, Context context) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, SYNC_ADAPTER_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorBlack))
                .setSmallIcon(reminderSmallIcon())
                .setLargeIcon(reminderLargeIcon(context))
                .setContentTitle(context.getString(R.string.sync_complete))
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentText(context.getString(R.string.updated_content_text))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.updated_content_text)))
                .setContentIntent(reminderContentIntent(id, context))
                .addAction(dismissNotification(context, id))
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_LOW);
        }
        manager.notify(id, notificationBuilder.build());
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

    private static PendingIntent reminderContentIntent(int id, Context context) {
        Intent startActivity = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(startActivity);
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

    private static int smallIcon() {
        return R.drawable.icon_tv_black;
    }

    private static Bitmap largeIcon(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_tv_black);
    }

    private static int reminderSmallIcon() { return R.drawable.app_icon_black; }

    private static Bitmap reminderLargeIcon(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon_black);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
