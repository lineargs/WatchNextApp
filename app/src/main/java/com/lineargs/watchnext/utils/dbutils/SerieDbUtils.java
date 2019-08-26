package com.lineargs.watchnext.utils.dbutils;

import android.content.ContentValues;

import com.lineargs.watchnext.api.series.seasondetails.Episode;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.MovieUtils;

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
