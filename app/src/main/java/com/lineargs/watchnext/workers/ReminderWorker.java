package com.lineargs.watchnext.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.TimeUnit;

public class ReminderWorker extends ListenableWorker {

    private static final int SYNC_FLEXTIME_MINUTES = 60;
    private static final int SYNC_FLEXTIME_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(SYNC_FLEXTIME_MINUTES));

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     *
     * See @https://developer.android.com/topic/libraries/architecture/workmanager/migrating-fb
     */
    public ReminderWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return null;
    }

    @Override
    public void onStopped() {
        super.onStopped();
    }
}
