package com.lineargs.watchnext.data;

/**
 * Created by goranminov on 28/11/2017.
 * <p>
 * See {@link Query}
 */

public interface EpisodesQuery {
    String[] EPISODE_PROJECTION = new String[]{
//            DataContract.Episodes._ID,
//            DataContract.Episodes.COLUMN_SEASON_ID,
            DataContract.Episodes.COLUMN_EPISODE_ID,
//            DataContract.Episodes.COLUMN_EPISODE_NUMBER,
            DataContract.Episodes.COLUMN_RELEASE_DATE,
            DataContract.Episodes.COLUMN_NAME,
//            DataContract.Episodes.COLUMN_SEASON_NUMBER,
            DataContract.Episodes.COLUMN_OVERVIEW,
            DataContract.Episodes.COLUMN_STILL_PATH,
            DataContract.Episodes.COLUMN_VOTE_AVERAGE,
//            DataContract.Episodes.COLUMN_VOTE_COUNT
    };

    //    int _ID = 0;
//    int SEASON_ID = 0;
    int EPISODE_ID = 0;
    //    int EPISODE_NUMBER = 3;
    int RELEASE_DATE = 1;
    int NAME = 2;
    //    int SEASON_NUMBER = 6;
    int OVERVIEW = 3;
    int STILL_PATH = 4;
    int VOTE_AVERAGE = 5;
//    int VOTE_COUNT = 10;
}
