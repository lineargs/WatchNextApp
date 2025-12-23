package com.lineargs.watchnext.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.VideosAdapter;
import com.lineargs.watchnext.data.VideosQuery;
import com.lineargs.watchnext.utils.ServiceUtils;

import com.lineargs.watchnext.databinding.FragmentVideosBinding;

/**
 * Created by goranminov on 26/11/2017.
 */

public class VideosFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, VideosAdapter.OnItemClick {

    private static final int LOADER_ID = 334;
    private FragmentVideosBinding binding;
    private Uri mUri;
    private VideosAdapter mAdapter;
    private Handler handler;

    public void setmUri(Uri uri) {
        this.mUri = uri;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVideosBinding.inflate(inflater, container, false);
        setupViews(savedInstanceState);
        return binding.getRoot();
    }

    private void setupViews(Bundle savedState) {
        handler = new Handler();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        binding.videosRecyclerView.setLayoutManager(layoutManager);
        binding.videosRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new VideosAdapter(getContext(), this);
        binding.videosRecyclerView.setAdapter(mAdapter);

        if (savedState == null) {
            startLoading();
        }

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void startLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.videosRecyclerView.setVisibility(View.GONE);
        binding.emptyVideos.setVisibility(View.GONE);
    }

    private void showData() {
        binding.progressBar.setVisibility(View.GONE);
        binding.videosRecyclerView.setVisibility(View.VISIBLE);
        binding.emptyVideos.setVisibility(View.GONE);
    }

    private void showEmpty() {
        binding.progressBar.setVisibility(View.GONE);
        binding.videosRecyclerView.setVisibility(View.GONE);
        binding.emptyVideos.setVisibility(View.VISIBLE);
    }

    @NonNull
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
                    }, 3000);

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
        binding = null;
    }
}
