package com.lineargs.watchnext.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.EpisodesQuery;
import com.lineargs.watchnext.data.SeasonsQuery;
import com.lineargs.watchnext.jobs.ReminderFirebaseUtilities;
import com.lineargs.watchnext.sync.syncseries.SeasonUtils;
import com.lineargs.watchnext.tools.SeasonTools;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;

public class EpisodesActivity extends BaseTopActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 667, BACK_LOADER_ID = 888;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.container) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.cover_poster) ImageView seasonPoster;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
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

        if (getIntent().hasExtra(SeasonsFragment.SEASON_NUMBER) && getIntent().hasExtra(SeasonsFragment.EPISODES)) {
            title = SeasonTools.getSeasonString(this, getIntent().getIntExtra(SeasonsFragment.SEASON_NUMBER, -1));
            subtitle = getIntent().getStringExtra(SeasonsFragment.EPISODES);
        }

        setupActionBar();
        setupNavDrawer();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        if (getIntent().hasExtra(SeasonsFragment.SEASON_ID) && getIntent().hasExtra(SeasonsFragment.SERIE_ID) && getIntent().hasExtra(SeasonsFragment.SEASON_NUMBER)) {
            String serieId = getIntent().getStringExtra(SeasonsFragment.SERIE_ID);
            number = getIntent().getIntExtra(SeasonsFragment.SEASON_NUMBER, -1);
            seasonId = getIntent().getStringExtra(SeasonsFragment.SEASON_ID);
            SeasonUtils.syncEpisodes(this, serieId, number, seasonId);
            startLoading();
        }

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(BACK_LOADER_ID, null, this);
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(this,
                        DataContract.Episodes.CONTENT_URI,
                        EpisodesQuery.EPISODE_PROJECTION,
                        DataContract.Episodes.COLUMN_SEASON_ID + " = ? ",
                        new String[]{seasonId},
                        null);
            case BACK_LOADER_ID:
                return new CursorLoader(this,
                        DataContract.Seasons.CONTENT_URI,
                        SeasonsQuery.SEASON_PROJECTION,
                        DataContract.Seasons.COLUMN_SEASON_ID + " = ? ",
                        new String[]{seasonId},
                        null);
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID:
                if (data != null && data.getCount() != 0) {
                    data.moveToFirst();
                    mSectionsPagerAdapter.swapCursor(data);
                    showData();
                    viewPager.setAdapter(mSectionsPagerAdapter);
                    tabs.setupWithViewPager(viewPager);
                }
                break;
            case BACK_LOADER_ID:
                if (data != null && data.getCount() != 0) {
                    data.moveToFirst();
                    mSectionsPagerAdapter.swapBackCursor(data);
                    swapPosterCursor(data);
                }
                break;
            default:
                throw new RuntimeException("Loader not implemented: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mSectionsPagerAdapter.swapCursor(null);
        mSectionsPagerAdapter.swapBackCursor(null);
    }

    void swapPosterCursor(Cursor cursor) {
        Picasso.with(seasonPoster.getContext())
                .load(cursor.getString(SeasonsQuery.POSTER_PATH))
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
        private static final String ARG_DETAILS = "details";
        private static final String ARG_TITLE = "title";

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
        public static PlaceholderFragment newInstance(String title, String[] details) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putStringArray(ARG_DETAILS, details);
            args.putString(ARG_TITLE, title);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_episodes, container, false);
            unbinder = ButterKnife.bind(this, rootView);
            details = getArguments().getStringArray(ARG_DETAILS);

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
                    notification.setVisibility(GONE);
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
        public void setNotification() {
            int intervalSeconds = getSeconds(System.currentTimeMillis(), details[3]);
            if (intervalSeconds != 0 && details != null) {
                ReminderFirebaseUtilities.scheduleReminder(getContext(), intervalSeconds, Integer.parseInt(details[5]),
                        getArguments().getString(ARG_TITLE), details[0]);
                Toast.makeText(getContext(), getString(R.string.toast_notification_reminder), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Cursor mCursor, mBackCursor;

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            mCursor.moveToPosition(position);
            String [] details = new String[9];
            details[0] = mCursor.getString(EpisodesQuery.NAME);
            details[1] = mCursor.getString(EpisodesQuery.STILL_PATH);
            details[2] = mCursor.getString(EpisodesQuery.VOTE_AVERAGE);
            details[3] = mCursor.getString(EpisodesQuery.RELEASE_DATE);
            details[4] = mCursor.getString(EpisodesQuery.OVERVIEW);
            details[5] = mCursor.getString(EpisodesQuery.EPISODE_ID);
            details[6] = mCursor.getString(EpisodesQuery.GUEST_STARS);
            details[7] = mCursor.getString(EpisodesQuery.DIRECTORS);
            details[8] = mCursor.getString(EpisodesQuery.WRITERS);

            String title = mBackCursor.getString(SeasonsQuery.SHOW_NAME);
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(title, details);
        }

        @Override
        public int getCount() {
            if (mCursor == null) return 0;
            return mCursor.getCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            mCursor.moveToPosition(position);
            return SeasonTools.getEpisodeFormat(EpisodesActivity.this, number, mCursor.getInt(EpisodesQuery.EPISODE_NUMBER));
        }

        void swapCursor(Cursor cursor) {
            mCursor = cursor;
            notifyDataSetChanged();
        }

        void swapBackCursor(Cursor cursor) {
            mBackCursor = cursor;
            notifyDataSetChanged();
        }
    }
}
