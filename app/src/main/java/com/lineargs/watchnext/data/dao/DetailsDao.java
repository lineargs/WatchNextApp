package com.lineargs.watchnext.data.dao;

import android.database.Cursor;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.lineargs.watchnext.data.entity.Review;
import com.lineargs.watchnext.data.entity.Videos;
import com.lineargs.watchnext.data.entity.Seasons;
import com.lineargs.watchnext.data.entity.Episodes;
import com.lineargs.watchnext.data.entity.Person;

@Dao
public interface DetailsDao {

    // Reviews
    @Query("SELECT * FROM review WHERE movie_id = :movieId")
    Cursor getReviews(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertReview(Review review);

    // Videos
    @Query("SELECT * FROM videos WHERE movie_id = :movieId")
    Cursor getVideos(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertVideo(Videos video);

    // Seasons
    @Query("SELECT * FROM seasons WHERE movie_id = :serieId")
    Cursor getSeasons(int serieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSeason(Seasons season);

    // Episodes
    @Query("SELECT * FROM episodes WHERE episode_id = :episodeId")
    Cursor getEpisode(int episodeId);

    @Query("SELECT * FROM episodes")
    Cursor getEpisodes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertEpisode(Episodes episode);

    // Person
    @Query("SELECT * FROM person WHERE person_id = :personId")
    Cursor getPerson(int personId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPerson(Person person);
}
