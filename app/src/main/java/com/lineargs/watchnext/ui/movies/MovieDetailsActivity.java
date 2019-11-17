package com.lineargs.watchnext.ui.movies;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.lineargs.watchnext.R;
import com.lineargs.watchnext.ui.base.BaseTopActivity;
import com.lineargs.watchnext.utils.Constants;

public class MovieDetailsActivity extends BaseTopActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setupActionBar();
        setupNavDrawer();
        setupFragment(savedInstanceState);
    }

    @Override
    protected void onResume() {
        showSnackBar();
        super.onResume();
    }

    private void setupFragment(Bundle savedState) {
        if (savedState == null) {
            MovieDetailsFragment fragment = new MovieDetailsFragment();
            fragment.setTmdbId(getIntent().getIntExtra(Constants.ID, 0));
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_movie_detail, fragment).commit();
        }
    }

    @Override
    public void showSnackBar() {
        if (!isConnected()) {
            Snackbar.make(findViewById(R.id.list_coordinator_layout), getString(R.string.snackbar_no_connection), Snackbar.LENGTH_INDEFINITE)
                    .show();
        }
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();
        setTitle("");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }
    }

    @Override
    public void setDrawerIndicatorEnabled() {
        super.setDrawerIndicatorEnabled();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
