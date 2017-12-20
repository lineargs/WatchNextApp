package com.lineargs.watchnext.data;

/**
 * Created by goranminov on 29/11/2017.
 * <p>
 * See {@link Query}
 */

public interface PersonQuery {

    /* The columns of data that we are interested in displaying. */
    String[] PERSON_PROJECTION = new String[]{
            DataContract.Person.COLUMN_PERSON_ID,
            DataContract.Person.COLUMN_NAME,
            DataContract.Person.COLUMN_BIOGRAPHY,
            DataContract.Person.COLUMN_PLACE_OF_BIRTH,
            DataContract.Person.COLUMN_PROFILE_PATH,
            DataContract.Person.COLUMN_HOMEPAGE
    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able
     * to access the data from our query. If the order of the Strings above changes, these
     * indices must be adjusted to match the order of the Strings.
     */

    int ID = 0;
    int NAME = 1;
    int BIOGRAPHY = 2;
    int PLACE_OF_BIRTH = 3;
    int PROFILE_PATH = 4;
    int HOMEPAGE = 5;
}
