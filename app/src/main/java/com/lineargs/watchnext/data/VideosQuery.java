package com.lineargs.watchnext.data;

/**
 * Created by goranminov on 26/11/2017.
 * <p>
 * See {@link Query}
 */

public interface VideosQuery {

    String[] VIDEO_PROJECTION = new String[]{
            DataContract.Videos.COLUMN_KEY,
            DataContract.Videos.COLUMN_NAME,
            DataContract.Videos.COLUMN_IMG
    };

    int KEY = 0;
    int NAME = 1;
    int IMG = 2;
}
