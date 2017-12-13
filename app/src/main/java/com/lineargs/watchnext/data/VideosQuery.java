package com.lineargs.watchnext.data;

/**
 * Created by goranminov on 26/11/2017.
 * <p>
 * See {@link Query}
 */

public interface VideosQuery {

    String[] VIDEO_PROJECTION = new String[]{
//            DataContract.Videos._ID,
//            DataContract.Videos.COLUMN_MOVIE_ID,
//            DataContract.Videos.COLUMN_VIDEO_ID,
            DataContract.Videos.COLUMN_KEY,
            DataContract.Videos.COLUMN_NAME,
//            DataContract.Videos.COLUMN_SITE,
//            DataContract.Videos.COLUMN_TYPE,
            DataContract.Videos.COLUMN_IMG
    };

    //    int _ID = 0;
//    int MOVIE_ID = 1;
//    int VIDEO_ID = 2;
    int KEY = 0;
    int NAME = 1;
    //    int SITE = 5;
//    int TYPE = 6;
    int IMG = 2;
}
