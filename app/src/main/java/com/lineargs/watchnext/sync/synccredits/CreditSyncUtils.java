package com.lineargs.watchnext.sync.synccredits;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by goranminov on 11/11/2017.
 * <p>
 * Helper class to perform a sync immediately using an IntentService for asynchronous
 * execution.
 */

public class CreditSyncUtils {

    /**
     * @param context The Context used to start the IntentService for the sync.
     * @param id      The ID passed
     */
    public static void syncMovieCredits(@NonNull Context context, @NonNull String id) {
        Intent intent = new Intent(context, CreditSyncIntentService.class);
        intent.putExtra(CreditSyncIntentService.ID, id);
        intent.putExtra(CreditSyncIntentService.ENDPOINT, 0);
        context.startService(intent);
    }

    /**
     * @param context The Context used to start the IntentService for the sync.
     * @param id      The ID passed
     */
    @SuppressWarnings("unused")
    public static void syncTVCredits(@NonNull Context context, @NonNull String id) {
        Intent intent = new Intent(context, CreditSyncIntentService.class);
        intent.putExtra(CreditSyncIntentService.ID, id);
        intent.putExtra(CreditSyncIntentService.ENDPOINT, 1);
        context.startService(intent);
    }
}
