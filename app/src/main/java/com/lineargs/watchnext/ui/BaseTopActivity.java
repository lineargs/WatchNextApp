package com.lineargs.watchnext.ui;


import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;

/**
 * Created by goranminov on 05/11/2017.
 * <p>
 * Top navigation hierarchy activity dispaying the Nav Drawer
 * upon pressing up/home action bar button
 */

public abstract class BaseTopActivity extends BaseDrawerActivity {

    @Override
    protected void setupActionBar() {
        super.setupActionBar();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setupNavDrawer() {
        super.setupNavDrawer();
        /* Show a drawer indicator */
        setDrawerIndicatorEnabled();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggleDrawer(item) || super.onOptionsItemSelected(item);
    }


}
