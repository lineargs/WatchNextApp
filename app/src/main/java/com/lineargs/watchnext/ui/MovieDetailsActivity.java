package com.lineargs.watchnext.ui;

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.lineargs.watchnext.R;

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
            fragment.setmUri(getIntent().getData());
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
