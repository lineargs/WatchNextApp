package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.api.series.SeriesApiService;
import com.lineargs.watchnext.api.series.seriesdetails.SeriesDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.lineargs.watchnext.utils.RoomUtils.buildCompaniesString;
import static com.lineargs.watchnext.utils.RoomUtils.buildGenresString;

public class SeriesDetailsViewModel extends AndroidViewModel {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String APPEND_TO_RESPONSE = "credits,videos,similar";

    private WatchNextRepository repository;
    private MutableLiveData<Series> seriesDetails;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private SeriesApiService seriesApiService = retrofit.create(SeriesApiService.class);

    public SeriesDetailsViewModel(@NonNull Application application) {
        super(application);
        repository = new WatchNextRepository(application);
    }

    public MutableLiveData<Series> getSeriesDetails(String id) {
        if (seriesDetails == null) {
            seriesDetails = new MutableLiveData<>();
            syncSeriesDetails(id);
        }
        return seriesDetails;
    }

    private void syncSeriesDetails(String id) {
        Call<SeriesDetails> call = seriesApiService.getDetails(id, BuildConfig.MOVIE_DATABASE_API_KEY, APPEND_TO_RESPONSE);

        call.enqueue(new Callback<SeriesDetails>() {
            @Override
            public void onResponse(@NonNull Call<SeriesDetails> call, @NonNull Response<SeriesDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Series series = new Series();
                    series.setHomepage(response.body().getHomepage());
                    series.setProductionCompanies(buildCompaniesString(response.body().getProductionCompanies()));
                    //TODO Implement networks
                    series.setNetworks("");
                    series.setStatus(response.body().getStatus());
                    series.setGenres(buildGenresString(response.body().getGenres()));
                    series.setTmdbId(response.body().getId());
                    repository.updateSeries(series);
                    repository.insertVideos(response.body().getVideos(), response.body().getId());
                    repository.insertCast(response.body().getCredits(), response.body().getId());
                    repository.insertCrew(response.body().getCredits(), response.body().getId());
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SeriesDetails> call, @NonNull Throwable t) {
            }
        });

    }

    public LiveData<List<Credits>> getCast(int tmdbId) {return repository.getCast(tmdbId);}
    public LiveData<List<Credits>> getCrew(int tmdbId) {return repository.getCrew(tmdbId);}

    public LiveData<Series> getSeries(int tmdbId) {
        return repository.getSeries(tmdbId);
    }
}
