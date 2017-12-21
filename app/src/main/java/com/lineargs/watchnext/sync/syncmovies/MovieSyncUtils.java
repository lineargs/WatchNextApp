package com.lineargs.watchnext.sync.syncmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

public class MovieSyncUtils {

    public static void syncMovieDetail (@NonNull Context context, Uri uri) {
        Intent intent = new Intent(context, MovieSyncIntentService.class);
        intent.setData(uri);
        context.startService(intent);
    }
}
