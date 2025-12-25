package com.lineargs.watchnext.jobs;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class WorkManagerUtils {

    public static void scheduleReminder(Context context, int delaySeconds, int id, String title, String name) {
        Data inputData = new Data.Builder()
                .putInt(NotificationWorker.ID, id)
                .putString(NotificationWorker.TITLE, title)
                .putString(NotificationWorker.NAME, name)
                .build();

        OneTimeWorkRequest reminderRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
                .setInputData(inputData)
                .addTag(String.valueOf(id))
                .build();

        WorkManager.getInstance(context).enqueueUniqueWork(
                String.valueOf(id),
                ExistingWorkPolicy.REPLACE,
                reminderRequest
        );
    }

    public static void cancelReminder(Context context, int id) {
        WorkManager.getInstance(context).cancelUniqueWork(String.valueOf(id));
    }
}
