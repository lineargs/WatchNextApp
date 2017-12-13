package com.lineargs.watchnext.sync.syncpeople;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by goranminov on 29/11/2017.
 * <p>
 * See {@link com.lineargs.watchnext.sync.synccredits.CreditSyncIntentService}
 */

public class PersonSyncIntentService extends IntentService {

    static final String ID = "id";
    private final static String TAG = "PersonSyncIntentService";

    public PersonSyncIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent.hasExtra(ID)) {
            String id = intent.getStringExtra(ID);
            PersonSyncTask.syncPerson(this, id);
        }
    }
}
