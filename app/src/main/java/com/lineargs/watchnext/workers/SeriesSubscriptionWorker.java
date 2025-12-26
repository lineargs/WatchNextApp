package com.lineargs.watchnext.workers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.WatchNextDatabase;
import com.lineargs.watchnext.data.entity.Favorites;
import com.lineargs.watchnext.data.entity.UpcomingEpisodes;
import com.lineargs.watchnext.jobs.WorkManagerUtils;
import com.lineargs.watchnext.utils.ReminderUtils;
import com.lineargs.watchnext.utils.retrofit.series.SeriesApiService;
import com.lineargs.watchnext.utils.retrofit.series.seriesdetails.SeriesDetails;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeriesSubscriptionWorker extends Worker {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public SeriesSubscriptionWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        WatchNextDatabase database = WatchNextDatabase.getDatabase(getApplicationContext());
        List<Favorites> subscribedSeries = database.favoritesDao().getSubscribedSeries();

        // Cleanup episodes for series no longer subscribed
        database.upcomingEpisodesDao().deleteUnsubscribedEpisodes();

        if (subscribedSeries == null || subscribedSeries.isEmpty()) {
            return Result.success();
        }

        SeriesApiService service = retrofit.create(SeriesApiService.class);

        for (Favorites series : subscribedSeries) {
            try {
                Response<SeriesDetails> response = service.getDetails(
                        String.valueOf(series.getTmdbId()),
                        BuildConfig.MOVIE_DATABASE_API_KEY,
                        null
                ).execute();

                if (response.isSuccessful() && response.body() != null) {
                    SeriesDetails details = response.body();
                    if (details.getNextEpisodeToAir() != null) {
                        String airDate = details.getNextEpisodeToAir().getAirDate();
                        int delaySeconds = ReminderUtils.getReminderDelayInSeconds(airDate);

                        if (delaySeconds > 0) {
                            WorkManagerUtils.scheduleReminder(
                                    getApplicationContext(),
                                    delaySeconds,
                                    details.getNextEpisodeToAir().getId(),
                                    details.getName(),
                                    details.getNextEpisodeToAir().getName()
                            );
                            
                            // Also save to upcoming_episodes table for UI
                            UpcomingEpisodes upcoming = new UpcomingEpisodes();
                            upcoming.setEpisodeId(details.getNextEpisodeToAir().getId());
                            upcoming.setSeriesId(details.getId());
                            upcoming.setSeriesTitle(details.getName());
                            upcoming.setEpisodeName(details.getNextEpisodeToAir().getName());
                            upcoming.setAirDate(details.getNextEpisodeToAir().getAirDate());
                            int seasonNumber = details.getNextEpisodeToAir().getSeasonNumber();
                            upcoming.setSeasonNumber(seasonNumber);
                            upcoming.setEpisodeNumber(details.getNextEpisodeToAir().getEpisodeNumber());
                            
                            // Find the season for deeper navigation info
                            if (details.getSeasons() != null) {
                                for (com.lineargs.watchnext.utils.retrofit.series.seriesdetails.Season season : details.getSeasons()) {
                                    if (season.getSeasonNumber() == seasonNumber) {
                                        upcoming.setSeasonId(season.getId());
                                        upcoming.setEpisodeCount(season.getEpisodeCount());
                                        break;
                                    }
                                }
                            }
                            
                            String posterPath = details.getPosterPath();
                            if (posterPath != null) {
                                if (posterPath.startsWith("/")) {
                                    posterPath = posterPath.substring(1);
                                }
                                posterPath = "https://image.tmdb.org/t/p/w500/" + posterPath;
                            }
                            upcoming.setPosterPath(posterPath);
                            database.upcomingEpisodesDao().insertUpcomingEpisode(upcoming);
                        } else {
                            // If it already aired or is airing, remove it
                            database.upcomingEpisodesDao().deleteBySeriesId(details.getId());
                        }
                    } else {
                        // No more upcoming episodes, remove existing record if any
                        database.upcomingEpisodesDao().deleteBySeriesId(details.getId());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Continue with next series
            }
        }

        return Result.success();
    }
}
