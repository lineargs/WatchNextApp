package com.lineargs.watchnext.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lineargs.watchnext.data.WatchNextDatabase;
import com.lineargs.watchnext.utils.NotificationUtils;

import static com.lineargs.watchnext.utils.Constants.SYNC_NOTIFICATION_ID;

public class CleanUpWorker extends Worker {

    public CleanUpWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context applicationContext = getApplicationContext();
        NotificationUtils.syncProgress(SYNC_NOTIFICATION_ID, applicationContext);
        WatchNextDatabase.getDatabase(getApplicationContext()).moviesDao().deleteAllMovies();
        WatchNextDatabase.getDatabase(getApplicationContext()).seriesDao().deleteAllSeries();
        return Result.success();
    }
}
