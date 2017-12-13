package com.lineargs.watchnext.sync.synccredits;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by goranminov on 11/11/2017.
 * <p>
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */

public class CreditSyncIntentService extends IntentService {

    static final String ID = "id";
    static final String ENDPOINT = "endpoint";
    private final static String TAG = "CreditSyncIntentService";

    public CreditSyncIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra(ID)) {
            if (intent.hasExtra(ENDPOINT)) {
                String id = intent.getStringExtra(ID);
                switch (intent.getIntExtra(ENDPOINT, 2)) {
                    case 0:
                        CreditSyncTask.syncMovieCredits(this, id);
                        break;
                    case 1:
                        CreditSyncTask.syncTVCredits(this, id);
                        break;
                }
            }
        }
    }
}
