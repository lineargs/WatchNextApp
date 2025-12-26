package com.lineargs.watchnext.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.VideosAdapter;
import com.lineargs.watchnext.utils.ServiceUtils;

import com.lineargs.watchnext.databinding.FragmentVideosBinding;

/**
 * Created by goranminov on 26/11/2017.
 */

public class VideosFragment extends BaseFragment implements VideosAdapter.OnItemClick {

    private FragmentVideosBinding binding;
    private Uri mUri;
    private VideosAdapter mAdapter;
    private Handler handler;
    private VideosViewModel viewModel;


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

        // Initialize ViewModel
        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(VideosViewModel.class);
        if (mUri != null) {
            int movieId = Integer.parseInt(mUri.getLastPathSegment());
            viewModel.setMovieId(movieId);
            viewModel.getVideos().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.Videos>>() {
                @Override
                public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.Videos> videos) {
                    mAdapter.swapVideos(videos);
                     if (videos != null && !videos.isEmpty()) {
                        handler.removeCallbacksAndMessages(null);
                        showData();
                    } else if (videos != null) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showEmpty();
                            }
                        }, 3000);
                    }
                }
            });
        }
    }

    private void startLoading() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setEnabled(false);
            binding.swipeRefreshLayout.setRefreshing(true);
        }
        binding.videosRecyclerView.setVisibility(View.GONE);
        binding.emptyVideos.setVisibility(View.GONE);
    }

    private void showData() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
        binding.videosRecyclerView.setVisibility(View.VISIBLE);
        binding.emptyVideos.setVisibility(View.GONE);
    }

    private void showEmpty() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
        binding.videosRecyclerView.setVisibility(View.GONE);
        binding.emptyVideos.setVisibility(View.VISIBLE);
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
