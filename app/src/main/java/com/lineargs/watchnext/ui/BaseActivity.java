package com.lineargs.watchnext.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.sync.syncadapter.WatchNextSyncAdapter;

import java.util.Locale;

/**
 * Created by goranminov on 05/11/2017.
 * <p>
 * Provides common functionality across all actitivies like setting the theme.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setCustomTheme();
        super.onCreate(savedInstanceState);

    }

    /*
     * Set a theme based on user preferences
     * As we have only two themes in our case it is safe to check only one
     */
    protected void setCustomTheme() {
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.pref_theme_key), "").contains(getString(R.string.pref_theme_blue_grey_key))) {
            setTheme(R.style.BlueGreyTheme);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String locale = Locale.getDefault().toString();
        if (!sharedPreferences
                .getString(getString(R.string.pref_locale_key), "").contains(locale)) {
            sharedPreferences.edit().putString(getString(R.string.pref_locale_key), locale).apply();
            WatchNextSyncAdapter.syncImmediately(this);
        }
    }

    /**
     * Implementers must call this in {@link #onCreate} after {@link #setContentView} if they want
     * to use the action bar. If want to set a title, might also want to @Override
     */
    protected void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
