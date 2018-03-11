package com.lineargs.watchnext.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.NotificationUtils;

/**
 * Created by goranminov on 05/11/2017.
 * <p>
 * Provides common functionality across all activities like setting the theme.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setCustomTheme();
        super.onCreate(savedInstanceState);
        NotificationUtils utils = new NotificationUtils(this);
    }

    /*
     * Set a theme based on user preferences
     * As we have only two themes in our case it is safe to check only one
     */
    protected void setCustomTheme() {
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.pref_theme_key), "").contains(getString(R.string.pref_theme_blue_grey_key))) {
            setTheme(R.style.WatchNext_BlueGrey);
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
