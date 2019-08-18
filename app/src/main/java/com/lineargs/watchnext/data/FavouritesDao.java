package com.lineargs.watchnext.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavouritesDao {

    @Query("SELECT * FROM favourites ORDER BY id ASC")
    LiveData<List<Favourites>> getAllFavourites();
}
