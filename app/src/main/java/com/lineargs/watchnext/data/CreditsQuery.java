package com.lineargs.watchnext.data;

/**
 * Created by goranminov on 23/11/2017.
 * <p>
 * See {@link Query}
 */

public interface CreditsQuery {

    String[] CREDITS_PROJECTION = new String[]{
            DataContract.Credits.COLUMN_PERSON_ID,
            DataContract.Credits.COLUMN_NAME,
            DataContract.Credits.COLUMN_CHARACTER_NAME,
            DataContract.Credits.COLUMN_PROFILE_PATH,
            DataContract.Credits.COLUMN_JOB,
            DataContract.Credits.COLUMN_TYPE
    };

    int PERSON_ID = 0;
    int NAME = 1;
    int CHARACTER_NAME = 2;
    int PROFILE_PATH = 3;
    int JOB = 4;
    int TYPE = 5;
}
