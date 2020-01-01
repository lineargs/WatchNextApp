package com.lineargs.watchnext.ui.seasons;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.ui.base.BaseTopActivity;
import com.lineargs.watchnext.utils.Constants;

/**
 * Same old season activity
 */

public class SeasonActivity extends BaseTopActivity {

    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);
        if (getIntent().hasExtra(Constants.TITLE)) {
            title = getIntent().getStringExtra(Constants.TITLE);
        }
        setupActionBar();
        setupNavDrawer();
        if (savedInstanceState == null) {
            SeasonFragment fragment = new SeasonFragment();
            fragment.setTmdbId(getIntent().getIntExtra(Constants.ID, 0));
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