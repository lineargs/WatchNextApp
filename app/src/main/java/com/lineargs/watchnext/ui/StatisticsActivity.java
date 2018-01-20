package com.lineargs.watchnext.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.StatsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StatisticsActivity extends BaseTopActivity {

    @BindView(R.id.container)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setupActionBar();
        setupNavDrawer();

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] fragments = new Fragment[]{
                    new FirstFragment()
            };

            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        setDrawerSelectedItem(R.id.nav_statistics);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static class FirstFragment extends BaseFragment {

        @BindView(R.id.movies_count)
        TextView movies;
        @BindView(R.id.series_count)
        TextView series;
        private Unbinder unbinder;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.item_statistics, container, false);
            unbinder = ButterKnife.bind(this, view);
            movies.setText(String.valueOf(StatsUtils.getMoviesCount(getActivity())));
            series.setText(String.valueOf(StatsUtils.getSeriesCount(getActivity())));
            return view;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }
    }
}
