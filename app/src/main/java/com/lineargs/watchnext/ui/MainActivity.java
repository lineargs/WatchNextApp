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
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
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
import com.lineargs.watchnext.adapters.MainAdapter;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Query;
import com.lineargs.watchnext.utils.NotificationUtils;
import com.lineargs.watchnext.utils.WorkManagerUtils;

import com.lineargs.watchnext.databinding.ActivityMainBinding;

public class MainActivity extends BaseTopActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        MainAdapter.OnItemClickListener {

    static final String FAB_ID = "fab_id";
    private static final int LOADER_ID = 333;
    private static final String BUNDLE_ARG = "sort";
    private static final String PREF_SORT_ORDER = "pref_sort_order";
    private static final String ASC = " ASC", DESC = " DESC";
    @NonNull
    ActivityMainBinding binding;
    private MainAdapter mAdapter;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Setup Crashlytics instance for crash reports */
        // Fabric.with(this, new Crashlytics());
        // Fabric.with(this, new Crashlytics());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
        bundle = new Bundle();
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        RecyclerView recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new MainAdapter(this, this);
        recyclerView.setAdapter(mAdapter);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFab();
            }
        });
        binding.toolbar.titleMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTextView();
            }
        });
        /* We init the loader with bundle, so later can use the bundle to restart
         * the loader to sort the list. The default sorting is always first in db,
         * first to display. We still do not save the user preference.
         */
        android.content.SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = sharedPreferences.getString(PREF_SORT_ORDER, DataContract.PopularMovieEntry.COLUMN_TITLE + ASC);
        bundle.putString(BUNDLE_ARG, sortOrder);
        getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
    }

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

    /* Here we are restarting the loader to sort the results, based on the
     * Popup Menu item clicked
     */
    public void showPopup() {
        View menu = findViewById(R.id.sort_by);
        PopupMenu popupMenu = new PopupMenu(this, menu);
        popupMenu.inflate(R.menu.sort_menu);

        android.content.SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = sharedPreferences.getString(PREF_SORT_ORDER, DataContract.PopularMovieEntry.COLUMN_TITLE + ASC);

        if (sortOrder.equals(DataContract.PopularMovieEntry.COLUMN_TITLE + ASC)) {
            popupMenu.getMenu().findItem(R.id.sort_title).setChecked(true);
        } else if (sortOrder.equals(DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + DESC)) {
            popupMenu.getMenu().findItem(R.id.sort_highest_rated).setChecked(true);
        } else if (sortOrder.equals(DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + ASC)) {
            popupMenu.getMenu().findItem(R.id.sort_lowest_rated).setChecked(true);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sort_title:
                        String sortTitle = DataContract.PopularMovieEntry.COLUMN_TITLE + ASC;
                        bundle.putString(BUNDLE_ARG, sortTitle);
                        saveSortOrder(sortTitle);
                        getSupportLoaderManager().restartLoader(LOADER_ID, bundle, MainActivity.this);
                        break;
                    case R.id.sort_highest_rated:
                        String sortHighest = DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + DESC;
                        bundle.putString(BUNDLE_ARG, sortHighest);
                        saveSortOrder(sortHighest);
                        getSupportLoaderManager().restartLoader(LOADER_ID, bundle, MainActivity.this);
                        break;
                    case R.id.sort_lowest_rated:
                        String sortLowest = DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + ASC;
                        bundle.putString(BUNDLE_ARG, sortLowest);
                        saveSortOrder(sortLowest);
                        getSupportLoaderManager().restartLoader(LOADER_ID, bundle, MainActivity.this);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(this,
                        DataContract.Favorites.CONTENT_URI,
                        Query.PROJECTION,
                        null,
                        null,
                        args.getString(BUNDLE_ARG, null));
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    private void saveSortOrder(String sortOrder) {
        android.content.SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_SORT_ORDER, sortOrder);
        editor.apply();
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID:
                mAdapter.swapCursor(data);
                if (data != null && data.getCount() != 0) {
                    data.moveToFirst();
                    showData();
                } else if (data != null && data.getCount() == 0) {
                    hideData();
                }
                break;
            default:
                throw new RuntimeException("Loader not implemented: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemSelected(Uri uri) {

        /* In the main(Favorites) activity we have both movies and series.
         * To decide on click which activity to start we simply check in the table if the
         * selected row is serie or movie.
         * TYPE: Movie = 0 / Serie = 1
         */
        if (isSeries(uri)) {
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

    /**
     * Checks whether the entry is Movie / Serie in the favourites db. Used so we can open the appropriate
     * Activity.
     * @param uri The URI
     * @return true / false
     */
    private boolean isSeries(Uri uri) {
        String id = uri.getLastPathSegment();
        Cursor cursor = this.getContentResolver().query(DataContract.Favorites.CONTENT_URI,
                null,
                DataContract.Favorites.COLUMN_TYPE + " = ? AND " + DataContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[]{String.valueOf(1), id},
                null);
        if (cursor == null) {
            return false;
        }
        boolean contains = cursor.getCount() > 0;
        cursor.close();
        return contains;
    }

    private void startIntent(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
