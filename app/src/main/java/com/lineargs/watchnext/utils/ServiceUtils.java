package com.lineargs.watchnext.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.view.View;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by goranminov on 30/11/2017.
 * <p>
 * Utilities helper class
 */

public final class ServiceUtils {

    private static final String IMDB_APP_POST_TITLE_URI = "/";

    private static final String IMDB_APP_TITLE_URI = "imdb:///title/";

    private static final String IMDB_TITLE_URL = "http://imdb.com/title/";

    /**
     * The class is never initialized
     */
    private ServiceUtils() {
    }

    @NonNull
    public static RequestCreator loadPicasso(Context context, String path) {
        return Picasso.with(context).load(path);
    }

    public static void openYouTube(Context context, String youTube) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(youTube));
        context.startActivity(intent);
    }

    public static void shareMovie(Activity activity, String id) {
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(activity)
                .setText(activity.getString(R.string.share_movie, id))
                .setType("text/plain");
        try {
            intentBuilder.startChooser();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, R.string.no_apps_installed, Toast.LENGTH_SHORT).show();
        }
    }

    public static void shareSerie(Activity activity, String id) {
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(activity)
                .setText(activity.getString(R.string.share_serie, id))
                .setType("text/plain");
        try {
            intentBuilder.startChooser();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, R.string.no_apps_installed, Toast.LENGTH_SHORT).show();
        }
    }

    public static void openWeb(Context context, String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }
        context.startActivity(intent);
    }

    public static void setUpImdbButton(final String imdbId, final View imdbButton) {
        if (imdbButton != null) {
            if (!imdbId.equals("0")) {
                imdbButton.setEnabled(true);
                imdbButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openImdb(imdbId, v.getContext());
                    }
                });
            } else {
                imdbButton.setEnabled(false);
            }
        }
    }

    private static void openImdb(String imdbId, Context context) {
        if (context == null || imdbId.equals("0")) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(IMDB_APP_TITLE_URI + imdbId
                + IMDB_APP_POST_TITLE_URI));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }

        if (!Utils.tryStartActivity(context, intent, false)) {
            //TODO Track action
            openWeb(context, IMDB_TITLE_URL + imdbId);
        }
    }
}
