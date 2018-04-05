package com.lineargs.watchnext.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.Utils;

public class Tools {

    private Tools() {
    }

    private static String getDeviceInfo(Context context) {
        return context.getString(R.string.feedback, Build.MANUFACTURER, Build.MODEL, Build.VERSION.RELEASE);
    }

    public static Intent getFeedbackIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"watchnextguide@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, Utils.versionString(context));
        intent.putExtra(Intent.EXTRA_TEXT, getDeviceInfo(context));
        return intent;
    }
}
