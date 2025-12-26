package com.lineargs.watchnext.sync.syncpeople;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.lineargs.watchnext.workers.PersonWorker;

public class PersonSyncUtils {

    public static void syncPerson(@NonNull Context context, @NonNull String id) {
        Data inputData = new Data.Builder()
                .putString(PersonWorker.ARG_ID, id)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(PersonWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(context).enqueue(request);
    }
}
