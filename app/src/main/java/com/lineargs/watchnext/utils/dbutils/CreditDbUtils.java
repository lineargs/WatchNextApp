package com.lineargs.watchnext.utils.dbutils;

import android.content.ContentValues;

import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.retrofit.credits.Cast;
import com.lineargs.watchnext.utils.retrofit.credits.Credits;
import com.lineargs.watchnext.utils.retrofit.credits.Crew;

import java.util.List;

/**
 * Created by goranminov on 11/11/2017.
 * <p>
 * Utilities Helper class used for the Credits API response
 */

public class CreditDbUtils {

    /* Final variable for our poster and backdrop path*/
    private static final String IMAGE_SMALL_BASE = "https://image.tmdb.org/t/p/w500/";

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param credits List used to get the values from our API response
     * @param id      The ID of the movie / serie
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getCastContentValues(Credits credits, String id) {
        List<Cast> casts = credits.getCast();
        int i = 0;
        ContentValues[] values = new ContentValues[casts.size()];

        for (Cast cast : casts) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataContract.Credits.COLUMN_MOVIE_ID, id);
            contentValues.put(DataContract.Credits.COLUMN_CHARACTER_NAME, cast.getCharacter());
            contentValues.put(DataContract.Credits.COLUMN_NAME, cast.getName());
            contentValues.put(DataContract.Credits.COLUMN_PERSON_ID, cast.getId());
            contentValues.put(DataContract.Credits.COLUMN_PROFILE_PATH, IMAGE_SMALL_BASE + String.valueOf(cast.getProfilePath()));
            values[i] = contentValues;
            i++;
        }

        return values;
    }

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param credits List used to get the values from our API response
     * @param id      The ID of the movie / serie
     * @return The {@link ContentValues}
     */
    @SuppressWarnings("unused")
    public static ContentValues[] getCrewContentValues(Credits credits, String id) {
        List<Crew> crews = credits.getCrew();
        int i = 0;
        ContentValues[] values = new ContentValues[crews.size()];

        for (Crew crew : crews) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataContract.Credits.COLUMN_MOVIE_ID, id);
            contentValues.put(DataContract.Credits.COLUMN_NAME, crew.getName());
            contentValues.put(DataContract.Credits.COLUMN_PERSON_ID, crew.getId());
            contentValues.put(DataContract.Credits.COLUMN_PROFILE_PATH, IMAGE_SMALL_BASE + String.valueOf(crew.getProfilePath()));
            contentValues.put(DataContract.Credits.COLUMN_JOB, crew.getJob());
            contentValues.put(DataContract.Credits.COLUMN_TYPE, 1);
            values[i] = contentValues;
            i++;
        }

        return values;
    }
}
