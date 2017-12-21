package com.lineargs.watchnext.sync.syncseries;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

public class SerieDetailIntentService extends IntentService {

    private final static String TAG = "SerieDetailIntentService";

    public SerieDetailIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        SerieDetailTask.syncSeasons(this, uri);
    }
}
