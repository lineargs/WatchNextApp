package com.lineargs.watchnext.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;

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
        WorkContinuation continuation = workManager.beginUniqueWork("notification",
                ExistingWorkPolicy.REPLACE, OneTimeWorkRequest.from(SyncNowWorker.class));
        continuation.enqueue();
    }
}
