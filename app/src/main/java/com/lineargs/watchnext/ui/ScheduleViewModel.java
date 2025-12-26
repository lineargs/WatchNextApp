package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.WatchNextDatabase;
import com.lineargs.watchnext.data.entity.UpcomingEpisodes;

import java.util.List;

public class ScheduleViewModel extends AndroidViewModel {

    private final LiveData<List<UpcomingEpisodes>> upcomingEpisodes;

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        upcomingEpisodes = database.upcomingEpisodesDao().getUpcomingEpisodes();
    }

    public LiveData<List<UpcomingEpisodes>> getUpcomingEpisodes() {
        return upcomingEpisodes;
    }
}
