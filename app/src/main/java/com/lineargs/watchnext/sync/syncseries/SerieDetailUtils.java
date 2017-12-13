package com.lineargs.watchnext.sync.syncseries;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by goranminov on 27/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncUtils}
 */

public class SerieDetailUtils {

    public static void syncSeasons(@NonNull Context context, @NonNull String id) {
        Intent intent = new Intent(context, SerieDetailIntentService.class);
        intent.putExtra(SerieDetailIntentService.ID, id);
        context.startService(intent);
    }
}
