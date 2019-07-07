package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.lineargs.watchnext.utils.MovieUtils;
import com.lineargs.watchnext.utils.retrofit.credits.Cast;
import com.lineargs.watchnext.utils.retrofit.credits.Crew;
import com.lineargs.watchnext.utils.retrofit.movies.moviedetail.ReviewsResult;
import com.lineargs.watchnext.utils.retrofit.videos.VideosResult;

import java.util.List;

public class WatchNextRepository {

    /* Final variable for our poster and backdrop path*/
    private static final String IMAGE_SMALL_BASE = "http://image.tmdb.org/t/p/w500/";

    private MoviesDao moviesDao;
    private SeriesDao seriesDao;
    private VideosDao videosDao;
    private ReviewsDao reviewsDao;
    private CreditsDao creditsDao;
    private PersonDao personDao;
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
        creditsDao = database.creditsDao();
        personDao = database.personDao();
    }

    //Movies
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

    void insertMovie(Movies movies) {
        new InsertMoviesTask(moviesDao).execute(movies);
    }

    void updateMovie(Movies movies) {
        new UpdateMovieTask(moviesDao).execute(movies);
    }

    //Series
    LiveData<List<Series>> getPopularSeries() {
        return popularSeries;
    }

    LiveData<List<Series>> getTopRatedSeries() {
        return topRatedSeries;
    }

    LiveData<List<Series>> getOnTheAirSeries() {
        return onTheAirSeries;
    }

    public void insertSeries(Series series) {
        new InsertSeriesTask(seriesDao).execute(series);
    }

    //Favourites
    public LiveData<List<Favourites>> getFavourites() {
        return favourites;
    }

    //Videos
    public LiveData<List<Videos>> getVideos(int tmdbId) {
        return videosDao.getVideos(tmdbId);
    }

    void insertVideos(com.lineargs.watchnext.utils.retrofit.videos.Videos videos, int tmdbId) {
        new InsertVideosTask(videosDao, tmdbId).execute(videos);
    }

    //Reviews
    LiveData<List<Reviews>> getReviews(int tmdbId) {
        return reviewsDao.getReviews(tmdbId);
    }

    void insertReviews(com.lineargs.watchnext.utils.retrofit.movies.moviedetail.Reviews reviews, int tmdbId) {
        new InsertReviewsTask(reviewsDao, tmdbId).execute(reviews);
    }

    //Credits
    LiveData<List<Credits>> getCast(int tmdbId) {
        return creditsDao.getAllCast(tmdbId);
    }

    LiveData<List<Credits>> getCrew(int tmdbId) {
        return creditsDao.getAllCrew(tmdbId);
    }

    void insertCast(com.lineargs.watchnext.utils.retrofit.credits.Credits credits, int tmdbId) {
        new InsertCastTask(creditsDao, tmdbId).execute(credits);
    }

    void insertCrew(com.lineargs.watchnext.utils.retrofit.credits.Credits credits, int tmdbId) {
        new InsertCrewTask(creditsDao, tmdbId).execute(credits);
    }

    //Person
    void insertPerson(Person person) {
        new InsertPersonTask(personDao).execute(person);
    }
    LiveData<Person> getPerson(int personId) { return personDao.getPerson(personId);}

    //Movies Tasks
    private static class UpdateMovieTask extends AsyncTask<Movies, Void, Void> {

        private MoviesDao moviesDao;

        UpdateMovieTask(MoviesDao moviesDao) {
            this.moviesDao = moviesDao;
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

        InsertMoviesTask(MoviesDao moviesDao) {
            this.moviesDao = moviesDao;
        }

        @Override
        protected Void doInBackground(final Movies... movies) {
            moviesDao.insert(movies[0]);
            return null;
        }
    }

    //Series Tasks
    private static class InsertSeriesTask extends AsyncTask<Series, Void, Void> {

        private SeriesDao seriesDao;

        InsertSeriesTask(SeriesDao seriesDao) {
            this.seriesDao = seriesDao;
        }

        @Override
        protected Void doInBackground(Series... series) {
            seriesDao.insert(series[0]);
            return null;
        }
    }

    //Videos Tasks
    private static class InsertVideosTask extends AsyncTask<com.lineargs.watchnext.utils.retrofit.videos.Videos, Void, Void> {

        private VideosDao videosDao;
        private int tmdbId;

        InsertVideosTask(VideosDao videosDao, int tmdbId) {
            this.videosDao = videosDao;
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

    //Reviews Tasks
    private static class InsertReviewsTask extends AsyncTask<com.lineargs.watchnext.utils.retrofit.movies.moviedetail.Reviews, Void, Void> {

        private ReviewsDao reviewsDao;
        private int tmdbId;

        InsertReviewsTask(ReviewsDao reviewsDao, int tmdbId) {
            this.reviewsDao = reviewsDao;
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

    //Credits Tasks
    private static class InsertCastTask extends AsyncTask<com.lineargs.watchnext.utils.retrofit.credits.Credits, Void, Void> {

        private CreditsDao creditsDao;
        private int tmdbId;

        InsertCastTask(CreditsDao creditsDao, int tmdbId) {
            this.creditsDao = creditsDao;
            this.tmdbId = tmdbId;
        }

        @Override
        protected Void doInBackground(com.lineargs.watchnext.utils.retrofit.credits.Credits... credits) {
            com.lineargs.watchnext.utils.retrofit.credits.Credits tmdbCredits = credits[0];
            List<Cast> castList = tmdbCredits.getCast();
            for (Cast cast : castList) {
                Credits credit = new Credits();
                credit.setTmdbId(tmdbId);
                credit.setName(cast.getName());
                credit.setCharacterName(cast.getCharacter());
                credit.setPersonId(cast.getId());
                credit.setProfilePath(IMAGE_SMALL_BASE + cast.getProfilePath());
                credit.setType(0);
                creditsDao.insertCredits(credit);
            }
            return null;
        }
    }

    private static class InsertCrewTask extends AsyncTask<com.lineargs.watchnext.utils.retrofit.credits.Credits, Void, Void> {

        private CreditsDao creditsDao;
        private int tmdbId;

        InsertCrewTask(CreditsDao creditsDao, int tmdbId) {
            this.creditsDao = creditsDao;
            this.tmdbId = tmdbId;
        }

        @Override
        protected Void doInBackground(com.lineargs.watchnext.utils.retrofit.credits.Credits... credits) {
            com.lineargs.watchnext.utils.retrofit.credits.Credits tmdbCredits = credits[0];
            List<Crew> crewList = tmdbCredits.getCrew();
            for (Crew crew : crewList) {
                Credits credit = new Credits();
                credit.setTmdbId(tmdbId);
                credit.setName(crew.getName());
                credit.setJob(crew.getJob());
                credit.setPersonId(crew.getId());
                credit.setProfilePath(IMAGE_SMALL_BASE + crew.getProfilePath());
                credit.setType(1);
                creditsDao.insertCredits(credit);
            }
            return null;
        }
    }

    //Person Tasks
    private static class InsertPersonTask extends AsyncTask<Person, Void, Void> {

        private PersonDao personDao;

        InsertPersonTask(PersonDao personDao) {
            this.personDao = personDao;
        }

        @Override
        protected Void doInBackground(Person... people) {
            personDao.insert(people[0]);
            return null;
        }
    }
}
