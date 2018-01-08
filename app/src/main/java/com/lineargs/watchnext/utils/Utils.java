package com.lineargs.watchnext.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataDbHelper;

public class Utils {

    private Utils() {}

    public static String version(Context context) {
        String version;
        try {
            version = context.getPackageManager().getPackageInfo
                    (context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "unknown";
        }
        return version;
    }

    public static String versionString(Context context) {
        return context.getString(R.string.about_version, version(context),
                DataDbHelper.DATABASE_VERSION);
    }

    public static Uri getBaseUri (Uri uri) {
        String stringUri = uri.toString();
        stringUri = stringUri.substring(0, stringUri.lastIndexOf('/'));
        return Uri.parse(stringUri);
    }
}
