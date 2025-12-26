package com.lineargs.watchnext.ui;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.Constants;

/**
 * Created by goranminov on 27/11/2017.
 * <p>
 * Same old seasons activity
 */

public class SeasonsActivity extends BaseTopActivity {

    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasons);
        if (getIntent().hasExtra(Constants.TITLE)) {
            title = getIntent().getStringExtra(Constants.TITLE);
        }
        setupActionBar();
        setupNavDrawer();
        if (savedInstanceState == null) {
            SeasonsFragment fragment = new SeasonsFragment();
            fragment.setmUri(getIntent().getData());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.seasons_fragment, fragment)
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
            actionBar.setSubtitle(getString(R.string.subtitle_activity_seasons));
        }
    }

    @Override
    public void setDrawerIndicatorEnabled() {
        super.setDrawerIndicatorEnabled();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back_white);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }
}
