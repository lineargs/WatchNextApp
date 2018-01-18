package com.lineargs.watchnext.ui;

import android.os.Bundle;

import com.lineargs.watchnext.R;

public class StatisticsActivity extends BaseTopActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setupActionBar();
        setupNavDrawer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
