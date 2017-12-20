package com.lineargs.watchnext.sync.syncmovies;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;

/**
 * Created by goranminov on 10/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncIntentService}
 */
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
