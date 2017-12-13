package com.lineargs.watchnext.sync.syncadapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by goranminov on 02/12/2017.
 * <p>
 * Service for our SyncAdapter
 */

public class WatchNextSyncService extends Service {

    private static final Object syncAdapterLock = new Object();
    private static WatchNextSyncAdapter watchNextSyncAdapter = null;


    @Override
    public void onCreate() {
        synchronized (syncAdapterLock) {
            if (watchNextSyncAdapter == null) {
                watchNextSyncAdapter = new WatchNextSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return watchNextSyncAdapter.getSyncAdapterBinder();
    }
}
