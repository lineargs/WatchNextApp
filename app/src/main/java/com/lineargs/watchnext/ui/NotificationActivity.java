package com.lineargs.watchnext.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.lineargs.watchnext.R;

/**
 * Created by goranminov on 01/12/2017.
 * <p>
 * We use this activity to show the episode detail for the PendingIntent
 * from the Notification
 */

public class NotificationActivity extends BaseTopActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setupActionBar();
        setupNavDrawer();
        if (savedInstanceState == null) {
            NotificationFragment fragment = new NotificationFragment();
            fragment.setmUri(getIntent().getData());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.notification_fragment, fragment)
                    .commit();
        }
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();
        setTitle("");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
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
