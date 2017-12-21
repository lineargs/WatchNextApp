package com.lineargs.watchnext.sync.syncseries;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

public class SerieDetailUtils {

    public static void syncSeasons(@NonNull Context context, @NonNull Uri uri) {
        Intent intent = new Intent(context, SerieDetailIntentService.class);
        intent.setData(uri);
        context.startService(intent);
    }
}
