package com.lineargs.watchnext.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.Constants;

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
        if (getIntent().hasExtra(Constants.TITLE)) {
            title = getIntent().getStringExtra(Constants.TITLE);
        }
        setupActionBar();
        setupNavDrawer();
        if (savedInstanceState == null) {
            VideosFragment videosFragment = new VideosFragment();
            videosFragment.setTmdbId(getIntent().getIntExtra(Constants.ID, 0));
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
