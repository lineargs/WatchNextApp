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

    @Query("SELECT * FROM review WHERE movie_id = :movieId")
    androidx.lifecycle.LiveData<java.util.List<Review>> getReviewsLiveData(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertReview(Review review);

    // Videos
    @Query("SELECT * FROM videos WHERE movie_id = :movieId")
    Cursor getVideos(int movieId);

    @Query("SELECT * FROM videos WHERE movie_id = :movieId")
    androidx.lifecycle.LiveData<java.util.List<Videos>> getVideosLiveData(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertVideo(Videos video);

    // Seasons
    @Query("SELECT * FROM seasons WHERE movie_id = :serieId")
    Cursor getSeasons(int serieId);

    @Query("SELECT * FROM seasons WHERE movie_id = :serieId")
    androidx.lifecycle.LiveData<java.util.List<Seasons>> getSeasonsLiveData(int serieId);

    @Query("SELECT * FROM seasons WHERE season_id = :seasonId")
    androidx.lifecycle.LiveData<Seasons> getSeasonLiveData(String seasonId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSeason(Seasons season);

    // Episodes
    @Query("SELECT * FROM episodes WHERE episode_id = :episodeId")
    Cursor getEpisode(int episodeId);

    @Query("SELECT * FROM episodes WHERE episode_id = :episodeId")
    androidx.lifecycle.LiveData<Episodes> getEpisodeLiveData(int episodeId);

    @Query("SELECT * FROM episodes WHERE season_id = :seasonId")
    androidx.lifecycle.LiveData<java.util.List<Episodes>> getEpisodesForSeasonLiveData(String seasonId);

    @Query("SELECT * FROM episodes")
    Cursor getEpisodes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertEpisode(Episodes episode);

    // Person
    @Query("SELECT * FROM person WHERE person_id = :personId")
    Cursor getPerson(int personId);

    @Query("SELECT * FROM person WHERE person_id = :personId")
    androidx.lifecycle.LiveData<Person> getPersonLiveData(int personId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPerson(Person person);
}
