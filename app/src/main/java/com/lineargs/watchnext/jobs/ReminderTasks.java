package com.lineargs.watchnext.jobs;

import com.lineargs.watchnext.utils.NotificationUtils;

/**
 * Created by goranminov on 01/12/2017.
 * <p>
 * Helper class for our IntentService
 */

public class ReminderTasks {

    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";

    static void executeTask(int id, String action) {
        if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            NotificationUtils.clearNotification(id);
        }
    }
}
