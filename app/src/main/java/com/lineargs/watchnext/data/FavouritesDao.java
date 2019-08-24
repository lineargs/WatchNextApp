package com.lineargs.watchnext.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavouritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Favourites favourites);

    @Query("SELECT * FROM favourites ORDER BY id ASC")
    LiveData<List<Favourites>> getAllFavourites();
}
