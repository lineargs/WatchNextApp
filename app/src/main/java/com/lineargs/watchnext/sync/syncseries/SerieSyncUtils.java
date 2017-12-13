package com.lineargs.watchnext.sync.syncseries;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by goranminov on 11/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncUtils}
 */

public class SerieSyncUtils {

    public static void syncPopularSeries(@NonNull Context context) {
        Intent intent = new Intent(context, SerieSyncIntentService.class);
        intent.putExtra(SerieSyncIntentService.SYNC_SERIES, 1);
        context.startService(intent);
    }

    public static void syncTopSeries(@NonNull Context context) {
        Intent intent = new Intent(context, SerieSyncIntentService.class);
        intent.putExtra(SerieSyncIntentService.SYNC_SERIES, 2);
        context.startService(intent);
    }

    public static void syncOnTheAirSeries(@NonNull Context context) {
        Intent intent = new Intent(context, SerieSyncIntentService.class);
        intent.putExtra(SerieSyncIntentService.SYNC_SERIES, 3);
        context.startService(intent);
    }
}
