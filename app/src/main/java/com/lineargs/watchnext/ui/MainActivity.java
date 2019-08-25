package com.lineargs.watchnext.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.MainAdapter;
import com.lineargs.watchnext.data.Favourites;
import com.lineargs.watchnext.data.FavouritesViewModel;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseTopActivity implements MainAdapter.OnItemClickListener {

    static final String FAB_ID = "fab_id";
    private static final String BUNDLE_ARG = "sort";
    private static final String ASC = " ASC", DESC = " DESC";
    @BindView(R.id.main_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_layout)
    RelativeLayout mRelativeLayout;
    private MainAdapter mAdapter;
    private FavouritesViewModel favouritesViewModel;
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
    }

    @OnClick(R.id.fab)
    public void searchFab() {
        Intent fabIntent = new Intent(MainActivity.this, SearchActivity.class);
        startIntent(fabIntent);
    }

    @OnClick(R.id.title_main_activity)
    public void searchTextView() {
        Intent txtIntent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(txtIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
        favouritesViewModel = ViewModelProviders.of(this).get(FavouritesViewModel.class);
        favouritesViewModel.getFavourites().observe(this, this::loadData);
        if (isConnected()) {
            syncWorkerNow();
        }
    }

    private void syncWorkerNow() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String locale = Locale.getDefault().toString();
        if (!sharedPreferences
                .getString(getString(R.string.pref_locale_key), "").contains(locale)) {
            sharedPreferences.edit().putString(getString(R.string.pref_locale_key), locale).apply();
            favouritesViewModel.syncNow();
        }
    }

    private void loadData(List<Favourites> favourites) {
        if (favourites != null) {
            if (favourites.size() != 0) {
                showData();
                mAdapter.swapFavourites(favourites);
            }
        } else {
            hideData();
        }
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
                Intent txtIntent = new Intent(MainActivity.this, SearchActivity.class);
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
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.sort_title:
                    break;
                case R.id.sort_highest_rated:
                    break;
                case R.id.sort_lowest_rated:
                    break;
            }
            return true;
        });
        popupMenu.show();
    }

    @Override
    public void onItemSelected(String tmdbId) {
        /* In the main(Favorites) activity we have both movies and series.
         * To decide on click which activity to start we simply check in the table if the
         * selected row is serie or movie.
         * TYPE: Movie = 0 / Serie = 1
         */
//        if (isSeries(uri)) {
//            Intent intent = new Intent(this, SeriesDetailsActivity.class);
//            intent.setData(uri);
//            intent.putExtra(FAB_ID, 1);
//            startIntent(intent);
//        } else {
//            Intent intent = new Intent(this, MovieDetailsActivity.class);
//            intent.setData(uri);
//            intent.putExtra(FAB_ID, 1);
//            startIntent(intent);
//        }
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
