package com.lineargs.watchnext.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.WorkManagerUtils;
import com.lineargs.watchnext.tools.Tools;

import java.util.Locale;



/**
 * Created by goranminov on 05/11/2017.
 * <p>
 * Adds Navigation Drawer to {@link BaseActivity}
 */

public abstract class BaseDrawerActivity extends BaseActivity {

    private static final int NAV_DRAWER_CLOSE_DELAY = 250;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ConnectivityBroadcastReceiver connectivityBroadcastReceiver;
    IntentFilter connectivityIntentFilter;
    Snackbar snackbar;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isConnected()) {
            syncAdapterNow();
        }
        handler = new Handler();
        connectivityIntentFilter = new IntentFilter();
        connectivityBroadcastReceiver = new ConnectivityBroadcastReceiver();
        connectivityIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        snackbar = Snackbar.make(findViewById(R.id.list_coordinator_layout), getString(R.string.snackbar_no_connection), Snackbar.LENGTH_INDEFINITE);
        showSnackBar();
        registerReceiver(connectivityBroadcastReceiver, connectivityIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityBroadcastReceiver);
    }

    private void syncAdapterNow() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String locale = Locale.getDefault().toString();
        if (!sharedPreferences
                .getString(getString(R.string.pref_locale_key), "").contains(locale)) {
            sharedPreferences.edit().putString(getString(R.string.pref_locale_key), locale).apply();
            WorkManagerUtils.syncImmediately(this);
        }
    }

    @Override
    protected void setCustomTheme() {
        super.setCustomTheme();
    }

    /**
     * Initializes the navigation drawer. Implementers must call this
     * in {@link #onCreate} after {@link #setContentView}.
     */
    public void setupNavDrawer() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onNavItemClicked(item.getItemId());
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
            return;
        }
        super.onBackPressed();
    }

    /**
     * Simple switch statement checking if we are already displaying the correct screen
     *
     * @param itemId Navigation menu item
     */
    private void onNavItemClicked(int itemId) {
        Intent intent = null;
        switch (itemId) {
            case R.id.nav_favorite:
                if (this instanceof MainActivity) {
                    break;
                }
                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.nav_movies:
                if (this instanceof TabbedMoviesActivity) {
                    break;
                }
                intent = new Intent(this, TabbedMoviesActivity.class);
                break;
            case R.id.nav_series:
                if (this instanceof TabbedSeriesActivity) {
                    break;
                }
                intent = new Intent(this, TabbedSeriesActivity.class);
                break;
            case R.id.nav_theaters:
                if (this instanceof TheaterActivity) {
                    break;
                }
                intent = new Intent(this, TheaterActivity.class);
                break;
//            case R.id.nav_statistics:
//                if (this instanceof StatisticsActivity) {
//                    break;
//                }
//                intent = new Intent(this, StatisticsActivity.class);
//                break;
            case R.id.nav_settings:
                intent = new Intent(this, SettingsActivity.class);
                break;
            case R.id.nav_feedback:
                Intent feedbackIntent = Tools.getFeedbackIntent(this);
                if (feedbackIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(feedbackIntent);
                } else {
                    Toast.makeText(this, R.string.no_apps_installed, Toast.LENGTH_SHORT).show();
                }
        }

        if (intent != null) {
            final Intent finalIntent = intent;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startNavDrawerItem(finalIntent);
                }
            }, NAV_DRAWER_CLOSE_DELAY);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void startNavDrawerItem(Intent intent) {
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * @return true if Nav Drawer is open
     */
    public boolean isNavDrawerOpen() {
        return drawerLayout.isDrawerOpen(navigationView);
    }

    /**
     * Hilghlights the given position. Activities listed
     * should call this method in {@link #onStart()}
     *
     * @param menuItemId Navigation menu item
     */
    public void setDrawerSelectedItem(@IdRes int menuItemId) {
        navigationView.setCheckedItem(menuItemId);
    }

    public void setDrawerIndicatorEnabled() {
        toolbar.setNavigationIcon(R.drawable.icon_menu_white);
    }

    public void openNavDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeNavDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public boolean toggleDrawer(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                closeNavDrawer();
            } else {
                openNavDrawer();
            }
            return true;
        }
        return false;
    }

    /**
     * @return true if the device is connected to WiFi or mobile data
     */
    public boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Shows snackbar if the device is not connected
     */
    public void showSnackBar() {
        if (!isConnected()) {
            snackbar.show();
        } else {
            snackbar.dismiss();
        }
    }

    private class ConnectivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                showSnackBar();
            }
        }
    }
}
