package com.lineargs.watchnext.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lineargs.watchnext.R;

public class ReviewActivity extends BaseTopActivity {

    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        if (getIntent().hasExtra(MovieDetailsFragment.TITLE)) {
            title = getIntent().getStringExtra(MovieDetailsFragment.TITLE);
        }
        setupActionBar();
        setupNavDrawer();
        showSnackBar();
        if (savedInstanceState == null) {
            ReviewFragment reviewFragment = new ReviewFragment();
            reviewFragment.setmUri(getIntent().getData());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.review_fragment, reviewFragment)
                    .commit();
        }
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();
        setTitle(title);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setSubtitle(getString(R.string.subtitle_activity_reviews));
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
}
