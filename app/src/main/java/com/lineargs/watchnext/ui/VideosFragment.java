package com.lineargs.watchnext.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.VideosAdapter;
import com.lineargs.watchnext.data.Videos;
import com.lineargs.watchnext.data.VideosViewModel;
import com.lineargs.watchnext.utils.Constants;
import com.lineargs.watchnext.utils.ServiceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by goranminov on 26/11/2017.
 */

public class VideosFragment extends BaseFragment implements VideosAdapter.OnItemClick {

    private int tmdbId;
    @BindView(R.id.videos_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.videos_nested_view)
    NestedScrollView mNestedView;
    @BindView(R.id.empty_videos)
    AppCompatTextView mEmptyVideos;
    private VideosAdapter mAdapter;
    private Unbinder unbinder;

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        setupViews(view, savedInstanceState);
        return view;
    }

    private void setupViews(View view, Bundle savedState) {
        unbinder = ButterKnife.bind(this, view);
        if (savedState != null) {
            tmdbId = savedState.getInt(Constants.ID);
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new VideosAdapter(view.getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
        VideosViewModel videosViewModel = ViewModelProviders.of(this).get(VideosViewModel.class);
        videosViewModel.getVideos(tmdbId).observe(this, new Observer<List<Videos>>() {
            @Override
            public void onChanged(@Nullable List<Videos> videos) {
                loadViews(videos);
            }
        });

    }

    private void showEmpty() {
        mNestedView.setVisibility(View.INVISIBLE);
        mEmptyVideos.setVisibility(View.VISIBLE);
    }

    private void showData() {
        mNestedView.setVisibility(View.VISIBLE);
        mEmptyVideos.setVisibility(View.INVISIBLE);
    }

    private void loadViews(List<Videos> videos) {
        if (videos != null) {
            if (videos.size() != 0) {
                showData();
                mAdapter.swapVideos(videos);
            } else {
                showEmpty();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(Constants.ID, tmdbId);
    }

    @Override
    public void OnClick(String key) {
        ServiceUtils.openYouTube(getContext(), getString(R.string.you_tube, key));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
