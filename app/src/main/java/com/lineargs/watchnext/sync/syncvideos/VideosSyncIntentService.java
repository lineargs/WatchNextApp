package com.lineargs.watchnext.sync.syncvideos;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by goranminov on 26/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncIntentService}
 */

public class VideosSyncIntentService extends IntentService {

    static final String MOVIE_ID = "movie_id";
    static final String TV_ID = "tv_id";
    private final static String TAG = "VideosSyncIntentService";

    public VideosSyncIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent.hasExtra(MOVIE_ID)) {
            String id = intent.getStringExtra(MOVIE_ID);
            VideosSyncTask.syncVideos(this, id);
        }

        if (intent.hasExtra(TV_ID)) {
            String id = intent.getStringExtra(TV_ID);
            VideosSyncTask.syncTVVideos(this, id);
        }
    }
}
