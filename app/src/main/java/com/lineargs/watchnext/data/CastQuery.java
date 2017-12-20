package com.lineargs.watchnext.data;

/**
 * Created by goranminov on 23/11/2017.
 * <p>
 * See {@link Query}
 */

public interface CastQuery {

    String[] CAST_PROJECTION = new String[]{
            DataContract.CreditCast.COLUMN_PERSON_ID,
            DataContract.CreditCast.COLUMN_NAME,
            DataContract.CreditCast.COLUMN_CHARACTER_NAME,
            DataContract.CreditCast.COLUMN_PROFILE_PATH
    };

    int PERSON_ID = 0;
    int NAME = 1;
    int CHARACTER_NAME = 2;
    int PROFILE_PATH = 3;
}
