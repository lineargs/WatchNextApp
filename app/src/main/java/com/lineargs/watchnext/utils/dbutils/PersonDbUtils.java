package com.lineargs.watchnext.utils.dbutils;

import android.content.ContentValues;

import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.MovieUtils;
import com.lineargs.watchnext.utils.retrofit.people.Person;

import java.text.ParseException;

/**
 * Created by goranminov on 29/11/2017.
 * <p>
 * Utilities Helper class used for the Person API response
 */

public class PersonDbUtils {

    /* Static variables */
    private static final String IMAGE_MEDIUM_BASE = "https://image.tmdb.org/t/p/w500/";
    private static final String IMAGE_SMALL_BASE = "https://image.tmdb.org/t/p/w370/";

    /**
     * Builds ContentValues[] used for our ContentResolver
     *
     * @param person Object used to get the values from our API response
     * @return The {@link ContentValues}
     */
    public static ContentValues[] getPersonContentValues(Person person) {
        int i = 0;
        ContentValues[] values = new ContentValues[1];
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataContract.Person.COLUMN_PERSON_ID, person.getId());
        if (person.getBirthday() != null) {
            try {
                contentValues.put(DataContract.Person.COLUMN_BIRTHDAY, MovieUtils.getNormalizedReleaseDate(person.getBirthday()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        contentValues.put(DataContract.Person.COLUMN_NAME, person.getName());
        contentValues.put(DataContract.Person.COLUMN_BIOGRAPHY, person.getBiography());
        contentValues.put(DataContract.Person.COLUMN_PLACE_OF_BIRTH, person.getPlaceOfBirth());
        contentValues.put(DataContract.Person.COLUMN_PROFILE_PATH, IMAGE_MEDIUM_BASE + person.getProfilePath());
        contentValues.put(DataContract.Person.COLUMN_HOMEPAGE, person.getHomepage());

        values[0] = contentValues;

        return values;
    }
}
