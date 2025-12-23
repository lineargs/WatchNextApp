package com.lineargs.watchnext.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.StatsUtils;

import com.lineargs.watchnext.databinding.ActivityStatisticsBinding;
import com.lineargs.watchnext.databinding.ItemStatisticsBinding;

public class StatisticsActivity extends BaseTopActivity {

    private ActivityStatisticsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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

        binding.container.setAdapter(fragmentPagerAdapter);
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

        private ItemStatisticsBinding binding;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            binding = ItemStatisticsBinding.inflate(inflater, container, false);
            binding.moviesCount.setText(String.valueOf(StatsUtils.getMoviesCount(getActivity())));
            binding.seriesCount.setText(String.valueOf(StatsUtils.getSeriesCount(getActivity())));
            return binding.getRoot();
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }
    }
}
