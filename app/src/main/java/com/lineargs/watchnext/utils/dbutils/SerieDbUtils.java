package com.lineargs.watchnext.utils.dbutils;

import android.content.ContentValues;

import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.MovieUtils;
import com.lineargs.watchnext.api.series.SeriesDetails;
import com.lineargs.watchnext.api.series.seasondetails.Episode;

import java.text.ParseException;
import java.util.List;

import static com.lineargs.watchnext.utils.Utils.buildDirectorsString;
import static com.lineargs.watchnext.utils.Utils.buildGuestStarsString;
import static com.lineargs.watchnext.utils.Utils.buildWritersString;

/**
 * Created by goranminov on 11/11/2017.
 * <p>
 * Utilities Helper class used for the Movies API response
 */

public class SerieDbUtils {

    /* Static variable for our poster and backdrop path*/
    private static final String IMAGE_MEDIUM_BASE = "https://image.tmdb.org/t/p/w500";

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param seriesDetail List used to get the values from our API response
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getPopularContentValues(List<SeriesDetails> seriesDetail) {
        int i = 0;
        ContentValues[] values = new ContentValues[seriesDetail.size()];
        for (SeriesDetails seriesDetails : seriesDetail) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID, seriesDetails.getId());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_TITLE, seriesDetails.getName());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_OVERVIEW, seriesDetails.getOverview());
            try {
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE, MovieUtils.getNormalizedReleaseDate(seriesDetails.getFirstAirDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE, MovieUtils.getNormalizedVoteAverage(String.valueOf(seriesDetails.getVoteAverage())));
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_POSTER_PATH, IMAGE_MEDIUM_BASE + seriesDetails.getPosterPath());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH, IMAGE_MEDIUM_BASE + seriesDetails.getBackdropPath());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE, seriesDetails.getOriginalLanguage());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE, seriesDetails.getOriginalName());

            values[i] = contentValues;
            i++;
        }

        return values;
    }

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param seriesDetail List used to get the values from our API response
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getTopContentValues(List<SeriesDetails> seriesDetail) {
        int i = 0;
        ContentValues[] values = new ContentValues[seriesDetail.size()];
        for (SeriesDetails seriesDetails : seriesDetail) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID, seriesDetails.getId());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_TITLE, seriesDetails.getName());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_OVERVIEW, seriesDetails.getOverview());
            try {
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE, MovieUtils.getNormalizedReleaseDate(seriesDetails.getFirstAirDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE, MovieUtils.getNormalizedVoteAverage(String.valueOf(seriesDetails.getVoteAverage())));
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_POSTER_PATH, IMAGE_MEDIUM_BASE + seriesDetails.getPosterPath());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH, IMAGE_MEDIUM_BASE + seriesDetails.getBackdropPath());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE, seriesDetails.getOriginalLanguage());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE, seriesDetails.getOriginalName());

            values[i] = contentValues;
            i++;
        }

        return values;
    }

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param seriesDetail List used to get the values from our API response
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getOnTheAirContentValues(List<SeriesDetails> seriesDetail) {
        int i = 0;
        ContentValues[] values = new ContentValues[seriesDetail.size()];
        for (SeriesDetails seriesDetails : seriesDetail) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID, seriesDetails.getId());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_TITLE, seriesDetails.getName());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_OVERVIEW, seriesDetails.getOverview());
            try {
                contentValues.put(DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE, MovieUtils.getNormalizedReleaseDate(seriesDetails.getFirstAirDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE, MovieUtils.getNormalizedVoteAverage(String.valueOf(seriesDetails.getVoteAverage())));
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_POSTER_PATH, IMAGE_MEDIUM_BASE + seriesDetails.getPosterPath());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH, IMAGE_MEDIUM_BASE + seriesDetails.getBackdropPath());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE, seriesDetails.getOriginalLanguage());
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE, seriesDetails.getOriginalName());

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
    public static ContentValues[] getSyncTV(SeriesDetails result) {

        ContentValues[] values = new ContentValues[1];
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataContract.PopularMovieEntry.COLUMN_MOVIE_ID, result.getId());
        contentValues.put(DataContract.Favorites.COLUMN_TYPE, 1);
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_TITLE, result.getName());
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_OVERVIEW, result.getOverview());
        try {
            contentValues.put(DataContract.PopularMovieEntry.COLUMN_RELEASE_DATE, MovieUtils.getNormalizedReleaseDate(result.getFirstAirDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE, MovieUtils.getNormalizedVoteAverage(String.valueOf(result.getVoteAverage())));
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_POSTER_PATH, IMAGE_MEDIUM_BASE + result.getPosterPath());
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_BACKDROP_PATH, IMAGE_MEDIUM_BASE + result.getBackdropPath());
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_LANGUAGE, result.getOriginalLanguage());
        contentValues.put(DataContract.PopularMovieEntry.COLUMN_ORIGINAL_TITLE, result.getOriginalName());

        values[0] = contentValues;

        return values;
    }

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param episodes List used to get the values from our API response
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getEpisodes(List<Episode> episodes, String seasonId) {
        int i = 0;
        ContentValues[] values = new ContentValues[episodes.size()];
        for (Episode episode : episodes) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(DataContract.Episodes.COLUMN_EPISODE_ID, episode.getId());
            contentValues.put(DataContract.Episodes.COLUMN_SEASON_ID, seasonId);
            contentValues.put(DataContract.Episodes.COLUMN_SEASON_NUMBER, episode.getSeasonNumber());
            if (episode.getAirDate() != null) {
                try {
                    contentValues.put(DataContract.Episodes.COLUMN_RELEASE_DATE, MovieUtils.getNormalizedReleaseDate(episode.getAirDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            contentValues.put(DataContract.Episodes.COLUMN_STILL_PATH, IMAGE_MEDIUM_BASE + episode.getStillPath());
            contentValues.put(DataContract.Episodes.COLUMN_EPISODE_NUMBER, episode.getEpisodeNumber());
            contentValues.put(DataContract.Episodes.COLUMN_NAME, episode.getName());
            contentValues.put(DataContract.Episodes.COLUMN_OVERVIEW, episode.getOverview());
            contentValues.put(DataContract.Episodes.COLUMN_VOTE_AVERAGE, MovieUtils.getNormalizedVoteAverage(String.valueOf(episode.getVoteAverage())));
            contentValues.put(DataContract.Episodes.COLUMN_VOTE_COUNT, episode.getVoteCount());
            contentValues.put(DataContract.Episodes.COLUMN_GUEST_STARS, buildGuestStarsString(episode.getGuestStars()));
            contentValues.put(DataContract.Episodes.COLUMN_DIRECTORS, buildDirectorsString(episode.getCrew()));
            contentValues.put(DataContract.Episodes.COLUMN_WRITERS, buildWritersString(episode.getCrew()));
            values[i] = contentValues;
            i++;
        }

        return values;
    }
}
