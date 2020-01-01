package com.lineargs.watchnext.ui.episodes;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.episodes.Episodes;
import com.lineargs.watchnext.data.episodes.EpisodesViewModel;
import com.lineargs.watchnext.jobs.ReminderFirebaseUtilities;
import com.lineargs.watchnext.tools.SeasonTools;
import com.lineargs.watchnext.ui.base.BaseTopActivity;
import com.lineargs.watchnext.utils.Constants;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;

public class EpisodesActivity extends BaseTopActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.container)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.cover_poster)
    ImageView seasonPoster;
    /**
     * The {@link androidx.viewpager.widget.ViewPager} that will provide
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);
        ButterKnife.bind(this);

        if (getIntent().hasExtra(Constants.SEASON_NUMBER) && getIntent().hasExtra(Constants.EPISODES)) {
            title = SeasonTools.getSeasonString(this, getIntent().getIntExtra(Constants.SEASON_NUMBER, -1));
            subtitle = getIntent().getStringExtra(Constants.EPISODES);
        }

        setupActionBar();
        setupNavDrawer();

        EpisodesViewModel episodesViewModel = ViewModelProviders.of(this).get(EpisodesViewModel.class);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mSectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
        if (getIntent().hasExtra(Constants.SEASON_ID) && getIntent().hasExtra(Constants.SERIE_ID) && getIntent().hasExtra(Constants.SEASON_NUMBER)) {
            String serieId = getIntent().getStringExtra(Constants.SERIE_ID);
            number = getIntent().getIntExtra(Constants.SEASON_NUMBER, -1);
            seasonId = getIntent().getStringExtra(Constants.SEASON_ID);
            episodesViewModel.syncEpisodes(serieId, number, seasonId);
            startLoading();
        }
        episodesViewModel.getEpisodes(Integer.parseInt(seasonId)).observe(this, episodes -> {
            mSectionsPagerAdapter.swapData(episodes);
            showData();
        });
    }

    private void startLoading() {
        progressBar.setVisibility(View.VISIBLE);
        viewPager.setVisibility(GONE);
    }

    private void showData() {
        progressBar.setVisibility(GONE);
        viewPager.setVisibility(View.VISIBLE);
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
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    void swapPoster(String posterPath) {
        Picasso.with(seasonPoster.getContext())
                .load(posterPath)
                .centerInside()
                .fit()
                .into(seasonPoster);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        @BindView(R.id.name)
        AppCompatTextView name;
        @BindView(R.id.still_path)
        ImageView stillPath;
        @BindView(R.id.vote_average)
        AppCompatTextView voteAverage;
        @BindView(R.id.release_date)
        AppCompatTextView releaseDate;
        @BindView(R.id.overview)
        AppCompatTextView overview;
        @BindView(R.id.guest_stars)
        AppCompatTextView guestStars;
        @BindView(R.id.directors)
        AppCompatTextView directors;
        @BindView(R.id.writers)
        AppCompatTextView writers;
        @BindView(R.id.guest_stars_container)
        LinearLayout guestStarsContainer;
        @BindView(R.id.directors_container)
        LinearLayout directorsContainer;
        @BindView(R.id.writers_container)
        LinearLayout writersContainer;
        @BindView(R.id.notification_fab)
        FloatingActionButton notification;
        private Unbinder unbinder;
        private String[] details;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        static PlaceholderFragment newInstance(String title, String[] details) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putStringArray(Constants.ARG_QUERY, details);
            args.putString(Constants.TITLE, title);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_episodes, container, false);
            unbinder = ButterKnife.bind(this, rootView);
            if (getArguments() != null) {
                details = getArguments().getStringArray(Constants.ARG_QUERY);
            }

            if (details != null) {
                name.setText(details[0]);
                voteAverage.setText(details[2]);
                releaseDate.setText(details[3]);
                overview.setText(details[4]);
                if (TextUtils.isEmpty(details[6])) {
                    guestStarsContainer.setVisibility(GONE);
                } else {
                    guestStars.setText(details[6]);
                }
                if (TextUtils.isEmpty(details[7])) {
                    directorsContainer.setVisibility(GONE);
                } else {
                    directors.setText(details[7]);
                }
                if (TextUtils.isEmpty(details[8])) {
                    writersContainer.setVisibility(GONE);
                } else {
                    writers.setText(details[8]);
                }
                if (airedAlready(details[3])) {
                    notification.hide();
                }
                ServiceUtils.loadPicasso(stillPath.getContext(), details[1])
                        .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                        .centerInside()
                        .into(stillPath);
            }
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
            return date1 != null && (System.currentTimeMillis() > date1.getTime());
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            unbinder.unbind();
        }

        private int getSeconds(long today, String date) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getString(R.string.date_pattern), Locale.getDefault());
            Date releaseDay = null;
            try {
                releaseDay = simpleDateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (releaseDay != null) {
                return (int) (TimeUnit.MILLISECONDS.toSeconds(releaseDay.getTime() - today));
            } else {
                return 0;
            }
        }

        @OnClick(R.id.notification_fab)
        void setNotification() {
            int intervalSeconds = getSeconds(System.currentTimeMillis(), details[3]);
            if (intervalSeconds != 0 && details != null) {
                if (getArguments() != null) {
                    ReminderFirebaseUtilities.scheduleReminder(getContext(), intervalSeconds, Integer.parseInt(details[5]),
                            getArguments().getString(Constants.TITLE), details[0]);
                }
                Toast.makeText(getContext(), getString(R.string.toast_notification_reminder), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Episodes> episodes;

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Episodes currentEpisode = episodes.get(position);
            String[] details = new String[9];
            details[0] = currentEpisode.getName();
            details[1] = currentEpisode.getStillPath();
            details[2] = currentEpisode.getVoteAverage();
            details[3] = currentEpisode.getReleaseDate();
            details[4] = currentEpisode.getOverview();
            details[5] = String.valueOf(currentEpisode.getId());
            details[6] = currentEpisode.getGuestStars();
            details[7] = currentEpisode.getDirectors();
            details[8] = currentEpisode.getWriters();

            String title = String.valueOf(currentEpisode.getSeasonNumber());
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(title, details);
        }

        @Override
        public int getCount() {
            if (episodes == null) return 0;
            return episodes.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Episodes episode = episodes.get(position);
            return SeasonTools.getEpisodeFormat(EpisodesActivity.this, number, episode.getEpisodeNumber());
        }

        void swapData(List<Episodes> episodes) {
            this.episodes = episodes;
            notifyDataSetChanged();
        }
    }
}
