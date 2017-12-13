package com.lineargs.watchnext.sync.syncpeople;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by goranminov on 29/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncUtils}
 */

public class PersonSyncUtils {

    public static void syncReviews(@NonNull Context context, @NonNull String id) {
        Intent intent = new Intent(context, PersonSyncIntentService.class);
        intent.putExtra(PersonSyncIntentService.ID, id);
        context.startService(intent);
    }
}
