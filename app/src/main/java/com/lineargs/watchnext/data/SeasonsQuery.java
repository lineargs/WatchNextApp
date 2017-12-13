package com.lineargs.watchnext.data;

/**
 * Created by goranminov on 27/11/2017.
 * <p>
 * See {@link Query}
 */

public interface SeasonsQuery {

    String[] SEASON_PROJECTION = new String[]{
            DataContract.Seasons._ID,
            DataContract.Seasons.COLUMN_SEASON_ID,
            DataContract.Seasons.COLUMN_SERIE_ID,
            DataContract.Seasons.COLUMN_RELEASE_DATE,
            DataContract.Seasons.COLUMN_POSTER_PATH,
            DataContract.Seasons.COLUMN_SEASON_NUMBER,
            DataContract.Seasons.COLUMN_EPISODE_COUNT,
            DataContract.Seasons.COLUMN_SHOW_NAME
    };

    int _ID = 0;
    int SEASON_ID = 1;
    int SERIE_ID = 2;
    int RELEASE_DATE = 3;
    int POSTER_PATH = 4;
    int SEASON_NUMBER = 5;
    int EPISODE_COUNT = 6;
    int SHOW_NAME = 7;
}
