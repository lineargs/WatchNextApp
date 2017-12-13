package com.lineargs.watchnext.sync.syncvideos;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by goranminov on 26/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncUtils}
 */

public class VideosSyncUtils {

    public static void syncVideos(@NonNull Context context, @NonNull String id) {
        Intent intent = new Intent(context, VideosSyncIntentService.class);
        intent.putExtra(VideosSyncIntentService.MOVIE_ID, id);
        context.startService(intent);
    }

    public static void syncTVVideos(@NonNull Context context, @NonNull String id) {
        Intent intent = new Intent(context, VideosSyncIntentService.class);
        intent.putExtra(VideosSyncIntentService.TV_ID, id);
        context.startService(intent);
    }
}
