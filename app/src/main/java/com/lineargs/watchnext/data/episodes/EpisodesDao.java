package com.lineargs.watchnext.data.episodes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EpisodesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEpisodes(Episodes episodes);

    @Query("SELECT * FROM episodes WHERE :seasonId LIKE season_id ORDER BY episode_number ASC")
    LiveData<List<Episodes>> getSeasonEpisodes(int seasonId);
}
