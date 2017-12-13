package com.lineargs.watchnext.jobs;

import android.content.Context;

import com.lineargs.watchnext.utils.NotificationUtils;

/**
 * Created by goranminov on 03/12/2017.
 * <p>
 * Helper task class to show the notification with certain ID
 */

class ReminderFirebaseTasks {

    static final String ACTION_REMINDER = "reminder";

    static void executeTask(Context context, int id, String title, String name, String action) {
        if (ACTION_REMINDER.equals(action)) {
            issueReminder(context, id, title, name);
        }
    }

    private static void issueReminder(Context context, int id, String title, String name) {
        NotificationUtils.episodeReminder(name, id, title, context);
    }
}
