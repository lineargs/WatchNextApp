package com.lineargs.watchnext.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.dao.DetailsDao;
import com.lineargs.watchnext.data.entity.Videos;

import java.util.List;

public class VideosRepository {

    private final DetailsDao detailsDao;

    public VideosRepository(Application application) {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        detailsDao = database.detailsDao();
    }

    public LiveData<List<Videos>> getVideos(int movieId) {
        return detailsDao.getVideosLiveData(movieId);
    }
}
