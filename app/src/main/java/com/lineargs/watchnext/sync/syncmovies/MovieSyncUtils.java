package com.lineargs.watchnext.sync.syncmovies;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.lineargs.watchnext.workers.MovieDetailWorker;

public class MovieSyncUtils {

    public static void syncFullMovieDetail(@NonNull Context context, Uri uri) {
        Data inputData = new Data.Builder()
                .putString(MovieDetailWorker.ARG_URI, uri.toString())
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MovieDetailWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(context).enqueue(request);
    }

    public static void syncUpdateMovieDetail(@NonNull Context context, Uri uri) {
        Data inputData = new Data.Builder()
                .putString(MovieDetailWorker.ARG_URI, uri.toString())
                .putBoolean(MovieDetailWorker.ARG_UPDATE_ONLY, true)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MovieDetailWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(context).enqueue(request);
    }
}
