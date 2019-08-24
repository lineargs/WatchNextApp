package com.lineargs.watchnext.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.EpisodesAdapter;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.EpisodesQuery;
import com.lineargs.watchnext.data.SeasonsQuery;
import com.lineargs.watchnext.sync.syncseries.SeasonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by goranminov on 09/12/2017.
 * <p>
 * ???
 */

public class EpisodesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 112, BACK_LOADER_ID = 456;
    @BindView(R.id.episodes_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    int number = -1;
    private EpisodesAdapter mAdapter;
    private Unbinder unbinder;
    private String seasonId = "", serieId = "";

    public EpisodesFragment() {
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public void setSerieId(String serieId) {
        this.serieId = serieId;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_episodes, container, false);
        setupViews(getContext(), view, savedInstanceState);
        return view;
    }

    private void setupViews(Context context, View view, Bundle savedState) {
        unbinder = ButterKnife.bind(this, view);
        if (savedState == null) {
            SeasonUtils.syncEpisodes(context, serieId, number, seasonId);
            startLoading();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        /* Simulating View Pager on our Recycler View */
        SnapHelper snapHelper = new PagerSnapHelper();
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        snapHelper.attachToRecyclerView(mRecyclerView);
        mAdapter = new EpisodesAdapter(context);
        mRecyclerView.setAdapter(mAdapter);

        getLoaderManager().initLoader(LOADER_ID, null, this);
        getLoaderManager().initLoader(BACK_LOADER_ID, null, this);
    }

    private void startLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void showData() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(getContext(),
                        DataContract.Episodes.CONTENT_URI,
                        EpisodesQuery.EPISODE_PROJECTION,
                        DataContract.Episodes.COLUMN_SEASON_ID + " = ? ",
                        new String[]{seasonId},
                        null);
            case BACK_LOADER_ID:
                return new CursorLoader(getContext(),
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
                    mAdapter.swapCursor(data);
                    showData();
                }
                break;
            case BACK_LOADER_ID:
                if (data != null && data.getCount() != 0) {
                    data.moveToFirst();
                    mAdapter.swapBackCursor(data);
                }
                break;
            default:
                throw new RuntimeException("Loader not implemented: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        mAdapter.swapBackCursor(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
