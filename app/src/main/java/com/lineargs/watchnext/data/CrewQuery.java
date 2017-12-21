package com.lineargs.watchnext.data;

/**
 * Created by goranminov on 20/12/2017.
 */

public interface CrewQuery {

    String[] CREW_PROJECTION = new String[]{
            DataContract.CreditCrew.COLUMN_NAME,
            DataContract.CreditCrew.COLUMN_PROFILE_PATH,
            DataContract.CreditCrew.COLUMN_JOB,
            DataContract.CreditCrew.COLUMN_CREW_ID
    };

    int CREW_NAME = 0;
    int PROFILE_PATH = 1;
    int CREW_JOB = 2;
    int CREW_ID = 3;
}
