package com.lineargs.watchnext.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.crashlytics.android.Crashlytics;
import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.MainAdapter;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Query;
import com.lineargs.watchnext.sync.syncadapter.WatchNextSyncAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseTopActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        MainAdapter.OnItemClickListener {

    static final String FAB_ID = "fab_id";
    private static final int LOADER_ID = 333;
    private static final String BUNDLE_ARG = "sort";
    private static final String ASC = " ASC", DESC = " DESC";
    @BindView(R.id.main_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    RelativeLayout mRelativeLayout;
    private MainAdapter mAdapter;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Setup Crashlytics instance for crash reports */
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        setupActionBar();
        setupNavDrawer();
        setupViews();
        if (isConnected()) {
            WatchNextSyncAdapter.initializeSyncAdapter(this);
        }
    }

    @OnClick(R.id.fab)
    public void searchFab() {
        Intent fabIntent = new Intent(MainActivity.this, SearchActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startIntent(fabIntent);
    }

    private void showData() {
        mRelativeLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideData() {
        mRelativeLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void setupViews() {
        ButterKnife.bind(this);
        bundle = new Bundle();
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MainAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);
        /* We init the loader with bundle, so later can use the bundle to restart
         * the loader to sort the list. The default sorting is always first in db,
         * first to display. We still do not save the user preference.
         */
        getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 800;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 1) {
            return 1;
        }
        return nColumns;
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();
        setTitle(getString(R.string.title_activity_main));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_activity_main));
        }
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
        int id = item.getItemId();
        if (id == R.id.search) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startIntent(searchIntent);
            return true;
        } else if (id == R.id.sort) {
            showPopup();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Here we are restarting the loader to sort the results, based on the
     * Popup Menu item clicked
     */
    public void showPopup() {
        View menu = findViewById(R.id.sort);
        PopupMenu popupMenu = new PopupMenu(this, menu);
        popupMenu.inflate(R.menu.sort_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sort_title:
                        bundle.putString(BUNDLE_ARG, DataContract.PopularMovieEntry.COLUMN_TITLE + ASC);
                        getSupportLoaderManager().restartLoader(LOADER_ID, bundle, MainActivity.this);
                        break;
                    case R.id.sort_highest_rated:
                        bundle.putString(BUNDLE_ARG, DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + DESC);
                        getSupportLoaderManager().restartLoader(LOADER_ID, bundle, MainActivity.this);
                        break;
                    case R.id.sort_lowest_rated:
                        bundle.putString(BUNDLE_ARG, DataContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + ASC);
                        getSupportLoaderManager().restartLoader(LOADER_ID, bundle, MainActivity.this);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

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

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
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
    public void onLoaderReset(Loader<Cursor> loader) {
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
