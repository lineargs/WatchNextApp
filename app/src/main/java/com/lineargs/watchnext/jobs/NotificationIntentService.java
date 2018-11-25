package com.lineargs.watchnext.jobs;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by goranminov on 01/12/2017.
 * <p>
 * See {@link IntentService}
 */

public class NotificationIntentService extends IntentService {

    private final static String TAG = "NotificationIntentService";

    public NotificationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        int id = intent.getIntExtra(ReminderFirebaseUtilities.ID, 0);
        NotificationTasks.executeTask(id, action, getApplicationContext());
    }
}
