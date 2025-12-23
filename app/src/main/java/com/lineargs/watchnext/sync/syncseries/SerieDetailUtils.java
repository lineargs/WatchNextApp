package com.lineargs.watchnext.sync.syncseries;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.lineargs.watchnext.workers.SerieDetailWorker;

public class SerieDetailUtils {

    public static void syncSeasons(@NonNull Context context, @NonNull Uri uri) {
        Data inputData = new Data.Builder()
                .putString(SerieDetailWorker.ARG_URI, uri.toString())
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SerieDetailWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(context).enqueue(request);
    }

    public static void updateDetails(@NonNull Context context, @NonNull Uri uri) {
        Data inputData = new Data.Builder()
                .putString(SerieDetailWorker.ARG_URI, uri.toString())
                .putBoolean(SerieDetailWorker.ARG_UPDATE_ONLY, true)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SerieDetailWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(context).enqueue(request);
    }
}
