package com.lineargs.watchnext.sync.syncmovies;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by goranminov on 10/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncUtils}
 */

public class MovieSyncUtils {

    public static void syncPopularMovies(@NonNull Context context) {
        Intent intent = new Intent(context, MovieSyncIntentService.class);
        intent.putExtra(MovieSyncIntentService.SYNC, 1);
        context.startService(intent);
    }

    public static void syncTopMovies(@NonNull Context context) {
        Intent intent = new Intent(context, MovieSyncIntentService.class);
        intent.putExtra(MovieSyncIntentService.SYNC, 2);
        context.startService(intent);
    }

    public static void syncUpcomingMovies(@NonNull Context context) {
        Intent intent = new Intent(context, MovieSyncIntentService.class);
        intent.putExtra(MovieSyncIntentService.SYNC, 3);
        context.startService(intent);
    }

    public static void syncTheaterMovies(@NonNull Context context) {
        Intent intent = new Intent(context, MovieSyncIntentService.class);
        intent.putExtra(MovieSyncIntentService.SYNC, 4);
        context.startService(intent);
    }
}
