package com.lineargs.watchnext.workers;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.Utils;
import com.lineargs.watchnext.utils.dbutils.CreditDbUtils;
import com.lineargs.watchnext.utils.dbutils.MovieDbUtils;
import com.lineargs.watchnext.utils.dbutils.ReviewsDbUtils;
import com.lineargs.watchnext.utils.dbutils.VideosDbUtils;
import com.lineargs.watchnext.utils.retrofit.movies.MovieApiService;
import com.lineargs.watchnext.utils.retrofit.movies.moviedetail.MovieDetail;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailWorker extends Worker {

    public static final String ARG_URI = "arg_uri";
    public static final String ARG_UPDATE_ONLY = "arg_update_only";

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String APPEND_TO_RESPONSE = "videos,reviews,credits";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public MovieDetailWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String uriString = getInputData().getString(ARG_URI);
        boolean updateOnly = getInputData().getBoolean(ARG_UPDATE_ONLY, false);

        if (uriString == null) {
            return Result.failure();
        }

        Uri uri = Uri.parse(uriString);
        String id = uri.getLastPathSegment();
        MovieApiService service = retrofit.create(MovieApiService.class);

        try {
            if (updateOnly) {
                updateMovieDetail(service, id, uri);
            } else {
                fetchFullMovieDetail(service, id, uri);
            }
            return Result.success();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.retry();
        }
    }

    private void fetchFullMovieDetail(MovieApiService service, String id, Uri uri) throws IOException {
        Call<MovieDetail> call = service.getMovieDetail(id, BuildConfig.MOVIE_DATABASE_API_KEY, APPEND_TO_RESPONSE);
        Response<MovieDetail> response = call.execute();

        if (response.isSuccessful() && response.body() != null) {
            MovieDetail movie = response.body();
            Context context = getApplicationContext();
            
            // Update Movie
            ContentValues updateValues = MovieDbUtils.updateMovie(movie);
            context.getContentResolver().update(Utils.getBaseUri(uri), updateValues, DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{id});

            // Insert Cast
            ContentValues[] castValues = CreditDbUtils.getCastContentValues(movie.getCredits(), id);
            bulkInsert(DataContract.Credits.CONTENT_URI_CAST, castValues);

            // Insert Crew
            ContentValues[] crewValues = CreditDbUtils.getCrewContentValues(movie.getCredits(), id);
            bulkInsert(DataContract.Credits.CONTENT_URI_CREW, crewValues);

            // Insert Videos
            ContentValues[] videoValues = VideosDbUtils.getVideosContentValues(movie.getVideos(), id);
            bulkInsert(DataContract.Videos.CONTENT_URI, videoValues);

            // Insert Reviews
            ContentValues[] reviewValues = ReviewsDbUtils.getReviewsContentValues(movie.getReviews(), id);
            bulkInsert(DataContract.Review.CONTENT_URI, reviewValues);
        }
    }

    private void updateMovieDetail(MovieApiService service, String id, Uri uri) throws IOException {
        Call<MovieDetail> call = service.updateMovie(id, BuildConfig.MOVIE_DATABASE_API_KEY);
        Response<MovieDetail> response = call.execute();

        if (response.isSuccessful() && response.body() != null) {
            ContentValues updateValues = MovieDbUtils.updateMovie(response.body());
            getApplicationContext().getContentResolver().update(Utils.getBaseUri(uri), updateValues, DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ?", new String[]{id});
        }
    }

    private void bulkInsert(Uri uri, ContentValues[] values) {
        if (values != null && values.length > 0) {
            getApplicationContext().getContentResolver().bulkInsert(uri, values);
        }
    }
}
