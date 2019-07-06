package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.lineargs.watchnext.utils.MovieUtils;
import com.lineargs.watchnext.utils.retrofit.movies.moviedetail.ReviewsResult;
import com.lineargs.watchnext.utils.retrofit.videos.VideosResult;

import java.util.List;

public class WatchNextRepository {

    private MoviesDao moviesDao;
    private SeriesDao seriesDao;
    private VideosDao videosDao;
    private ReviewsDao reviewsDao;
    private LiveData<List<Movies>> popularMovies;
    private LiveData<List<Movies>> topRatedMovies;
    private LiveData<List<Movies>> upcomingMovies;
    private LiveData<List<Movies>> theatreMovies;
    private LiveData<List<Series>> popularSeries;
    private LiveData<List<Series>> topRatedSeries;
    private LiveData<List<Series>> onTheAirSeries;
    private LiveData<List<Favourites>> favourites;

    WatchNextRepository(Application application) {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(application);
        moviesDao = database.moviesDao();
        popularMovies = moviesDao.getPopularMovies();
        topRatedMovies = moviesDao.getTopRatedMovies();
        upcomingMovies = moviesDao.getUpcomingMovies();
        theatreMovies = moviesDao.getTheaterMovies();
        seriesDao = database.seriesDao();
        onTheAirSeries = seriesDao.getOnTheAirSeries();
        topRatedSeries = seriesDao.getTopRatedSeries();
        popularSeries = seriesDao.getPopularSeries();
        FavouritesDao favouritesDao = database.favouritesDao();
        favourites = favouritesDao.getAllFavourites();
        videosDao = database.videosDao();
        reviewsDao = database.reviewsDao();
    }

    LiveData<Movies> getMovie(int tmdbId) {
        return moviesDao.getMovie(tmdbId);
    }

    LiveData<List<Movies>> getPopularMovies() {
        return popularMovies;
    }

    LiveData<List<Movies>> getTopRatedMovies() {
        return topRatedMovies;
    }

    LiveData<List<Movies>> getUpcomingMovies() {
        return upcomingMovies;
    }

    LiveData<List<Movies>> getTheatreMovies() {
        return theatreMovies;
    }

    LiveData<List<Series>> getPopularSeries() {
        return popularSeries;
    }

    LiveData<List<Series>> getTopRatedSeries() {
        return topRatedSeries;
    }

    LiveData<List<Series>> getOnTheAirSeries() {
        return onTheAirSeries;
    }

    public LiveData<List<Favourites>> getFavourites() {
        return favourites;
    }

    public LiveData<List<Videos>> getVideos(int tmdbId) {
        return videosDao.getVideos(tmdbId);
    }

    public LiveData<List<Reviews>> getReviews(int tmdbId) {return reviewsDao.getReviews(tmdbId);}

    void insertMovie(Movies movies) {
        new InsertMoviesTask(moviesDao).execute(movies);
    }

    void updateMovie(Movies movies) {
        new UpdateMovieTask(moviesDao).execute(movies);
    }

    public void insertSeries(Series series) {
        new InsertSeriesTask(seriesDao).execute(series);
    }

    void insertVideos(com.lineargs.watchnext.utils.retrofit.videos.Videos videos, int tmdbId) {
        new InsertVideosTask(videosDao, tmdbId).execute(videos);
    }

    void insertReviews(com.lineargs.watchnext.utils.retrofit.movies.moviedetail.Reviews reviews, int tmdbId) {
        new InsertReviewsTask(reviewsDao, tmdbId).execute(reviews);
    }

    private static class UpdateMovieTask extends AsyncTask<Movies, Void, Void> {

        private MoviesDao moviesDao;

        UpdateMovieTask(MoviesDao dao) {
            moviesDao = dao;
        }

        @Override
        protected Void doInBackground(final Movies... movies) {
            Movies movie = movies[0];
            moviesDao.updateMovie(movie.getTmdbId(), movie.getImdbId(), movie.getHomepage(), movie.getProductionCompanies(),
                    movie.getProductionCountries(), movie.getGenres(), movie.getRuntime(), movie.getStatus());
            return null;
        }
    }

    private static class InsertMoviesTask extends AsyncTask<Movies, Void, Void> {

        private MoviesDao moviesDao;

        InsertMoviesTask(MoviesDao dao) {
            moviesDao = dao;
        }

        @Override
        protected Void doInBackground(final Movies... movies) {
            moviesDao.insert(movies[0]);
            return null;
        }
    }

    private static class InsertSeriesTask extends AsyncTask<Series, Void, Void> {

        private SeriesDao seriesDao;

        InsertSeriesTask(SeriesDao dao) {
            seriesDao = dao;
        }

        @Override
        protected Void doInBackground(Series... series) {
            seriesDao.insert(series[0]);
            return null;
        }
    }

    private static class InsertVideosTask extends AsyncTask<com.lineargs.watchnext.utils.retrofit.videos.Videos, Void, Void> {

        private VideosDao videosDao;
        private int tmdbId;

        InsertVideosTask(VideosDao dao, int tmdbId) {
            videosDao = dao;
            this.tmdbId = tmdbId;
        }

        @Override
        protected Void doInBackground(com.lineargs.watchnext.utils.retrofit.videos.Videos... videos) {
            com.lineargs.watchnext.utils.retrofit.videos.Videos tmdbVideos = videos[0];
            List<VideosResult> resultList = tmdbVideos.getResults();
            for (VideosResult result : resultList) {
                Videos video = new Videos();
                video.setTmdbId(tmdbId);
                video.setName(result.getName());
                video.setKey(result.getKey());
                video.setImage(MovieUtils.getYouTubeImage(result.getKey()));
                videosDao.insert(video);
            }
            return null;
        }
    }

    private static class InsertReviewsTask extends AsyncTask<com.lineargs.watchnext.utils.retrofit.movies.moviedetail.Reviews, Void, Void> {

        private ReviewsDao reviewsDao;
        private int tmdbId;

        InsertReviewsTask(ReviewsDao dao, int tmdbId) {
            reviewsDao = dao;
            this.tmdbId = tmdbId;
        }

        @Override
        protected Void doInBackground(com.lineargs.watchnext.utils.retrofit.movies.moviedetail.Reviews... reviews) {
            com.lineargs.watchnext.utils.retrofit.movies.moviedetail.Reviews tmdbReviews = reviews[0];
            List<ReviewsResult> resultList = tmdbReviews.getResults();
            for (ReviewsResult result : resultList) {
                Reviews review = new Reviews();
                review.setTmdbId(tmdbId);
                review.setAuthor(result.getAuthor());
                review.setContent(result.getContent());
                review.setUrl(result.getUrl());
                reviewsDao.insert(review);
            }
            return null;
        }
    }
}
