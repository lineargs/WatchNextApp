package com.lineargs.watchnext.sync.syncmovies;

import android.app.IntentService;
import android.content.Intent;

public class MovieSyncIntentService extends IntentService {

    static final String ID = "movie_id";
    private final static String TAG = "MovieSyncIntentService";

    public MovieSyncIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getData() != null) {
            MovieSyncTask.syncMovieDetail(this, intent.getData());
        }
    }
}
