package com.lineargs.watchnext.data;

/**
 * Created by goranminov on 23/11/2017.
 * <p>
 * See {@link Query}
 */

public interface CreditsQuery {

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


    String[] CREW_PROJECTION = new String[]{
            DataContract.CreditCrew._ID,
            DataContract.CreditCrew.COLUMN_MOVIE_ID,
            DataContract.CreditCrew.COLUMN_CREW_ID,
            DataContract.CreditCrew.COLUMN_NAME,
            DataContract.CreditCrew.COLUMN_CREDIT_ID,
            DataContract.CreditCrew.COLUMN_PROFILE_PATH,
            DataContract.CreditCrew.COLUMN_DEPARTMENT,
            DataContract.CreditCrew.COLUMN_JOB
    };

     int CREW_ID = 2;
     int CREDIT_ID = 4;
     int DEPARTMENT = 5;
     int JOB = 6;
}
