package com.lineargs.watchnext.ui.person;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.ui.base.BaseActivity;
import com.lineargs.watchnext.utils.Constants;

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
        if (savedInstanceState == null) {
            PersonFragment fragment = new PersonFragment();
            if (getIntent().hasExtra(Constants.ID)) {
                fragment.setId(getIntent().getStringExtra(Constants.ID));
            }
            if (getIntent().hasExtra(Constants.NAME)) {
                name = getIntent().getStringExtra(Constants.NAME);
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_person, fragment)
                    .commit();
        }
        if (savedInstanceState != null) {
            name = savedInstanceState.getString(Constants.NAME);
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
        outState.putString(Constants.NAME, name);
    }
}