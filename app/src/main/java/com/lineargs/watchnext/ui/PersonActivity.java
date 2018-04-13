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

    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        ButterKnife.bind(this);
        if (savedInstanceState == null && getIntent().getData() != null) {
            PersonFragment fragment = new PersonFragment();
            fragment.setmUri(getIntent().getData());
            if (getIntent().hasExtra(CreditsCastFragment.ID)) {
                fragment.setId(getIntent().getStringExtra(CreditsCastFragment.ID));
            }
            if (getIntent().hasExtra(CreditsCastFragment.NAME)) {
                name = getIntent().getStringExtra(CreditsCastFragment.NAME);
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_person, fragment)
                    .commit();
        }
        if (savedInstanceState != null) {
            name = savedInstanceState.getString(CreditsCastFragment.NAME);
        }
        setupActionBar();
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();
        setTitle(name);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name);
        }
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CreditsCastFragment.NAME, name);
    }
}
