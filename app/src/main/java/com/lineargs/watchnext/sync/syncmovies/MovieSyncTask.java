package com.lineargs.watchnext.sync.syncmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.lineargs.watchnext.BuildConfig;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.dbutils.CreditDbUtils;
import com.lineargs.watchnext.utils.dbutils.MovieDbUtils;
import com.lineargs.watchnext.utils.dbutils.ReviewsDbUtils;
import com.lineargs.watchnext.utils.dbutils.VideosDbUtils;
import com.lineargs.watchnext.utils.retrofit.movies.MovieApiService;
import com.lineargs.watchnext.utils.retrofit.movies.moviedetail.MovieDetail;

import java.lang.ref.WeakReference;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class MovieSyncTask {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String APPEND_TO_RESPONSE = "videos,reviews,credits";
    private static String id;
    private static Uri mUri;

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final MovieApiService MOVIE_API_SERVICE = retrofit.create(MovieApiService.class);

    static void syncMovieDetail(final Context context, final Uri uri) {
        String stringUri = uri.toString();
        stringUri = stringUri.substring(0, stringUri.lastIndexOf('/'));
        mUri = Uri.parse(stringUri);
        id = uri.getLastPathSegment();
        String language = Locale.getDefault().toString();
        language = language.replace('_', '-');
        Call<MovieDetail> call = MOVIE_API_SERVICE.getMovieDetail(id, BuildConfig.MOVIE_DATABASE_API_KEY, language, APPEND_TO_RESPONSE);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetail> call, @NonNull final Response<MovieDetail> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ContentValues updateValues = MovieDbUtils.updateMovie(response.body());
                    UpdateMovie updateMovie = new UpdateMovie(context);
                    updateMovie.execute(updateValues);
                    ContentValues[] castValues = CreditDbUtils.getCastContentValues(response.body().getCredits(), id);
                    InsertCast insertCast = new InsertCast(context);
                    insertCast.execute(castValues);
                    ContentValues[] crewValues = CreditDbUtils.getCrewContentValues(response.body().getCredits(), id);
                    InsertCrew insertCrew = new InsertCrew(context);
                    insertCrew.execute(crewValues);
                    ContentValues[] videoValues = VideosDbUtils.getVideosContentValues(response.body().getVideos(), id);
                    InsertVideos insertVideos = new InsertVideos(context);
                    insertVideos.execute(videoValues);
                    ContentValues[] reviewsValues = ReviewsDbUtils.getReviewsContentValues(response.body().getReviews(), id);
                    InsertReviews insertReviews = new InsertReviews(context);
                    insertReviews.execute(reviewsValues);
                } else if (response.errorBody() != null) {
                    response.errorBody().close();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetail> call, @NonNull Throwable t) {

            }
        });
    }

    static class UpdateMovie extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        UpdateMovie(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            ContentValues values = contentValues[0];
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (values != null) {
                    contentResolver.update(mUri, values, DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{id});
                }
            }
            return null;
        }
    }

    static class InsertCast extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertCast(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.CreditCast.CONTENT_URI, contentValues);
                }

            }
            return null;
        }
    }

    static class InsertCrew extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertCrew(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.CreditCrew.CONTENT_URI, contentValues);
                }

            }
            return null;
        }
    }

    static class InsertVideos extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertVideos(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.Videos.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }

    static class InsertReviews extends AsyncTask<ContentValues, Void, Void> {
        private final WeakReference<Context> weakReference;

        InsertReviews(Context context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            Context context = weakReference.get();
            if (context != null) {
                ContentResolver contentResolver = context.getContentResolver();
                if (contentValues != null && contentValues.length != 0) {
                    contentResolver.bulkInsert(DataContract.Review.CONTENT_URI, contentValues);
                }
            }
            return null;
        }
    }
}
