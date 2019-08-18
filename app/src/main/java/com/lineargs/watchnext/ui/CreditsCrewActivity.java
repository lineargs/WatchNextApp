package com.lineargs.watchnext.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.lineargs.watchnext.R;

public class CreditsCrewActivity extends BaseTopActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits_crew);
        setupActionBar();
        setupNavDrawer();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();
        setTitle(getString(R.string.title_activity_credits_crew));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_activity_credits_crew));
        }
    }

    @Override
    public void setDrawerIndicatorEnabled() {
        super.setDrawerIndicatorEnabled();
        /* We change the menu icon with an arrow so the user
         * can navigate back and still able to open the Nav
         * Drawer while swiping right from left
         */
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
