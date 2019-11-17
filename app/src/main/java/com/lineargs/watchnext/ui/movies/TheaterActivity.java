package com.lineargs.watchnext.ui.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.ui.base.BaseTopActivity;
import com.lineargs.watchnext.ui.search.SearchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TheaterActivity extends BaseTopActivity {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater);
        ButterKnife.bind(this);
        setupActionBar();
        setupNavDrawer();
        setupFragment(savedInstanceState);
    }

    private void setupFragment(Bundle savedInstance) {
        if (savedInstance == null) {
            TheaterFragment theaterFragment = new TheaterFragment();
            theaterFragment.setSwipeRefreshLayout(swipeRefreshLayout);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_movie_detail, theaterFragment).commit();
        }
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();
        setTitle(getString(R.string.title_activity_theater));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_activity_theater));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setDrawerSelectedItem(R.id.nav_theaters);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search) {
            Intent searchIntent = new Intent(this, SearchActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startSearchIntent(searchIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startSearchIntent(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
