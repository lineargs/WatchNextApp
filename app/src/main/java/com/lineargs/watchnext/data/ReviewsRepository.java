package com.lineargs.watchnext.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.data.dao.DetailsDao;
import com.lineargs.watchnext.data.entity.Review;

import java.util.List;

public class ReviewsRepository {

    private final DetailsDao detailsDao;

    public ReviewsRepository(Application application) {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        detailsDao = database.detailsDao();
    }

    public LiveData<List<Review>> getReviews(int movieId) {
        return detailsDao.getReviewsLiveData(movieId);
    }
}
