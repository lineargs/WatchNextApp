package com.lineargs.watchnext.sync.syncmovies;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;

/**
 * Created by goranminov on 10/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncIntentService}
 */
@SuppressLint({"unused", "Registered"})
public class MovieSyncIntentService extends IntentService {

    static final String SYNC = "syncmovies";
    private final static String TAG = "MovieSyncIntentService";

    public MovieSyncIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent.hasExtra(SYNC)) {
            switch (intent.getIntExtra(SYNC, 0)) {
                case 0:
                    break;
                case 1:
                    MovieSyncTask.syncPopularMovies(this);
                    break;
                case 2:
                    MovieSyncTask.syncTopMovies(this);
                    break;
                case 3:
                    MovieSyncTask.syncUpcomingMovies(this);
                    break;
                case 4:
                    MovieSyncTask.syncTheaterMovies(this);
                    break;
            }
        }
    }
}
