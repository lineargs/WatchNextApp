package com.lineargs.watchnext.sync.syncseries;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.lineargs.watchnext.workers.SeasonWorker;

public class SeasonUtils {

    public static void syncEpisodes(@NonNull Context context, @NonNull String id, @NonNull int seasonNumber, @NonNull String seasonId) {
        Data inputData = new Data.Builder()
                .putString(SeasonWorker.ARG_ID, id)
                .putInt(SeasonWorker.ARG_SEASON_NUMBER, seasonNumber)
                .putString(SeasonWorker.ARG_SEASON_ID, seasonId)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SeasonWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(context).enqueue(request);
    }
}
