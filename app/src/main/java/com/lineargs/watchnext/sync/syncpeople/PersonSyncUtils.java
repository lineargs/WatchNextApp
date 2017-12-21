package com.lineargs.watchnext.sync.syncpeople;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class PersonSyncUtils {

    public static void syncReviews(@NonNull Context context, @NonNull String id) {
        Intent intent = new Intent(context, PersonSyncIntentService.class);
        intent.putExtra(PersonSyncIntentService.ID, id);
        context.startService(intent);
    }
}
