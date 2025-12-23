package com.lineargs.watchnext.utils.dbutils;

import android.content.ContentValues;

import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.MovieUtils;
import com.lineargs.watchnext.utils.retrofit.movies.Result;
import com.lineargs.watchnext.utils.retrofit.movies.moviedetail.Genre;
import com.lineargs.watchnext.utils.retrofit.movies.moviedetail.MovieDetail;
import com.lineargs.watchnext.utils.retrofit.movies.moviedetail.ProductionCompany;
import com.lineargs.watchnext.utils.retrofit.movies.moviedetail.ProductionCountry;

import java.text.ParseException;
import java.util.List;

/**
 * Created by goranminov on 11/11/2017.
 * <p>
 * Utilities Helper class used for the Movies API response
 */

public class MovieDbUtils {

    /* Static variable for our poster and backdrop path*/
    private static final String IMAGE_SMALL_BASE = "https://image.tmdb.org/t/p/w500/";

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
    public static ContentValues[] getUpcomingContentValues(List<Result> results) {
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
    public static ContentValues[] getTheaterContentValues(List<Result> results) {
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
     * @param result Object used to get the values from our API response
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getSyncMovie(Result result) {

        ContentValues[] values = new ContentValues[1];
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

        values[0] = contentValues;

        return values;
    }

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param result Object used to get the values from our API response
     * @return The {@link ContentValues}
     */
    public static ContentValues updateMovie(MovieDetail result) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(DataContract.PopularMovieEntry.COLUMN_IMDB_ID, result.getImdbId());
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_RUNTIME, result.getRuntime());
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_STATUS, result.getStatus());
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COMPANIES,
                buildCompaniesString(result.getProductionCompanies()));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_PRODUCTION_COUNTRIES,
                buildCountriesString(result.getProductionCountries()));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_GENRES,
                buildGenresString(result.getGenres()));

        return contentValues;
    }

    //TODO Make all the Companies from same List type
    /**
     * Helper method used for building Companies String
     * @param companies List of type ProductionCompany
     * @return String in following format: Google, Google, Google
     */
    private static String buildCompaniesString(List<ProductionCompany> companies) {

        if (companies == null || companies.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < companies.size(); i++) {
            ProductionCompany company = companies.get(i);
            stringBuilder.append(company.getName());
            if (i + 1 < companies.size()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Helper method used for building Countries String
     * @param countries List of type ProductionCountry
     * @return String in following format: Macedonia, England, United States
     */
    private static String buildCountriesString(List<ProductionCountry> countries) {

        if (countries == null || countries.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < countries.size(); i++) {
            ProductionCountry country = countries.get(i);
            stringBuilder.append(country.getName());
            if (i + 1 < countries.size()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Helper method used for building Genres String
     * @param genres List of type Genre
     * @return String in following format: Comedy, Horror, Fantasy
     */
    private static String buildGenresString(List<Genre> genres) {

        if (genres == null || genres.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            Genre genre = genres.get(i);
            stringBuilder.append(genre.getName());
            if (i + 1 < genres.size()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}
