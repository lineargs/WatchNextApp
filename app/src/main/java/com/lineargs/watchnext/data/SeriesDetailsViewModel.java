package com.lineargs.watchnext.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.utils.retrofit.series.SeriesApiService;
import com.lineargs.watchnext.utils.retrofit.series.seriesdetails.Genre;
import com.lineargs.watchnext.utils.retrofit.series.seriesdetails.ProductionCompany;
import com.lineargs.watchnext.utils.retrofit.series.seriesdetails.SeriesDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeriesDetailsViewModel extends AndroidViewModel {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String APPEND_TO_RESPONSE = "credits,videos,similar";

    private WatchNextRepository repository;
    private MutableLiveData<Series> seriesDetails;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    SeriesApiService seriesApiService = retrofit.create(SeriesApiService.class);

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
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SeriesDetails> call, @NonNull Throwable t) {
            }
        });

    }

    public LiveData<Series> getSeries(int tmdbId) {
        return repository.getSeries(tmdbId);
    }

    /**
     * Helper method used for building Companies String
     *
     * @param companies List of type ProductionCompany
     * @return String in following format: Google, Google, Google
     */
    private static String buildCompaniesString(List<ProductionCompany> companies) {

        if (companies == null || companies.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < companies.size(); i++) {
            ProductionCompany company = companies.get(i);
            stringBuilder.append(company.getName());
            if (i + 1 < companies.size()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Helper method used for building Genres String
     *
     * @param genres List of type Genre
     * @return String in following format: Comedy, Horror, Fantasy
     */
    private static String buildGenresString(List<Genre> genres) {

        if (genres == null || genres.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            Genre genre = genres.get(i);
            stringBuilder.append(genre.getName());
            if (i + 1 < genres.size()) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}
