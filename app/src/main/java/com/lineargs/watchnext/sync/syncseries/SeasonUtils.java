package com.lineargs.watchnext.sync.syncseries;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

public class SeasonUtils {

    public static void syncEpisodes(@NonNull Context context, @NonNull String id, @NonNull int seasonNumber, @NonNull String seasonId) {
        Intent intent = new Intent(context, SeasonIntentService.class);
        intent.putExtra(SeasonIntentService.ID, id);
        intent.putExtra(SeasonIntentService.SEASON_NUMBER, seasonNumber);
        intent.putExtra(SeasonIntentService.SEASON_ID, seasonId);
        context.startService(intent);
    }
}
