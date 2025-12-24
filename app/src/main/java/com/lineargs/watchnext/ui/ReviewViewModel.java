package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.lineargs.watchnext.data.ReviewsRepository;
import com.lineargs.watchnext.data.entity.Review;

import java.util.List;

public class ReviewViewModel extends AndroidViewModel {

    private final ReviewsRepository repository;
    private final MutableLiveData<Integer> movieId = new MutableLiveData<>();
    private final LiveData<List<Review>> reviews;

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        repository = new ReviewsRepository(application);
        reviews = Transformations.switchMap(movieId, repository::getReviews);
    }

    public void setMovieId(int id) {
        movieId.setValue(id);
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }
}
