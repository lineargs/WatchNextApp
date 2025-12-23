package com.lineargs.watchnext.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.EpisodesQuery;
import com.lineargs.watchnext.data.SeasonsQuery;
// import com.lineargs.watchnext.jobs.ReminderFirebaseUtilities;
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

public class EpisodesActivity extends BaseTopActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 667, BACK_LOADER_ID = 888;

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

        if (getIntent().hasExtra(Constants.SEASON_ID) && getIntent().hasExtra(Constants.SERIE_ID) && getIntent().hasExtra(Constants.SEASON_NUMBER)) {
            String serieId = getIntent().getStringExtra(Constants.SERIE_ID);
            number = getIntent().getIntExtra(Constants.SEASON_NUMBER, -1);
            seasonId = getIntent().getStringExtra(Constants.SEASON_ID);
            SeasonUtils.syncEpisodes(this, serieId, number, seasonId);
            startLoading();
        }

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        getSupportLoaderManager().initLoader(BACK_LOADER_ID, null, this);
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
                    binding.container.setAdapter(mSectionsPagerAdapter);
                    binding.tabs.setupWithViewPager(binding.container);
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
        Picasso.get()
                .load(cursor.getString(SeasonsQuery.POSTER_PATH))
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
                if (airedAlready(details[3])) {
                    binding.notificationFab.setVisibility(GONE);
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
            return date1 != null && (System.currentTimeMillis() > date1.getTime());
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
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
            int intervalSeconds = getSeconds(System.currentTimeMillis(), details[3]);
             if (intervalSeconds != 0 && details != null) {
                 if (getArguments() != null) {
                      WorkManagerUtils.scheduleReminder(getContext(), intervalSeconds, Integer.parseInt(details[5]),
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

        private Cursor mCursor, mBackCursor;

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            mCursor.moveToPosition(position);
            String[] details = new String[9];
            details[0] = mCursor.getString(EpisodesQuery.NAME);
            details[1] = mCursor.getString(EpisodesQuery.STILL_PATH);
            details[2] = mCursor.getString(EpisodesQuery.VOTE_AVERAGE);
            details[3] = mCursor.getString(EpisodesQuery.RELEASE_DATE);
            details[4] = mCursor.getString(EpisodesQuery.OVERVIEW);
            details[5] = mCursor.getString(EpisodesQuery.EPISODE_ID);
            details[6] = mCursor.getString(EpisodesQuery.GUEST_STARS);
            details[7] = mCursor.getString(EpisodesQuery.DIRECTORS);
            details[8] = mCursor.getString(EpisodesQuery.WRITERS);

            String title = "";
            if (mBackCursor != null && mBackCursor.moveToFirst()) {
                title = mBackCursor.getString(SeasonsQuery.SHOW_NAME);
            }
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
            if (mCursor != null && mCursor.moveToPosition(position)) {
                return SeasonTools.getEpisodeFormat(EpisodesActivity.this, number, mCursor.getInt(EpisodesQuery.EPISODE_NUMBER));
            }
            return "";
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
