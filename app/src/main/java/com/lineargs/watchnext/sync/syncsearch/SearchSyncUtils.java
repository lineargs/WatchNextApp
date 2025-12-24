package com.lineargs.watchnext.sync.syncsearch;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.lineargs.watchnext.workers.SearchWorker;

public class SearchSyncUtils {

    public static void syncSearch(@NonNull Context context, @NonNull String query, @NonNull boolean adult) {
        Data inputData = new Data.Builder()
                .putString(SearchWorker.ARG_QUERY, query)
                .putBoolean(SearchWorker.ARG_ADULT, adult)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SearchWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(context).enqueue(request);
    }

    public static void syncSearchMovie(@NonNull Context context, @NonNull String id) {
        Data inputData = new Data.Builder()
                .putString(SearchWorker.ARG_MOVIE_ID, id)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SearchWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(context).enqueue(request);
    }

    public static void syncTV(@NonNull Context context, @NonNull String id) {
        Data inputData = new Data.Builder()
                .putString(SearchWorker.ARG_TV_ID, id)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SearchWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(context).enqueue(request);
    }
}
