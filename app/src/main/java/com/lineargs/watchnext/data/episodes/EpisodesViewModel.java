package com.lineargs.watchnext.data.episodes;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.api.series.SeriesApiService;
import com.lineargs.watchnext.api.series.seasondetails.SeasonDetails;
import com.lineargs.watchnext.data.WatchNextRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EpisodesViewModel extends AndroidViewModel {

    private WatchNextRepository repository;

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public EpisodesViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
    }

    public LiveData<List<Episodes>> getEpisodes(int seasonId) {
        return repository.getSeasonEpisodes(seasonId);
    }

    public void syncEpisodes(final String id, int seasonNumber, final String seasonId) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SeriesApiService seriesApiService = retrofit.create(SeriesApiService.class);

        Call<SeasonDetails> call = seriesApiService.getSeason(id, seasonNumber, BuildConfig.MOVIE_DATABASE_API_KEY);

        call.enqueue(new Callback<SeasonDetails>() {
            @Override
            public void onResponse(@NonNull Call<SeasonDetails> call, @NonNull Response<SeasonDetails> response) {

                if (response.isSuccessful() && response.body() != null) {
                    repository.insertEpisodes(response.body().getEpisodes(), seasonId);

                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SeasonDetails> call, @NonNull Throwable t) {
            }
        });
    }
}
