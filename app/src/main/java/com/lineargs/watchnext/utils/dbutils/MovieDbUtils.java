package com.lineargs.watchnext.utils.dbutils;

import android.content.ContentValues;

import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.MovieUtils;
import com.lineargs.watchnext.utils.retrofit.movies.Result;

import java.text.ParseException;
import java.util.List;

/**
 * Created by goranminov on 11/11/2017.
 * <p>
 * Utilities Helper class used for the Movies API response
 */

public class MovieDbUtils {

    /* Static variable for our poster and backdrop path*/
    private static final String IMAGE_SMALL_BASE = "http://image.tmdb.org/t/p/w370/";

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param results List used to get the values from our API response
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getPopularContentValues(List<Result> results) {
        int i = 0;
        ContentValues[] values = new ContentValues[results.size()];
        for (Result result : results) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID, result.getId());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_TITLE, result.getTitle());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_OVERVIEW, result.getOverview());
            try {
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE, MovieUtils.getNormalizedReleaseDate(result.getReleaseDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE, MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_POSTER_PATH, IMAGE_SMALL_BASE + result.getPosterPath());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH, IMAGE_SMALL_BASE + result.getBackdropPath());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE, result.getOriginalLanguage());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE, result.getOriginalTitle());

            values[i] = contentValues;
            i++;
        }

        return values;
    }

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param results List used to get the values from our API response
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getTopContentValues(List<Result> results) {
        int i = 0;
        ContentValues[] values = new ContentValues[results.size()];
        for (Result result : results) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataContract.TopRatedMovieEntry.COLUMN_MOVIE_ID, result.getId());
            contentValues.put(DataContract.TopRatedMovieEntry.COLUMN_TITLE, result.getTitle());
            contentValues.put(DataContract.TopRatedMovieEntry.COLUMN_OVERVIEW, result.getOverview());
            try {
                contentValues.put(DataContract.TopRatedMovieEntry.COLUMN_RELEASE_DATE, MovieUtils.getNormalizedReleaseDate(result.getReleaseDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            contentValues.put(DataContract.TopRatedMovieEntry.COLUMN_VOTE_AVERAGE, MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
            contentValues.put(DataContract.TopRatedMovieEntry.COLUMN_POSTER_PATH, IMAGE_SMALL_BASE + result.getPosterPath());
            contentValues.put(DataContract.TopRatedMovieEntry.COLUMN_BACKDROP_PATH, IMAGE_SMALL_BASE + result.getBackdropPath());
            contentValues.put(DataContract.TopRatedMovieEntry.COLUMN_ORIGINAL_LANGUAGE, result.getOriginalLanguage());
            contentValues.put(DataContract.TopRatedMovieEntry.COLUMN_ORIGINAL_TITLE, result.getOriginalTitle());

            values[i] = contentValues;
            i++;
        }

        return values;
    }

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param results List used to get the values from our API response
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getUpcomingContentValues(List<Result> results) {
        int i = 0;
        ContentValues[] values = new ContentValues[results.size()];
        for (Result result : results) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataContract.UpcomingMovieEntry.COLUMN_MOVIE_ID, result.getId());
            contentValues.put(DataContract.UpcomingMovieEntry.COLUMN_TITLE, result.getTitle());
            contentValues.put(DataContract.UpcomingMovieEntry.COLUMN_OVERVIEW, result.getOverview());
            try {
                contentValues.put(DataContract.UpcomingMovieEntry.COLUMN_RELEASE_DATE, MovieUtils.getNormalizedReleaseDate(result.getReleaseDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            contentValues.put(DataContract.UpcomingMovieEntry.COLUMN_VOTE_AVERAGE, MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
            contentValues.put(DataContract.UpcomingMovieEntry.COLUMN_POSTER_PATH, IMAGE_SMALL_BASE + result.getPosterPath());
            contentValues.put(DataContract.UpcomingMovieEntry.COLUMN_BACKDROP_PATH, IMAGE_SMALL_BASE + result.getBackdropPath());
            contentValues.put(DataContract.UpcomingMovieEntry.COLUMN_ORIGINAL_LANGUAGE, result.getOriginalLanguage());
            contentValues.put(DataContract.UpcomingMovieEntry.COLUMN_ORIGINAL_TITLE, result.getOriginalTitle());

            values[i] = contentValues;
            i++;
        }

        return values;
    }

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param results List used to get the values from our API response
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getTheaterContentValues(List<Result> results) {
        int i = 0;
        ContentValues[] values = new ContentValues[results.size()];
        for (Result result : results) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataContract.TheaterMovieEntry.COLUMN_MOVIE_ID, result.getId());
            contentValues.put(DataContract.TheaterMovieEntry.COLUMN_TITLE, result.getTitle());
            contentValues.put(DataContract.TheaterMovieEntry.COLUMN_OVERVIEW, result.getOverview());
            try {
                contentValues.put(DataContract.TheaterMovieEntry.COLUMN_RELEASE_DATE, MovieUtils.getNormalizedReleaseDate(result.getReleaseDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            contentValues.put(DataContract.TheaterMovieEntry.COLUMN_VOTE_AVERAGE, MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
            contentValues.put(DataContract.TheaterMovieEntry.COLUMN_POSTER_PATH, IMAGE_SMALL_BASE + result.getPosterPath());
            contentValues.put(DataContract.TheaterMovieEntry.COLUMN_BACKDROP_PATH, IMAGE_SMALL_BASE + result.getBackdropPath());
            contentValues.put(DataContract.TheaterMovieEntry.COLUMN_ORIGINAL_LANGUAGE, result.getOriginalLanguage());
            contentValues.put(DataContract.TheaterMovieEntry.COLUMN_ORIGINAL_TITLE, result.getOriginalTitle());

            values[i] = contentValues;
            i++;
        }

        return values;
    }

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param result Object used to get the values from our API response
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getSyncMovie(Result result) {

        ContentValues[] values = new ContentValues[1];
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataContract.Favorites.COLUMN_FAV_ID, result.getId());
        contentValues.put(DataContract.Favorites.COLUMN_TITLE, result.getTitle());
        contentValues.put(DataContract.Favorites.COLUMN_OVERVIEW, result.getOverview());
        try {
            contentValues.put(DataContract.Favorites.COLUMN_RELEASE_DATE, MovieUtils.getNormalizedReleaseDate(result.getReleaseDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        contentValues.put(DataContract.Favorites.COLUMN_VOTE_AVERAGE, MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
        contentValues.put(DataContract.Favorites.COLUMN_POSTER_PATH, IMAGE_SMALL_BASE + result.getPosterPath());
        contentValues.put(DataContract.Favorites.COLUMN_BACKDROP_PATH, IMAGE_SMALL_BASE + result.getBackdropPath());
        contentValues.put(DataContract.Favorites.COLUMN_ORIGINAL_LANGUAGE, result.getOriginalLanguage());
        contentValues.put(DataContract.Favorites.COLUMN_ORIGINAL_TITLE, result.getOriginalTitle());

        values[0] = contentValues;

        return values;
    }
}
