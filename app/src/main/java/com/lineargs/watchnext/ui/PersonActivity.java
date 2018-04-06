package com.lineargs.watchnext.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lineargs.watchnext.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        setupActionBar();
        if (savedInstanceState == null && getIntent().getData() != null) {
            PersonFragment fragment = new PersonFragment();
            fragment.setmUri(getIntent().getData());
            if (getIntent().hasExtra(CreditsCastFragment.ID)) {
                fragment.setId(getIntent().getStringExtra(CreditsCastFragment.ID));
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_person, fragment)
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
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
