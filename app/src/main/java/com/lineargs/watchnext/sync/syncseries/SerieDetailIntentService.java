package com.lineargs.watchnext.sync.syncseries;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by goranminov on 27/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncIntentService}
 */

public class SerieDetailIntentService extends IntentService {

    static final String ID = "id";
    private final static String TAG = "SerieDetailIntentService";

    public SerieDetailIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra(ID)) {
            String id = intent.getStringExtra(ID);
            SerieDetailTask.syncSeasons(this, id);
        }
    }
}
