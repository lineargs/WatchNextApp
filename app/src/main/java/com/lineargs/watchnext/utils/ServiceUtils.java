package com.lineargs.watchnext.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by goranminov on 30/11/2017.
 * <p>
 * Utilities class used for Picasso loading
 * See {@link RequestCreator}
 */

public final class ServiceUtils {

    /** The class is never initialized */
    private ServiceUtils(){}

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
}
