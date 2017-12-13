package com.lineargs.watchnext.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lineargs.watchnext.R;

/**
 * Created by goranminov on 26/11/2017.
 * <p>
 * Just some video activity. Nothing big going on
 */

public class VideosActivity extends BaseTopActivity {

    private String title = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        if (getIntent().hasExtra(MovieDetailsFragment.TITLE)) {
            title = getIntent().getStringExtra(MovieDetailsFragment.TITLE);
        }
        setupActionBar();
        setupNavDrawer();
        if (savedInstanceState == null) {
            VideosFragment videosFragment = new VideosFragment();
            videosFragment.setmUri(getIntent().getData());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.videos_fragment, videosFragment)
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
            actionBar.setSubtitle(getString(R.string.subtitle_activity_videos));
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
