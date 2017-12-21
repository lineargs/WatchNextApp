package com.lineargs.watchnext.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lineargs.watchnext.R;

public class CreditsCastActivity extends BaseTopActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits_cast);
        setupActionBar();
        setupNavDrawer();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();
        setTitle(getString(R.string.title_activity_credits_cast));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_activity_credits_cast));
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
