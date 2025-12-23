package com.lineargs.watchnext.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SeasonsAdapter;
import com.lineargs.watchnext.data.SeasonsQuery;

import com.lineargs.watchnext.databinding.FragmentSeasonBinding;

/**
 * Same old season fragment. Or not??? Hmmm....
 */

public class SeasonFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SeasonsAdapter.OnClickListener {

    private static final int LOADER_ID = 112;
    private FragmentSeasonBinding binding;
    private Uri mUri;
    private SeasonsAdapter mAdapter;

    public SeasonFragment() {
    }

    public void setmUri(Uri uri) {
        mUri = uri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSeasonBinding.inflate(inflater, container, false);
        setupViews(getContext());
        return binding.getRoot();
    }

    private void setupViews(Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.seasonRecyclerView.setLayoutManager(layoutManager);
        binding.seasonRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new SeasonsAdapter(context, this);
        binding.seasonRecyclerView.setAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @NonNull
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
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
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
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    //TODO Too many things going on here
    @Override
    public void OnClick(String seasonId, int seasonNumber, String serieId, String episodes) {
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
        binding = null;
    }
}
