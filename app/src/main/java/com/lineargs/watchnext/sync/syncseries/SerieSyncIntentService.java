package com.lineargs.watchnext.sync.syncseries;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;

/**
 * Created by goranminov on 11/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncIntentService}
 */

@SuppressLint({"unused", "Registered"})
public class SerieSyncIntentService extends IntentService {

    static final String SYNC_SERIES = "syncseries";
    private final static String TAG = "SerieSyncIntentService";

    public SerieSyncIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra(SYNC_SERIES)) {
            switch (intent.getIntExtra(SYNC_SERIES, 0)) {
                case 0:
                    break;
                case 1:
                    SerieSyncTask.syncPopularSeries(this);
                    break;
                case 2:
                    SerieSyncTask.syncTopRatedSeries(this);
                    break;
                case 3:
                    SerieSyncTask.syncOnTheAirSeries(this);
                    break;
            }
        }
    }
}
