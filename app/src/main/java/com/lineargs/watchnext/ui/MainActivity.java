package com.lineargs.watchnext.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

// import com.crashlytics.android.Crashlytics;
import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Query;
import com.lineargs.watchnext.utils.NotificationUtils;
import com.lineargs.watchnext.jobs.WorkManagerUtils;
import com.lineargs.watchnext.ui.FavoritesViewModel;
import com.lineargs.watchnext.adapters.FavoritesAdapter;


import com.lineargs.watchnext.databinding.ActivityMainBinding;

public class MainActivity extends BaseTopActivity {

    static final String FAB_ID = "fab_id";
    private static final String PREF_SORT_ORDER = "pref_sort_order";
    private static final String ASC = " ASC", DESC = " DESC";
    @NonNull
    ActivityMainBinding binding;
    private FavoritesViewModel viewModel;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private String currentSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(FavoritesViewModel.class);
        
        setupActionBar();
        setupNavDrawer();
        setupTabs();
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
        if (isConnected()) {
            WorkManagerUtils.schedulePeriodicSync(this);
            WorkManagerUtils.syncImmediately(this);
            WorkManagerUtils.scheduleSubscriptionCheck(this);
        }
    }

    public void searchFab() {
        Intent fabIntent = new Intent(MainActivity.this, SearchMainActivity.class);
        startIntent(fabIntent);
    }

    public void searchTextView() {
        Intent txtIntent = new Intent(MainActivity.this, SearchMainActivity.class);
        startActivity(txtIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void setupTabs() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        binding.container.setAdapter(mSectionsPagerAdapter);
        binding.tabs.setupWithViewPager(binding.container);

        binding.fab.setOnClickListener(view -> searchFab());
        binding.toolbar.titleMainActivity.setOnClickListener(view -> searchTextView());
        
        android.content.SharedPreferences sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        currentSortOrder = sharedPreferences.getString(PREF_SORT_ORDER, DataContract.PopularMovieEntry.COLUMN_TITLE + ASC);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setDrawerSelectedItem(R.id.nav_favorite);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent txtIntent = new Intent(MainActivity.this, SearchMainActivity.class);
                startActivity(txtIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            case R.id.sort_by:
                showPopup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showPopup() {
        View menu = findViewById(R.id.sort_by);
        PopupMenu popupMenu = new PopupMenu(this, menu);
        popupMenu.inflate(R.menu.sort_menu);

        if (currentSortOrder.equals(DataContract.PopularMovieEntry.COLUMN_TITLE + ASC)) {
            popupMenu.getMenu().findItem(R.id.sort_title).setChecked(true);
        } else if (currentSortOrder.equals(DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + DESC)) {
            popupMenu.getMenu().findItem(R.id.sort_highest_rated).setChecked(true);
        } else if (currentSortOrder.equals(DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + ASC)) {
            popupMenu.getMenu().findItem(R.id.sort_lowest_rated).setChecked(true);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.sort_title:
                    currentSortOrder = DataContract.PopularMovieEntry.COLUMN_TITLE + ASC;
                    saveSortOrder(currentSortOrder);
                    viewModel.setSortOrder(currentSortOrder);
                    break;
                case R.id.sort_highest_rated:
                    currentSortOrder = DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + DESC;
                    saveSortOrder(currentSortOrder);
                    viewModel.setSortOrder(currentSortOrder);
                    break;
                case R.id.sort_lowest_rated:
                    currentSortOrder = DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + ASC;
                    saveSortOrder(currentSortOrder);
                    viewModel.setSortOrder(currentSortOrder);
                    break;
            }
            return true;
        });
        popupMenu.show();
    }

    private void saveSortOrder(String sortOrder) {
        android.content.SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_SORT_ORDER, sortOrder);
        editor.apply();
    }

    public void onItemSelected(Uri uri, boolean isSeries) {
        if (isSeries) {
            Intent intent = new Intent(this, SeriesDetailsActivity.class);
            intent.setData(uri);
            intent.putExtra(FAB_ID, 1);
            startIntent(intent);
        } else {
            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.setData(uri);
            intent.putExtra(FAB_ID, 1);
            startIntent(intent);
        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //Updated to 900 as per the newer device compatibility
        int widthDivider = 900;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 1) {
            return 1;
        }
        return nColumns;
    }

    @Override
    public void setDrawerIndicatorEnabled() {
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.icon_menu_grey);
    }

    private void startIntent(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FavoritesFragment();
                case 1:
                    return new ScheduleFragment();
                default:
                    return new FavoritesFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_favorites);
                case 1:
                    return getString(R.string.tab_schedule);
                default:
                    return null;
            }
        }
    }
}
