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
import com.lineargs.watchnext.data.entity.Movie;
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
    private LiveData<Movie> movie;
    private LiveData<java.util.List<com.lineargs.watchnext.data.entity.Credits>> cast;
    private LiveData<java.util.List<com.lineargs.watchnext.data.entity.Credits>> crew;
    private LiveData<java.util.List<com.lineargs.watchnext.data.entity.Review>> reviews;
    private LiveData<java.util.List<com.lineargs.watchnext.data.entity.Videos>> videos;
    private LiveData<com.lineargs.watchnext.data.entity.Favorites> favorite;
    private final MutableLiveData<Integer> movieId = new MutableLiveData<>();

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
                return Transformations.map(moviesRepository.getPopularMovieLiveData(id), movie -> (Movie) movie);
            } else if (path.contains("toprated")) {
                return Transformations.map(moviesRepository.getTopRatedMovieLiveData(id), movie -> (Movie) movie);
            } else if (path.contains("upcoming")) {
                return Transformations.map(moviesRepository.getUpcomingMovieLiveData(id), movie -> (Movie) movie);
            } else if (path.contains("theater")) {
                return Transformations.map(moviesRepository.getTheaterMovieLiveData(id), movie -> (Movie) movie);
            } else if (path.contains("search")) {
                com.lineargs.watchnext.data.SearchRepository searchRepo = new com.lineargs.watchnext.data.SearchRepository(getApplication());
                return Transformations.map(searchRepo.getSearchMovieLiveData(id), movie -> (Movie) movie);
            } else if (path.contains("favorites")) {
                return Transformations.map(favoritesRepository.getFavorite(id), this::mapFavoriteToMovie);
            } else {
                return Transformations.map(moviesRepository.getPopularMovieLiveData(id), movie -> (Movie) movie);
            }
        });
 
        cast = Transformations.switchMap(movieId, creditsRepository::getCast);
        crew = Transformations.switchMap(movieId, creditsRepository::getCrew);
        reviews = Transformations.switchMap(movieId, reviewsRepository::getReviews);
        videos = Transformations.switchMap(movieId, videosRepository::getVideos);
        favorite = Transformations.switchMap(movieId, id -> {
            if (id == null) {
                return new MutableLiveData<>(null);
            } else {
                return favoritesRepository.getFavorite(id);
            }
        });
    }

    private Movie mapFavoriteToMovie(Favorites favorites) {
        if (favorites == null) return null;
        PopularMovie movie = new PopularMovie();
        movie.setTmdbId(favorites.getTmdbId());
        movie.setTitle(favorites.getTitle());
        movie.setOverview(favorites.getOverview());
        movie.setPosterPath(favorites.getPosterPath());
        movie.setBackdropPath(favorites.getBackdropPath());
        movie.setVoteAverage(favorites.getVoteAverage());
        movie.setReleaseDate(favorites.getReleaseDate());
        movie.setOriginalLanguage(favorites.getOriginalLanguage());
        movie.setProductionCompanies(favorites.getProductionCompanies());
        movie.setProductionCountries(favorites.getProductionCountries());
        movie.setStatus(favorites.getStatus());
        movie.setGenres(favorites.getGenres());
        movie.setRuntime(favorites.getRuntime());
        movie.setImdbId(favorites.getImdbId());
        movie.setHomepage(favorites.getHomepage());
        return movie;
    }

    public void setMovieId(int id) {
        movieId.setValue(id);
    }

    public void setMovieUri(android.net.Uri uri) {
        movieUri.setValue(uri);
        if (uri != null) {
            movieId.setValue(Integer.parseInt(uri.getLastPathSegment()));
        }
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
