package com.lineargs.watchnext.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.res.ColorStateList;
import androidx.preference.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.jobs.WorkManagerUtils;
import com.lineargs.watchnext.sync.syncseries.SeasonUtils;
import com.lineargs.watchnext.tools.SeasonTools;
import com.lineargs.watchnext.utils.Constants;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.lineargs.watchnext.databinding.ActivityEpisodesBinding;
import com.lineargs.watchnext.databinding.FragmentEpisodesBinding;

import static android.view.View.GONE;

public class EpisodesActivity extends BaseTopActivity {

    private ActivityEpisodesBinding binding;
    /**
     * The {@link androidx.viewpager.widget.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link androidx.fragment.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private String title = "", subtitle = "";
    private int number;
    private String seasonId = "";
    private String showName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEpisodesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra(Constants.SEASON_NUMBER) && getIntent().hasExtra(Constants.EPISODES)) {
            title = SeasonTools.getSeasonString(this, getIntent().getIntExtra(Constants.SEASON_NUMBER, -1));
            subtitle = getIntent().getStringExtra(Constants.EPISODES);
        }

        setupActionBar();
        setupNavDrawer();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Initialize ViewModel
        EpisodeViewModel viewModel = new androidx.lifecycle.ViewModelProvider(this).get(EpisodeViewModel.class);

        if (getIntent().hasExtra(Constants.SEASON_ID) && getIntent().hasExtra(Constants.SERIE_ID) && getIntent().hasExtra(Constants.SEASON_NUMBER)) {
            String serieId = getIntent().getStringExtra(Constants.SERIE_ID);
            number = getIntent().getIntExtra(Constants.SEASON_NUMBER, -1);
            seasonId = getIntent().getStringExtra(Constants.SEASON_ID);
            SeasonUtils.syncEpisodes(this, serieId, number, seasonId);
            startLoading();

            viewModel.setSeasonId(seasonId);
            viewModel.getEpisodes().observe(this, new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.Episodes>>() {
                @Override
                public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.Episodes> episodes) {
                    if (episodes != null && !episodes.isEmpty()) {
                        mSectionsPagerAdapter.swapEpisodes(episodes);
                        showData();
                        binding.container.setAdapter(mSectionsPagerAdapter);
                        binding.tabs.setupWithViewPager(binding.container);
                    }
                }
            });

            viewModel.getSeason().observe(this, new androidx.lifecycle.Observer<com.lineargs.watchnext.data.entity.Seasons>() {
                @Override
                public void onChanged(com.lineargs.watchnext.data.entity.Seasons season) {
                    if (season != null) {
                        setPoster(season.getPosterPath());
                        showName = season.getShowName();
                        mSectionsPagerAdapter.setShowName(showName);
                    }
                }
            });
        }
    }

    private void startLoading() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setEnabled(false);
            binding.swipeRefreshLayout.setRefreshing(true);
        }
        binding.container.setVisibility(GONE);
    }

    private void showData() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
        binding.container.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setupActionBar() {
        super.setupActionBar();
        setTitle(title);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setSubtitle(subtitle);
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

    void setPoster(String posterPath) {
        Picasso.get()
                .load(posterPath)
                .centerInside()
                .fit()
                .into(binding.coverPoster);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private FragmentEpisodesBinding binding;
        private String[] details;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String title, String[] details) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putStringArray(Constants.ARG_QUERY, details);
            args.putString(Constants.TITLE, title);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            binding = FragmentEpisodesBinding.inflate(inflater, container, false);
            View rootView = binding.getRoot();
            if (getArguments() != null) {
                details = getArguments().getStringArray(Constants.ARG_QUERY);
            }

            if (details != null) {
                binding.name.setText(details[0]);
                binding.voteAverage.setText(details[2]);
                binding.releaseDate.setText(details[3]);
                binding.overview.setText(details[4]);
                if (TextUtils.isEmpty(details[6])) {
                    binding.guestStarsContainer.setVisibility(GONE);
                } else {
                    binding.guestStars.setText(details[6]);
                }
                if (TextUtils.isEmpty(details[7])) {
                    binding.directorsContainer.setVisibility(GONE);
                } else {
                    binding.directors.setText(details[7]);
                }
                if (TextUtils.isEmpty(details[8])) {
                    binding.writersContainer.setVisibility(GONE);
                } else {
                    binding.writers.setText(details[8]);
                }
                if (TextUtils.isEmpty(details[3]) || airedAlready(details[3])) {
                    binding.notificationFab.setVisibility(GONE);
                } else {
                    updateFabUI();
                }
                ServiceUtils.loadPicasso(binding.stillPath.getContext(), details[1])
                        .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                        .centerInside()
                        .into(binding.stillPath);
            }
            binding.notificationFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNotification();
                }
            });
            return rootView;
        }

        private boolean airedAlready(String date) {
            Date date1 = null;
            if (date != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.date_pattern), Locale.getDefault());
                try {
                    date1 = simpleDateFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (date1 == null) return true;

            // Allow reminders on the release day if it's before 8:00 PM
            java.util.Calendar airTime = java.util.Calendar.getInstance();
            airTime.setTime(date1);
            airTime.set(java.util.Calendar.HOUR_OF_DAY, 20);
            airTime.set(java.util.Calendar.MINUTE, 0);
            airTime.set(java.util.Calendar.SECOND, 0);

            return System.currentTimeMillis() > airTime.getTimeInMillis();
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }

        private int getSeconds(long today, String date) {
            if (TextUtils.isEmpty(date)) return 0;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.date_pattern), Locale.getDefault());
            Date releaseDay = null;
            try {
                releaseDay = simpleDateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (releaseDay != null) {
                java.util.Calendar airTime = java.util.Calendar.getInstance();
                airTime.setTime(releaseDay);
                airTime.set(java.util.Calendar.HOUR_OF_DAY, 20);
                airTime.set(java.util.Calendar.MINUTE, 0);
                airTime.set(java.util.Calendar.SECOND, 0);

                long delayMillis = airTime.getTimeInMillis() - today;
                return delayMillis > 0 ? (int) TimeUnit.MILLISECONDS.toSeconds(delayMillis) : 0;
            } else {
                return 0;
            }
        }

        public void setNotification() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.POST_NOTIFICATIONS)) {
                        Snackbar.make(binding.notificationFab, getString(R.string.snackbar_notifications_required), Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.snackbar_grant), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                                    }
                                })
                                .show();
                    } else {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        if (sharedPreferences.getBoolean("PREF_PERMISSION_REQUESTED", false)) {
                            Snackbar.make(binding.notificationFab, getString(R.string.snackbar_notifications_disabled), Snackbar.LENGTH_LONG)
                                    .setAction(getString(R.string.snackbar_settings), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                            intent.setData(uri);
                                            startActivity(intent);
                                        }
                                    })
                                    .show();
                        } else {
                            sharedPreferences.edit().putBoolean("PREF_PERMISSION_REQUESTED", true).apply();
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                        }
                    }
                    return;
                }
            }
            int id = Integer.parseInt(details[5]);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean isScheduled = sp.getBoolean("reminder_" + id, false);

            if (isScheduled) {
                WorkManagerUtils.cancelReminder(getContext(), id);
                sp.edit().remove("reminder_" + id).apply();
                Toast.makeText(getContext(), R.string.toast_notification_cancelled, Toast.LENGTH_SHORT).show();
            } else {
                int intervalSeconds = getSeconds(System.currentTimeMillis(), details[3]);
                if (intervalSeconds != 0 && details != null) {
                    if (getArguments() != null) {
                        WorkManagerUtils.scheduleReminder(getContext(), intervalSeconds, id,
                                getArguments().getString(Constants.TITLE), details[0]);
                    }
                    sp.edit().putBoolean("reminder_" + id, true).apply();
                    Toast.makeText(getContext(), getString(R.string.toast_notification_reminder), Toast.LENGTH_SHORT).show();
                }
            }
            updateFabUI();
        }

        private void updateFabUI() {
            int id = Integer.parseInt(details[5]);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean isScheduled = sp.getBoolean("reminder_" + id, false);
            if (isScheduled) {
                binding.notificationFab.setImageResource(R.drawable.icon_cancel_black);
                binding.notificationFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorGrey)));
            } else {
                binding.notificationFab.setImageResource(R.drawable.icon_tv_black);
                binding.notificationFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorYellow)));
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private java.util.List<com.lineargs.watchnext.data.entity.Episodes> episodes;
        private String showName = "";

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            com.lineargs.watchnext.data.entity.Episodes episode = episodes.get(position);
            String[] details = new String[9];
            details[0] = episode.getName();
            details[1] = episode.getStillPath();
            details[2] = episode.getVoteAverage();
            details[3] = episode.getReleaseDate();
            details[4] = episode.getOverview();
            details[5] = String.valueOf(episode.getEpisodeId());
            details[6] = episode.getGuestStars();
            details[7] = episode.getDirectors();
            details[8] = episode.getWriters();

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(showName, details);
        }

        @Override
        public int getCount() {
            if (episodes == null) return 0;
            return episodes.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (episodes != null) {
                return SeasonTools.getEpisodeFormat(EpisodesActivity.this, number, episodes.get(position).getEpisodeNumber());
            }
            return "";
        }

        void swapEpisodes(java.util.List<com.lineargs.watchnext.data.entity.Episodes> episodes) {
            this.episodes = episodes;
            notifyDataSetChanged();
        }

        void setShowName(String name) {
            this.showName = name;
            // No need to notify if it's passed during instantiation, but if it updates later, fragments might not update title.
            // But usually ShowName is constant.
        }
    }
}
