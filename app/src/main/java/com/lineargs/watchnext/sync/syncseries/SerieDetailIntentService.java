package com.lineargs.watchnext.sync.syncseries;

import android.app.IntentService;
import android.content.Intent;

public class SerieDetailIntentService extends IntentService {

    private final static String TAG = "SerieDetailIntentService";

    public SerieDetailIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getData() != null && intent.hasExtra(SerieDetailUtils.UPDATE_TAG)) {
            SerieDetailTask.updateSeasons(this, intent.getData());
        } else if (intent.getData() != null) {
            SerieDetailTask.syncSeasons(this, intent.getData());
        }
    }
}
