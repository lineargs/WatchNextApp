package com.lineargs.watchnext.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SeasonsAdapter;
import com.lineargs.watchnext.data.SeasonsQuery;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Same old season fragment. Or not??? Hmmm....
 */

public class SeasonFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SeasonsAdapter.OnClickListener {

    private static final int LOADER_ID = 112;
    @BindView(R.id.season_recycler_view)
    RecyclerView mRecyclerView;
    private Uri mUri;
    private SeasonsAdapter mAdapter;
    private Unbinder unbinder;

    public SeasonFragment() {
    }

    public void setmUri(Uri uri) {
        mUri = uri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_season, container, false);
        setupViews(getContext(), view);
        return view;
    }

    private void setupViews(Context context, View view) {
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new SeasonsAdapter(context, this);
        mRecyclerView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(getContext(),
                        mUri,
                        SeasonsQuery.SEASON_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID:
                if (data != null && data.getCount() != 0) {
                    data.moveToFirst();
                    mAdapter.swapCursor(data);
                }
                break;
            default:
                throw new RuntimeException("Loader not implemented: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    //TODO Too many things going on here
    @Override
    public void OnClick(String seasonId, String seasonNumber, String serieId, String seasonTitle, String episodes) {
        EpisodesFragment fragment = new EpisodesFragment();
        fragment.setSeasonId(seasonId);
        fragment.setSerieId(serieId);
        fragment.setNumber(seasonNumber);
        getFragmentManager().beginTransaction()
                .replace(R.id.episodes_frame_layout, fragment)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
