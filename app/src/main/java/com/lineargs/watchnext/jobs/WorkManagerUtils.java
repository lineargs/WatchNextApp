package com.lineargs.watchnext.jobs;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import androidx.work.PeriodicWorkRequest;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.ExistingPeriodicWorkPolicy;

import com.lineargs.watchnext.workers.WatchNextWorker;
import com.lineargs.watchnext.workers.SeriesSubscriptionWorker;

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

    public static void scheduleSubscriptionCheck(Context context) {
        PeriodicWorkRequest subscriptionRequest = new PeriodicWorkRequest.Builder(
                SeriesSubscriptionWorker.class,
                24, TimeUnit.HOURS)
                .addTag("series_subscription_check")
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "series_subscription_check",
                ExistingPeriodicWorkPolicy.KEEP,
                subscriptionRequest
        );
    }

    private static final String PERIODIC_WORK_TAG = "watch_next_periodic_work";

    public static void schedulePeriodicSync(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(WatchNextWorker.class, 24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                PERIODIC_WORK_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                request
        );
    }

    public static void syncImmediately(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(WatchNextWorker.class)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context).enqueueUniqueWork(
                PERIODIC_WORK_TAG + "_immediate",
                ExistingWorkPolicy.KEEP,
                request
        );
    }

    public static void syncSubscriptionsImmediately(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SeriesSubscriptionWorker.class)
                .setConstraints(constraints)
                .addTag("series_subscription_check_immediate")
                .build();

        WorkManager.getInstance(context).enqueueUniqueWork(
                "series_subscription_check_immediate",
                ExistingWorkPolicy.REPLACE,
                request
        );
    }
}
