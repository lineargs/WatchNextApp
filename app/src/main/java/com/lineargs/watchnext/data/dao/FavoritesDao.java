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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFavorite(Favorites favorite);

    @Query("DELETE FROM favorites WHERE movie_id = :id")
    int deleteFavorite(int id);
}
