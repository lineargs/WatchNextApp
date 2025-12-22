package com.lineargs.watchnext.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SearchAdapter;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.SearchQuery;
import com.lineargs.watchnext.sync.syncsearch.SearchSyncUtils;
import com.lineargs.watchnext.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * Created by goranminov on 06/11/2017.
 * <p>
 * Search Activity used to search for movies on the Db website
 */
public class SearchMainActivity extends BaseTopActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 223;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.search_results)
    RecyclerView mSearchResults;
    boolean adult;
    private String queryString;
    private Handler handler;
    private String mQuery = "";
    private SearchAdapter mResultsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);
        handler = new Handler();
        setupActionBar();
        setupNavDrawer();
        setupSearchView();
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mSearchResults.setLayoutManager(layoutManager);
        mResultsAdapter = new SearchAdapter(this);
        mSearchResults.setAdapter(mResultsAdapter);
        String query = getIntent().getStringExtra(SearchManager.QUERY);
        query = query == null ? "" : query;
        mQuery = query;
        if (mSearchView != null) {
            mSearchView.setQuery(query, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adult = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.adult_search), false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(SearchManager.QUERY)) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (!TextUtils.isEmpty(query)) {
                mSearchView.setQuery(query, false);
                searchFor(query);
            }
        }
    }

    @Override
    public void setDrawerIndicatorEnabled() {
        super.setDrawerIndicatorEnabled();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back_black);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        if (searchManager != null) {
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        mSearchView.setIconified(false);
        mSearchView.setQueryHint(getString(R.string.search_query_hint));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryString = newText;
                handler.removeCallbacksAndMessages(null);
                /* So we can wait for the user to find the next letter
                 * on the keyboard
                 */
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchFor(queryString);
                    }
                }, 1000);
                return true;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                dismiss();
                return false;
            }
        });
        if (!TextUtils.isEmpty(mQuery)) {
            mSearchView.setQuery(mQuery, false);
        }
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    public void dismiss() {
        ActivityCompat.finishAfterTransition(this);
    }

    @OnClick(R.id.scrim)
    public void scrimView() {
        dismiss();
    }

    private void searchFor(String query) {
        Bundle args = new Bundle(1);
        if (query == null) {
            query = "";
        }
        args.putString(Constants.ARG_QUERY, query);

        if (TextUtils.equals(query, mQuery)) {
            getSupportLoaderManager().initLoader(LOADER_ID, args, this);
        } else {
            SearchSyncUtils.syncSearchMovies(this, query, adult);
            startLoading();
        }
        mQuery = query;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void startLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mSearchResults.setVisibility(GONE);
    }

    private void showData() {
        mProgressBar.setVisibility(GONE);
        mSearchResults.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(this,
                        DataContract.Search.CONTENT_URI,
                        SearchQuery.SEARCH_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new UnsupportedOperationException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID:
                mResultsAdapter.swapCursor(data);
                if (data != null && data.getCount() != 0) {
                    data.moveToFirst();
                    showData();
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mResultsAdapter.swapCursor(null);
    }
}
