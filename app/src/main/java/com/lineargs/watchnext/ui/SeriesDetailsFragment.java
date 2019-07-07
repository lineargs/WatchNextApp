package com.lineargs.watchnext.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.CastAdapter;
import com.lineargs.watchnext.adapters.TVDetailAdapter;
import com.lineargs.watchnext.data.Query;
import com.lineargs.watchnext.data.Series;
import com.lineargs.watchnext.data.SeriesDetailsViewModel;
import com.lineargs.watchnext.utils.Constants;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.lineargs.watchnext.utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

public class SeriesDetailsFragment extends Fragment implements CastAdapter.OnClick {


    @Nullable
    @BindView(R.id.cover_poster)
    ImageView mPosterPath;
    @Nullable
    @BindView(R.id.cover_backdrop)
    ImageView mBackdropPath;
    @BindView(R.id.star_fab)
    FloatingActionButton starFab;
    @BindView(R.id.cast_recycler_view)
    RecyclerView mCastRecyclerView;
    @BindView(R.id.cast_linear_layout)
    LinearLayout mCastLayout;
    @BindView(R.id.movie_details_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.google)
    Button googleButton;
    @BindView(R.id.youtube)
    Button youTubeButton;
    @BindView(R.id.google_play)
    Button googlePlayButton;
    @Nullable
    @BindView(R.id.videos)
    Button videosButton;
    private CastAdapter mCastAdapter;
    private TVDetailAdapter mAdapter;
    private int tmdbId;
    private String title = "";
    private Unbinder unbinder;
    private boolean mDualPane;

    public SeriesDetailsFragment() {
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            tmdbId = savedInstanceState.getInt(Constants.ID);
        }
        setHasOptionsMenu(true);
        View mRootView = inflater.inflate(R.layout.fragment_series_detail, container, false);
        setupViews(mRootView, savedInstanceState);
        return mRootView;
    }

    private void setupViews(View view, Bundle savedState) {
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new TVDetailAdapter(view.getContext());
        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mCastRecyclerView.setLayoutManager(mLayoutManager);
        mCastRecyclerView.setHasFixedSize(true);
        mCastAdapter = new CastAdapter(view.getContext(), this);
        mCastRecyclerView.setAdapter(mCastAdapter);

        if (getActivity().getIntent().getIntExtra(MainActivity.FAB_ID, 0) == 1) {
            starFab.setVisibility(View.GONE);
        }
        SeriesDetailsViewModel seriesDetailsViewModel = ViewModelProviders.of(this).get(SeriesDetailsViewModel.class);
        if (savedState == null) {
            seriesDetailsViewModel.getSeriesDetails(String.valueOf(tmdbId));
        }
        seriesDetailsViewModel.getSeries(tmdbId).observe(this, new Observer<Series>() {
            @Override
            public void onChanged(@Nullable Series series) {
                mAdapter.swapSeries(series);
                imageLoad(series);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View detailFrame = getActivity().findViewById(R.id.seasons_frame_layout);
        mDualPane = detailFrame != null && detailFrame.getVisibility() == View.VISIBLE;
        if (mDualPane && savedInstanceState == null) {
            SeasonsFragment fragment = new SeasonsFragment();
//            fragment.setmUri(DataContract.Seasons.buildSeasonUriWithId(Long.parseLong(mUri.getLastPathSegment())));
            getFragmentManager().beginTransaction()
                    .replace(R.id.seasons_frame_layout, fragment)
                    .commit();
            VideosTvFragment videosFragment = new VideosTvFragment();
//            videosFragment.setmUri(DataContract.Videos.buildVideoUriWithId(Long.parseLong(mUri.getLastPathSegment())));
            getFragmentManager().beginTransaction()
                    .replace(R.id.videos_frame_layout, videosFragment)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(Constants.ID, tmdbId);
    }

    @OnClick(R.id.star_fab)
    public void starFabFavorite() {
//        if (DbUtils.isFavorite(getContext(), Long.parseLong(mUri.getLastPathSegment()))) {
//            DbUtils.removeFromFavorites(getContext(), mUri);
//            Toast.makeText(getContext(), getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
//            starFab.setImageDrawable(Utils.starBorderImage(getContext()));
//        } else {
//            DbUtils.addTVToFavorites(getContext(), mUri);
//            Toast.makeText(getContext(), getString(R.string.toast_add_to_favorites), Toast.LENGTH_SHORT).show();
//            starFab.setImageDrawable(Utils.starImage(getContext()));
//        }
    }

    @OnClick(R.id.cast_header_layout)
    public void loadCast() {
//        Intent intent = (new Intent(getContext(), CreditsCastActivity.class));
//        Uri uri = DataContract.Credits.buildCastUriWithId(Long.parseLong(mUri.getLastPathSegment()));
//        intent.setData(uri);
//        intent.putExtra(Constants.TITLE, title);
//        startActivity(intent);
    }

    @Optional
    @OnClick(R.id.seasons)
    public void loadSeasons() {
//        Intent intent = new Intent(getContext(), SeasonsActivity.class);
//        Uri uri = DataContract.Seasons.buildSeasonUriWithId(Long.parseLong(mUri.getLastPathSegment()));
//        intent.setData(uri);
//        intent.putExtra(Constants.TITLE, title);
//        startActivity(intent);
    }

    @Optional
    @OnClick(R.id.videos)
    public void loadVideos() {
        Intent intent = new Intent(getContext(), VideosActivity.class);
        intent.putExtra(Constants.ID, tmdbId);
        intent.putExtra(Constants.TITLE, title);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_share, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
//                ServiceUtils.shareSerie(getActivity(), String.valueOf(id));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void imageLoad(Series series) {
        title = series.getTitle();
        ServiceUtils.setUpGoogleSearchButton(title, googleButton);
        ServiceUtils.setUpYouTubeButton(title, youTubeButton);
        ServiceUtils.setUpGooglePlayButton(title, googlePlayButton);
//        if (DbUtils.isFavorite(getContext(), id)) {
//            starFab.setImageDrawable(Utils.starImage(getContext()));
//        } else {
        starFab.setImageDrawable(Utils.starBorderImage(getContext()));
//        }
        if (mPosterPath != null) {
            Picasso.with(mPosterPath.getContext())
                    .load(series.getPosterPath())
                    .fit()
                    .into(mPosterPath);
        }
        if (mBackdropPath != null) {
            Picasso.with(mBackdropPath.getContext())
                    .load(series.getBackdropPath())
                    .fit()
                    .into(mBackdropPath);
        }
    }

    @Override
    public void onPersonClick(String id, String name) {
//        if (mDualPane) {
//            Intent intent = (new Intent(getContext(), CreditsCastActivity.class));
//            Uri uri = DataContract.Credits.buildCastUriWithId(Long.parseLong(mUri.getLastPathSegment()));
//            intent.setData(uri);
//            intent.putExtra(Constants.ID, id);
//            intent.putExtra(Constants.NAME, name);
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent(getContext(), PersonActivity.class);
//            intent.setData(DataContract.Person.buildPersonUriWithId(Long.parseLong(id)));
//            intent.putExtra(Constants.ID, id);
//            intent.putExtra(Constants.NAME, name);
//            startActivity(intent);
//        }
    }
}

