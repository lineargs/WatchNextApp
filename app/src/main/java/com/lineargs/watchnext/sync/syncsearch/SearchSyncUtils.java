package com.lineargs.watchnext.sync.syncsearch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class SearchSyncUtils {

    public static void syncSearchMovies(@NonNull Context context, @NonNull String query, @NonNull boolean adult) {
        Intent intent = new Intent(context, SearchSyncIntentService.class);
        intent.putExtra(SearchSyncIntentService.QUERY, query);
        intent.putExtra(SearchSyncIntentService.ADULT, adult);
        context.startService(intent);
    }

    public static void syncSearchTV(@NonNull Context context, @NonNull String query, @NonNull boolean adult) {
        Intent intent = new Intent(context, SearchSyncIntentService.class);
        intent.putExtra(SearchSyncIntentService.TV_QUERY, query);
        intent.putExtra(SearchSyncIntentService.ADULT, adult);
        context.startService(intent);
    }

    public static void syncSearchMovie(@NonNull Context context, @NonNull String id) {
        Intent intent = new Intent(context, SearchSyncIntentService.class);
        intent.putExtra(SearchSyncIntentService.MOVIE_ID, id);
        context.startService(intent);
    }

    public static void syncTV(@NonNull Context context, @NonNull String id) {
        Intent intent = new Intent(context, SearchSyncIntentService.class);
        intent.putExtra(SearchSyncIntentService.TV_ID, id);
        context.startService(intent);
    }
}
