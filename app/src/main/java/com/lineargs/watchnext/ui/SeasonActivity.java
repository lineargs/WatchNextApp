package com.lineargs.watchnext.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lineargs.watchnext.R;

/**
 * Same old season activity
 */

public class SeasonActivity extends BaseTopActivity {

    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);
        if (getIntent().hasExtra(MovieDetailsFragment.TITLE)) {
            title = getIntent().getStringExtra(MovieDetailsFragment.TITLE);
        }
        setupActionBar();
        setupNavDrawer();
        if (savedInstanceState == null) {
            SeasonFragment fragment = new SeasonFragment();
            fragment.setmUri(getIntent().getData());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.season_fragment, fragment)
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
