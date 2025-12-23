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

import com.lineargs.watchnext.databinding.ActivitySearchMainBinding;

import static android.view.View.GONE;

/**
 * Created by goranminov on 06/11/2017.
 * <p>
 * Search Activity used to search for movies on the Db website
 */
public class SearchMainActivity extends BaseTopActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 223;
    private ActivitySearchMainBinding binding;
    boolean adult;
    private String queryString;
    private Handler handler;
    private String mQuery = "";
    private SearchAdapter mResultsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        handler = new Handler();
        setupActionBar();
        setupNavDrawer();
        setupSearchView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.searchResults.setLayoutManager(layoutManager);
        mResultsAdapter = new SearchAdapter(this);
        binding.searchResults.setAdapter(mResultsAdapter);
        String query = getIntent().getStringExtra(SearchManager.QUERY);
        query = query == null ? "" : query;
        mQuery = query;
        if (binding.searchView != null) {
            binding.searchView.setQuery(query, false);
        }
        
        binding.scrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrimView();
            }
        });
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
                binding.searchView.setQuery(query, false);
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
            binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        binding.searchView.setIconified(false);
        binding.searchView.setQueryHint(getString(R.string.search_query_hint));
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                binding.searchView.clearFocus();
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
        binding.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                dismiss();
                return false;
            }
        });
        if (!TextUtils.isEmpty(mQuery)) {
            binding.searchView.setQuery(mQuery, false);
        }
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    public void dismiss() {
        ActivityCompat.finishAfterTransition(this);
    }

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
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.searchResults.setVisibility(GONE);
    }

    private void showData() {
        binding.progressBar.setVisibility(GONE);
        binding.searchResults.setVisibility(View.VISIBLE);
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
