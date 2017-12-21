package com.lineargs.watchnext.sync.syncsearch;

import android.app.IntentService;
import android.content.Intent;

public class SearchSyncIntentService extends IntentService {

    static final String QUERY = "query";
    static final String ADULT = "adult";
    static final String TV_QUERY = "tv_query";
    static final String MOVIE_ID = "movie_id";
    static final String TV_ID = "tv_id";
    private final static String TAG = "SearchSyncIntentService";

    public SearchSyncIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra(QUERY)) {
            String query = intent.getStringExtra(QUERY);
            boolean adult = intent.getBooleanExtra(ADULT, false);
            SearchSyncTask.syncSearchMovies(this, query, adult);
        }

        if (intent.hasExtra(TV_QUERY)) {
            String query = intent.getStringExtra(TV_QUERY);
            boolean adult = intent.getBooleanExtra(ADULT, false);
            SearchSyncTask.syncSearchTV(this, query, adult);
        }
        if (intent.hasExtra(MOVIE_ID)) {
            String id = intent.getStringExtra(MOVIE_ID);
            SearchSyncTask.syncSearchMovie(this, id);
        }
        if (intent.hasExtra(TV_ID)) {
            String id = intent.getStringExtra(TV_ID);
            SearchSyncTask.syncSearchTV(this, id);
        }
    }
}
