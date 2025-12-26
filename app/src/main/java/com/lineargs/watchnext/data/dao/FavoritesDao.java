package com.lineargs.watchnext.data.dao;

import android.database.Cursor;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.lineargs.watchnext.data.entity.Favorites;

@Dao
public interface FavoritesDao {

    @Query("SELECT * FROM favorites")
    Cursor getFavorites();

    @Query("SELECT * FROM favorites")
    androidx.lifecycle.LiveData<java.util.List<Favorites>> getFavoritesLiveData();

    @Query("SELECT * FROM favorites WHERE movie_id = :id")
    Cursor getFavorite(int id);

    @Query("SELECT * FROM favorites WHERE movie_id = :id")
    androidx.lifecycle.LiveData<Favorites> getFavoriteLiveData(int id);

    @Query("SELECT movie_id FROM favorites WHERE type = 0")
    androidx.lifecycle.LiveData<java.util.List<Integer>> getFavoriteMovieIds();

    @Query("SELECT movie_id FROM favorites WHERE type = 1")
    androidx.lifecycle.LiveData<java.util.List<Integer>> getFavoriteSeriesIds();

    @Query("SELECT COUNT(*) FROM favorites WHERE type = 0")
    androidx.lifecycle.LiveData<Integer> getMoviesCountLiveData();

    @Query("SELECT COUNT(*) FROM favorites WHERE type = 1")
    androidx.lifecycle.LiveData<Integer> getSeriesCountLiveData();

    @Query("SELECT * FROM favorites WHERE type = 1 AND notify = 1")
    java.util.List<Favorites> getSubscribedSeries();

    @Query("UPDATE favorites SET notify = :notify WHERE movie_id = :id")
    int updateNotifyStatus(int id, int notify);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFavorite(Favorites favorite);

    @Query("DELETE FROM favorites WHERE movie_id = :id")
    int deleteFavorite(int id);
}
