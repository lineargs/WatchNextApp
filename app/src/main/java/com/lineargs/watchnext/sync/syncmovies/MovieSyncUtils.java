package com.lineargs.watchnext.sync.syncmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;

public class MovieSyncUtils {

    static final String UPDATE_TAG = "UPDATE";

    public static void syncFullMovieDetail(@NonNull Context context, Uri uri) {
        Intent intent = new Intent(context, MovieSyncIntentService.class);
        intent.setData(uri);
        context.startService(intent);
    }

    public static void syncUpdateMovieDetail(@NonNull Context context, Uri uri) {
        Intent intent = new Intent(context, MovieSyncIntentService.class);
        intent.setData(uri);
        intent.putExtra(UPDATE_TAG, true);
        context.startService(intent);
    }
}
