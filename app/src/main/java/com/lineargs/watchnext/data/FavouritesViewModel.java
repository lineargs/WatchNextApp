package com.lineargs.watchnext.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.lineargs.watchnext.workers.SyncWorker;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FavouritesViewModel extends AndroidViewModel {

    private WorkManager workManager;
    private LiveData<List<Favourites>> favourites;

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
        workManager = WorkManager.getInstance(application);
        WatchNextRepository repository = new WatchNextRepository(application);
        favourites = repository.getFavourites();
    }

    public LiveData<List<Favourites>> getFavourites() {
        return favourites;
    }

    public void periodicSync() {
        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        PeriodicWorkRequest periodicSyncWorker = new PeriodicWorkRequest.Builder(SyncWorker.class,
                24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();
        workManager.enqueueUniquePeriodicWork("periodic_sync",
                ExistingPeriodicWorkPolicy.REPLACE, periodicSyncWorker);
    }
}
