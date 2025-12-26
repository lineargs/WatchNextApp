package com.lineargs.watchnext.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.lineargs.watchnext.data.entity.UpcomingEpisodes;

import java.util.List;

@Dao
public interface UpcomingEpisodesDao {

    @Query("SELECT * FROM upcoming_episodes ORDER BY air_date ASC")
    LiveData<List<UpcomingEpisodes>> getUpcomingEpisodes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUpcomingEpisode(UpcomingEpisodes episode);

    @Query("DELETE FROM upcoming_episodes WHERE series_id NOT IN (SELECT movie_id FROM favorites WHERE type = 1 AND notify = 1)")
    void deleteUnsubscribedEpisodes();

    @Query("SELECT COUNT(*) FROM upcoming_episodes WHERE poster_path IS NULL OR season_id = 0")
    int countUpcomingEpisodesWithoutPosters();

    @Query("DELETE FROM upcoming_episodes WHERE series_id = :seriesId")
    void deleteBySeriesId(int seriesId);

    @Query("DELETE FROM upcoming_episodes")
    void deleteAll();
}
