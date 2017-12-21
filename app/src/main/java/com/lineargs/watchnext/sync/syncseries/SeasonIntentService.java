package com.lineargs.watchnext.sync.syncseries;

import android.app.IntentService;
import android.content.Intent;

public class SeasonIntentService extends IntentService {

    static final String ID = "id";
    static final String SEASON_NUMBER = "season_number";
    static final String SEASON_ID = "season_id";
    private final static String TAG = "SerieDetailIntentService";

    public SeasonIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra(ID) && intent.hasExtra(SEASON_NUMBER)
                && intent.hasExtra(SEASON_ID)) {
            String id = intent.getStringExtra(ID);
            String seasonNumber = intent.getStringExtra(SEASON_NUMBER);
            String seasonId = intent.getStringExtra(SEASON_ID);
            SeasonTask.syncEpisodes(this, id, seasonNumber, seasonId);
        }
    }
}
