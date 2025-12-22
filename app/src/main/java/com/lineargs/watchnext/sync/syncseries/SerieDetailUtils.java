package com.lineargs.watchnext.sync.syncseries;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;

public class SerieDetailUtils {

    static final String UPDATE_TAG = "UPDATE";

    public static void syncSeasons(@NonNull Context context, @NonNull Uri uri) {
        Intent intent = new Intent(context, SerieDetailIntentService.class);
        intent.setData(uri);
        context.startService(intent);
    }

    public static void updateDetails(@NonNull Context context, @NonNull Uri uri) {
        Intent intent = new Intent(context, SerieDetailIntentService.class);
        intent.setData(uri);
        intent.putExtra(UPDATE_TAG, true);
        context.startService(intent);
    }
}
