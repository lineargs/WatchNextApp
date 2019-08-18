package com.lineargs.watchnext.ui;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SearchAdapter;
import com.lineargs.watchnext.data.Search;
import com.lineargs.watchnext.data.SearchViewModel;
import com.lineargs.watchnext.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * Created by goranminov on 06/11/2017.
 * <p>
 * Search Activity used to search for movies on the Db website
 */
public class SearchActivity extends BaseTopActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.search_results)
    RecyclerView searchResults;
    boolean adult;
    private String queryString;
    private Handler handler;
    private String query = "";
    private SearchAdapter resultsAdapter;
    private SearchViewModel searchViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        handler = new Handler();
        setupActionBar();
        setupNavDrawer();
        setupSearchView();
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        searchResults.setLayoutManager(layoutManager);
        resultsAdapter = new SearchAdapter(this);
        searchResults.setAdapter(resultsAdapter);
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        searchViewModel.getSearchResults().observe(this, new Observer<List<Search>>() {
            @Override
            public void onChanged(@Nullable List<Search> searches) {
                loadData(searches);
            }
        });
        String query = getIntent().getStringExtra(SearchManager.QUERY);
        query = query == null ? "" : query;
        this.query = query;
        if (searchView != null) {
            searchView.setQuery(query, false);
        }
    }

    private void loadData(List<Search> searches) {
        if (searches != null) {
            showData();
            resultsAdapter.swapResults(searches);
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
                searchView.setQuery(query, false);
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
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setIconified(false);
        searchView.setQueryHint(getString(R.string.search_query_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
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
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                dismiss();
                return false;
            }
        });
        if (!TextUtils.isEmpty(this.query)) {
            searchView.setQuery(this.query, false);
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

        if (!TextUtils.equals(query, this.query) && !query.isEmpty()) {
            searchViewModel.getSearch(query, adult);
            startLoading();
        }
        this.query = query;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void startLoading() {
        progressBar.setVisibility(View.VISIBLE);
        searchResults.setVisibility(GONE);
    }

    private void showData() {
        progressBar.setVisibility(GONE);
        searchResults.setVisibility(View.VISIBLE);
    }
}
