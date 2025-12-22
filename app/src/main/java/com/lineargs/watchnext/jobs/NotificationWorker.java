package com.lineargs.watchnext.jobs;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lineargs.watchnext.utils.NotificationUtils;

public class NotificationWorker extends Worker {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String NAME = "name";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        int id = getInputData().getInt(ID, 0);
        String title = getInputData().getString(TITLE);
        String name = getInputData().getString(NAME);

        if (title != null && name != null) {
            NotificationUtils.episodeReminder(name, id, title, getApplicationContext());
            return Result.success();
        }

        return Result.failure();
    }
}
