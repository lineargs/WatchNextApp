package com.lineargs.watchnext.sync.syncadapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by goranminov on 02/12/2017.
 * <p>
 * The service which allows the sync adapter framework to access the authenticator.
 */

public class WatchNextAuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private WatchNextAuthenticator watchNextAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        watchNextAuthenticator = new WatchNextAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return watchNextAuthenticator.getIBinder();
    }
}
