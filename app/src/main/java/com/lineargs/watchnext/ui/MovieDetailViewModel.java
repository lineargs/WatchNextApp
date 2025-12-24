package com.lineargs.watchnext.ui;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.lineargs.watchnext.data.CreditsRepository;
import com.lineargs.watchnext.data.FavoritesRepository;
import com.lineargs.watchnext.data.MoviesRepository;
import com.lineargs.watchnext.data.ReviewsRepository;
import com.lineargs.watchnext.data.VideosRepository;
import com.lineargs.watchnext.data.entity.Credits;
import com.lineargs.watchnext.data.entity.Favorites;
import com.lineargs.watchnext.data.entity.PopularMovie;
import com.lineargs.watchnext.data.entity.Review;
import com.lineargs.watchnext.data.entity.Videos;

import java.util.List;

public class MovieDetailViewModel extends AndroidViewModel {

    private final MoviesRepository moviesRepository;
    private final CreditsRepository creditsRepository;
    private final ReviewsRepository reviewsRepository;
    private final VideosRepository videosRepository;
    private final FavoritesRepository favoritesRepository;

    private final MutableLiveData<android.net.Uri> movieUri = new MutableLiveData<>();
    private final LiveData<com.lineargs.watchnext.data.entity.Movie> movie;
    private final LiveData<List<Credits>> cast;
    private final LiveData<List<Credits>> crew;
    private final LiveData<List<Review>> reviews;
    private final LiveData<List<Videos>> videos;
    private final LiveData<Favorites> favorite;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        moviesRepository = new MoviesRepository(application);
        creditsRepository = new CreditsRepository(application);
        reviewsRepository = new ReviewsRepository(application);
        videosRepository = new VideosRepository(application);
        favoritesRepository = new FavoritesRepository(application);

        movie = Transformations.switchMap(movieUri, uri -> {
            int id = Integer.parseInt(uri.getLastPathSegment());
            String path = uri.getPath();
            if (path.contains("popular")) {
                return (LiveData) moviesRepository.getPopularMovieLiveData(id);
            } else if (path.contains("toprated")) {
                return (LiveData) moviesRepository.getTopRatedMovieLiveData(id);
            } else if (path.contains("upcoming")) {
                return (LiveData) moviesRepository.getUpcomingMovieLiveData(id);
            } else if (path.contains("theater")) {
                return (LiveData) moviesRepository.getTheaterMovieLiveData(id);
            } else if (path.contains("search")) {
                // Search Repository
                com.lineargs.watchnext.data.SearchRepository searchRepo = new com.lineargs.watchnext.data.SearchRepository(getApplication());
                return (LiveData) searchRepo.getSearchMovieLiveData(id);
            } else if (path.contains("favorites")) {
                return (LiveData) favoritesRepository.getFavorite(id);
            } else {
                 return (LiveData) moviesRepository.getPopularMovieLiveData(id);
            }
        });

        LiveData<Integer> movieId = Transformations.map(movieUri, uri -> Integer.parseInt(uri.getLastPathSegment()));

        cast = Transformations.switchMap(movieId, creditsRepository::getCast);
        crew = Transformations.switchMap(movieId, creditsRepository::getCrew);
        reviews = Transformations.switchMap(movieId, reviewsRepository::getReviews);
        videos = Transformations.switchMap(movieId, videosRepository::getVideos);
        favorite = Transformations.switchMap(movieId, favoritesRepository::getFavorite);
    }

    public void setMovieUri(android.net.Uri uri) {
        movieUri.setValue(uri);
    }

    public LiveData<com.lineargs.watchnext.data.entity.Movie> getMovie() {
        return movie;
    }

    public LiveData<List<Credits>> getCast() {
        return cast;
    }

    public LiveData<List<Credits>> getCrew() {
        return crew;
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    public LiveData<List<Videos>> getVideos() {
        return videos;
    }

    public LiveData<Favorites> getFavorite() {
        return favorite;
    }
}
