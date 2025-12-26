package com.lineargs.watchnext.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.EpisodesAdapter;
import com.lineargs.watchnext.sync.syncseries.SeasonUtils;

import com.lineargs.watchnext.databinding.ContentEpisodesBinding;

/**
 * Created by goranminov on 09/12/2017.
 * <p>
 * ???
 */

public class EpisodesFragment extends Fragment {

    private ContentEpisodesBinding binding;
    int number = -1;
    private EpisodesAdapter mAdapter;
    private String seasonId = "", serieId = "";
    private EpisodeViewModel viewModel;

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
        binding = ContentEpisodesBinding.inflate(inflater, container, false);
        setupViews(getContext(), savedInstanceState);
        return binding.getRoot();
    }

    private void setupViews(Context context, Bundle savedState) {
        if (savedState == null) {
            SeasonUtils.syncEpisodes(context, serieId, number, seasonId);
            startLoading();
        }
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SeasonUtils.syncEpisodes(getContext(), serieId, number, seasonId);
                startLoading();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        /* Simulating View Pager on our Recycler View */
        SnapHelper snapHelper = new PagerSnapHelper();
        binding.episodesRecyclerView.setLayoutManager(layoutManager);
        binding.episodesRecyclerView.setHasFixedSize(true);
        binding.episodesRecyclerView.setNestedScrollingEnabled(false);
        snapHelper.attachToRecyclerView(binding.episodesRecyclerView);
        mAdapter = new EpisodesAdapter(context);
        binding.episodesRecyclerView.setAdapter(mAdapter);

        // Initialize ViewModel
        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(EpisodeViewModel.class);
        viewModel.setSeasonId(seasonId);
        
        viewModel.getEpisodes().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.Episodes>>() {
            @Override
            public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.Episodes> episodes) {
                if (episodes != null && !episodes.isEmpty()) {
                    mAdapter.swapEpisodes(episodes);
                    showData();
                }
            }
        });

        viewModel.getSeason().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<com.lineargs.watchnext.data.entity.Seasons>() {
            @Override
            public void onChanged(com.lineargs.watchnext.data.entity.Seasons season) {
                if (season != null) {
                    mAdapter.swapSeason(season);
                }
            }
        });
    }

    private void startLoading() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(true);
        }
        binding.episodesRecyclerView.setVisibility(View.GONE);
    }

    private void showData() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
        binding.episodesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
