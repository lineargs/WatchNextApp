package com.lineargs.watchnext.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lineargs.watchnext.utils.NotificationUtils;

public class SyncNowWorker extends Worker {

    private static final String TAG = SyncNowWorker.class.getSimpleName();
    private static final int SYNC_NOTIFICATION_ID = 29101988;

    /**
     * Creates an instance of the {@link Worker}.
     *
     * @param context   the application {@link Context}
     * @param workerParams the set of {@link WorkerParameters}
     */
    public SyncNowWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context applicationContext = getApplicationContext();
        NotificationUtils.syncProgress(SYNC_NOTIFICATION_ID, applicationContext);
        return Result.success();
    }
}
