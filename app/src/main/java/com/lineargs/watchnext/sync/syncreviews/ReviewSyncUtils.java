package com.lineargs.watchnext.sync.syncreviews;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by goranminov on 22/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncUtils}
 */

public class ReviewSyncUtils {

    public static void syncReviews(@NonNull Context context, @NonNull String id) {
        Intent intent = new Intent(context, ReviewSyncIntentService.class);
        intent.putExtra(ReviewSyncIntentService.ID, id);
        context.startService(intent);
    }
}
