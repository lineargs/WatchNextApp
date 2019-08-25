package com.lineargs.watchnext.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;

import com.lineargs.watchnext.workers.CleanUpWorker;
import com.lineargs.watchnext.workers.SyncNowWorker;

import java.util.List;

public class FavouritesViewModel extends AndroidViewModel {

    private WatchNextRepository repository;
    private WorkManager workManager;
    private LiveData<List<Favourites>> favourites;

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
        workManager = WorkManager.getInstance(application);
        repository = new WatchNextRepository(application);
        favourites = repository.getFavourites();
    }

    public LiveData<List<Favourites>> getFavourites() {
        return favourites;
    }

    public void syncNow() {
        WorkContinuation continuation = workManager.beginUniqueWork("sync_now",
                ExistingWorkPolicy.REPLACE, OneTimeWorkRequest.from(CleanUpWorker.class));
        OneTimeWorkRequest.Builder syncNowBuilder = new OneTimeWorkRequest.Builder(SyncNowWorker.class);
        continuation = continuation.then(syncNowBuilder.build());
        continuation.enqueue();
    }
}
