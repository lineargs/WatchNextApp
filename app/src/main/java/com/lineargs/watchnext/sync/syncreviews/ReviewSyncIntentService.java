package com.lineargs.watchnext.sync.syncreviews;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by goranminov on 22/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncIntentService}
 */

public class ReviewSyncIntentService extends IntentService {

    static final String ID = "id";
    private final static String TAG = "ReviewSyncIntentService";

    public ReviewSyncIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent.hasExtra(ID)) {
            String id = intent.getStringExtra(ID);
            ReviewSyncTask.syncReviews(this, id);
        }
    }
}
