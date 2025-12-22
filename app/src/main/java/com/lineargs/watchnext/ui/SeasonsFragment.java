package com.lineargs.watchnext.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SeasonsAdapter;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.SeasonsQuery;
import com.lineargs.watchnext.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by goranminov on 27/11/2017.
 * <p>
 * Same old seasons fragment. Or not??? Hmmm....
 */

public class SeasonsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SeasonsAdapter.OnClickListener {

    private static final int LOADER_ID = 112;
    @BindView(R.id.seasons_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.empty_seasons)
    AppCompatTextView mEmpty;
    private Uri mUri;
    private Handler handler;
    private SeasonsAdapter mAdapter;
    private Unbinder unbinder;

    public SeasonsFragment() {
    }

    public void setmUri(Uri uri) {
        mUri = uri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seasons, container, false);
        setupViews(getContext(), view, savedInstanceState);
        return view;
    }

    private void setupViews(Context context, View view, Bundle savedState) {
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new SeasonsAdapter(context, this);
        mRecyclerView.setAdapter(mAdapter);
        handler = new Handler();
        if (savedState == null) {
            startLoading();
        }
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void startLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
    }

    private void showData() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmpty.setVisibility(View.GONE);
    }

    private void showEmpty() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    @NonNull
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
                    handler.removeCallbacksAndMessages(null);
                    data.moveToFirst();
                    mAdapter.swapCursor(data);
                    showData();
                } else if (data != null && data.getCount() == 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showEmpty();
                        }
                    }, 5000);

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

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    //TODO Too many things going on here
    @Override
    public void OnClick(String seasonId, int seasonNumber, String serieId, String episodes) {
        if (isTablet(getContext())) {
            Intent intent = new Intent(getContext(), SeasonActivity.class);
            Uri uri = DataContract.Seasons.buildSeasonUriWithId(Long.parseLong(serieId));
            intent.setData(uri);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), EpisodesActivity.class);
            intent.putExtra(Constants.SEASON_ID, seasonId);
            intent.putExtra(Constants.SEASON_NUMBER, seasonNumber);
            intent.putExtra(Constants.SERIE_ID, serieId);
            intent.putExtra(Constants.EPISODES, episodes);
            startActivity(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
