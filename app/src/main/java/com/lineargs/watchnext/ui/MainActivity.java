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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

// import com.crashlytics.android.Crashlytics;
import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Query;
import com.lineargs.watchnext.utils.NotificationUtils;
import com.lineargs.watchnext.utils.WorkManagerUtils;
import com.lineargs.watchnext.ui.FavoritesViewModel;
import com.lineargs.watchnext.adapters.FavoritesAdapter;


import com.lineargs.watchnext.databinding.ActivityMainBinding;

public class MainActivity extends BaseTopActivity {

    static final String FAB_ID = "fab_id";
    private static final String PREF_SORT_ORDER = "pref_sort_order";
    private static final String ASC = " ASC", DESC = " DESC";
    @NonNull
    ActivityMainBinding binding;
    private FavoritesAdapter mAdapter;
    private FavoritesViewModel viewModel;
    private String currentSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(FavoritesViewModel.class);
        
        setupActionBar();
        setupNavDrawer();
        setupViews();
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
        if (isConnected()) {
            WorkManagerUtils.schedulePeriodicSync(this);
            WorkManagerUtils.syncImmediately(this);
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

    private void showData() {
        if (findViewById(R.id.empty_layout) != null) {findViewById(R.id.empty_layout).setVisibility(View.GONE);}
        findViewById(R.id.main_recycler_view).setVisibility(View.VISIBLE);
    }

    private void hideData() {
        if (findViewById(R.id.empty_layout) != null) {findViewById(R.id.empty_layout).setVisibility(View.VISIBLE);}
        findViewById(R.id.main_recycler_view).setVisibility(View.GONE);
    }

    private void setupViews() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        
        mAdapter = new FavoritesAdapter(this, this::onItemSelected, this::onStarClicked);
        recyclerView.setAdapter(mAdapter);
        
        binding.fab.setOnClickListener(view -> searchFab());
        binding.toolbar.titleMainActivity.setOnClickListener(view -> searchTextView());
        
        android.content.SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentSortOrder = sharedPreferences.getString(PREF_SORT_ORDER, DataContract.PopularMovieEntry.COLUMN_TITLE + ASC);

        viewModel.setSortOrder(currentSortOrder);
        viewModel.getFavorites().observe(this, favorites -> {
            if (favorites != null && !favorites.isEmpty()) {
                mAdapter.submitList(favorites, () -> {
                    if (recyclerView != null) {
                        recyclerView.scrollToPosition(0);
                    }
                });
                showData();
            } else {
                hideData();
            }
        });
    }

    private void onStarClicked(com.lineargs.watchnext.data.entity.Favorites favorite) {
        viewModel.removeFavorite(favorite.getTmdbId());
        android.widget.Toast.makeText(this, getString(R.string.toast_remove_from_favorites), android.widget.Toast.LENGTH_SHORT).show();
    }

    // sortList and safeParseDouble removed


    @Override
    public void setDrawerIndicatorEnabled() {
        super.setDrawerIndicatorEnabled();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_menu_grey);
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

    private void startIntent(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
