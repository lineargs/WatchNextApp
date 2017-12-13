package com.lineargs.watchnext.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.VideosAdapter;
import com.lineargs.watchnext.data.VideosQuery;
import com.lineargs.watchnext.sync.syncvideos.VideosSyncUtils;
import com.lineargs.watchnext.utils.ServiceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by goranminov on 26/11/2017.
 */

public class VideosFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, VideosAdapter.OnItemClick {

    private static final int LOADER_ID = 334;
    @BindView(R.id.videos_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.empty_videos)
    AppCompatTextView mEmptyVideos;
    private Uri mUri;
    private VideosAdapter mAdapter;
    private Handler handler;
    private Unbinder unbinder;

    public void setmUri(Uri uri) {
        this.mUri = uri;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        setupViews(view, savedInstanceState);
        return view;
    }

    private void setupViews(View view, Bundle savedState) {
        unbinder = ButterKnife.bind(this, view);
        handler = new Handler();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new VideosAdapter(getContext(), this);
        mRecyclerView.setAdapter(mAdapter);

        if (mUri != null && savedState == null) {
            VideosSyncUtils.syncVideos(getContext(), mUri.getLastPathSegment());
            startLoading();
        }

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void startLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mEmptyVideos.setVisibility(View.GONE);
    }

    private void showData() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyVideos.setVisibility(View.GONE);
    }

    private void showEmpty() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mEmptyVideos.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(getContext(),
                        mUri,
                        VideosQuery.VIDEO_PROJECTION,
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
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void OnClick(String key) {
        ServiceUtils.openYouTube(getContext(), getString(R.string.you_tube, key));
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
