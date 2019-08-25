package com.lineargs.watchnext.workers;

import android.os.AsyncTask;

import com.lineargs.watchnext.api.movies.Result;
import com.lineargs.watchnext.data.WatchNextDatabase;
import com.lineargs.watchnext.utils.MovieUtils;

import java.text.ParseException;
import java.util.List;

import static com.lineargs.watchnext.utils.Constants.IMAGE_SMALL_BASE;

class WorkerUtils {

    private WorkerUtils() {
    }

    static class InsertPopularMovies extends AsyncTask<List<com.lineargs.watchnext.api.movies.Result>, Void, Void> {

        private final WatchNextDatabase database;

        InsertPopularMovies(WatchNextDatabase database) {
            this.database = database;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<com.lineargs.watchnext.api.movies.Result>... lists) {
            List<com.lineargs.watchnext.api.movies.Result> resultList = lists[0];
            com.lineargs.watchnext.data.Movies movies = new com.lineargs.watchnext.data.Movies();
            for (com.lineargs.watchnext.api.movies.Result result : resultList) {
                movies.setTmdbId(result.getId());
                movies.setBackdropPath(IMAGE_SMALL_BASE + result.getBackdropPath());
                movies.setPosterPath(IMAGE_SMALL_BASE + result.getPosterPath());
                movies.setVoteAverage(MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
                movies.setTitle(result.getTitle());
                movies.setOverview(result.getOverview());
                try {
                    movies.setReleaseDate(MovieUtils.getNormalizedReleaseDate(result.getReleaseDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                movies.setType(0);
                database.moviesDao().insert(movies);
            }
            return null;
        }
    }

    static class InsertTopMovies extends AsyncTask<List<com.lineargs.watchnext.api.movies.Result>, Void, Void> {

        private final WatchNextDatabase database;

        InsertTopMovies(WatchNextDatabase database) {
            this.database = database;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<com.lineargs.watchnext.api.movies.Result>... lists) {
            List<com.lineargs.watchnext.api.movies.Result> resultList = lists[0];
            com.lineargs.watchnext.data.Movies movies = new com.lineargs.watchnext.data.Movies();
            for (com.lineargs.watchnext.api.movies.Result result : resultList) {
                movies.setTmdbId(result.getId());
                movies.setBackdropPath(IMAGE_SMALL_BASE + result.getBackdropPath());
                movies.setPosterPath(IMAGE_SMALL_BASE + result.getPosterPath());
                movies.setVoteAverage(MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
                movies.setTitle(result.getTitle());
                movies.setOverview(result.getOverview());
                try {
                    movies.setReleaseDate(MovieUtils.getNormalizedReleaseDate(result.getReleaseDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                movies.setType(1);
                database.moviesDao().insert(movies);
            }
            return null;
        }
    }

    static class InsertUpcomingMovies extends AsyncTask<List<com.lineargs.watchnext.api.movies.Result>, Void, Void> {

        private final WatchNextDatabase database;

        InsertUpcomingMovies(WatchNextDatabase database) {
            this.database = database;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<com.lineargs.watchnext.api.movies.Result>... lists) {
            List<com.lineargs.watchnext.api.movies.Result> resultList = lists[0];
            com.lineargs.watchnext.data.Movies movies = new com.lineargs.watchnext.data.Movies();
            for (com.lineargs.watchnext.api.movies.Result result : resultList) {
                movies.setTmdbId(result.getId());
                movies.setBackdropPath(IMAGE_SMALL_BASE + result.getBackdropPath());
                movies.setPosterPath(IMAGE_SMALL_BASE + result.getPosterPath());
                movies.setVoteAverage(MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
                movies.setTitle(result.getTitle());
                movies.setOverview(result.getOverview());
                try {
                    movies.setReleaseDate(MovieUtils.getNormalizedReleaseDate(result.getReleaseDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                movies.setType(2);
                database.moviesDao().insert(movies);
            }
            return null;
        }
    }

    static class InsertTheaterMovies extends AsyncTask<List<Result>, Void, Void> {

        private final WatchNextDatabase database;

        InsertTheaterMovies(WatchNextDatabase database) {
            this.database = database;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<com.lineargs.watchnext.api.movies.Result>... lists) {
            List<com.lineargs.watchnext.api.movies.Result> resultList = lists[0];
            com.lineargs.watchnext.data.Movies movies = new com.lineargs.watchnext.data.Movies();
            for (com.lineargs.watchnext.api.movies.Result result : resultList) {
                movies.setTmdbId(result.getId());
                movies.setBackdropPath(IMAGE_SMALL_BASE + result.getBackdropPath());
                movies.setPosterPath(IMAGE_SMALL_BASE + result.getPosterPath());
                movies.setVoteAverage(MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
                movies.setTitle(result.getTitle());
                movies.setOverview(result.getOverview());
                try {
                    movies.setReleaseDate(MovieUtils.getNormalizedReleaseDate(result.getReleaseDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                movies.setType(3);
                database.moviesDao().insert(movies);
            }
            return null;
        }
    }
}
