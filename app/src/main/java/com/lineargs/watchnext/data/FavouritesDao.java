package com.lineargs.watchnext.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavouritesDao {

    @Query("SELECT * FROM favourites ORDER BY id ASC")
    LiveData<List<Favourites>> getAllFavourites();
}
