package com.lineargs.watchnext.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.databinding.FragmentScheduleBinding;
import com.lineargs.watchnext.ui.ScheduleViewModel;
import com.lineargs.watchnext.adapters.ScheduleAdapter;
import com.lineargs.watchnext.data.WatchNextDatabase;
import com.lineargs.watchnext.jobs.WorkManagerUtils;

public class ScheduleFragment extends Fragment {

    private FragmentScheduleBinding binding;
    private ScheduleAdapter mAdapter;
    private ScheduleViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        setupRecyclerView();
        
        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(ScheduleViewModel.class);
        viewModel.getUpcomingEpisodes().observe(getViewLifecycleOwner(), episodes -> {
            if (episodes == null || episodes.isEmpty()) {
                binding.scheduleRecyclerView.setVisibility(View.GONE);
                binding.emptyLayout.getRoot().setVisibility(View.VISIBLE);
            } else {
                binding.scheduleRecyclerView.setVisibility(View.VISIBLE);
                binding.emptyLayout.getRoot().setVisibility(View.GONE);
                mAdapter.submitList(episodes);
                
                // Proactive sync if posters are missing
                WatchNextDatabase.databaseWriteExecutor.execute(() -> {
                    WatchNextDatabase database = WatchNextDatabase.getDatabase(getContext());
                    if (database.upcomingEpisodesDao().countUpcomingEpisodesWithoutPosters() > 0) {
                        WorkManagerUtils.syncSubscriptionsImmediately(getContext());
                    }
                });
            }
        });
    }

    private void setupRecyclerView() {
        binding.scheduleRecyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
        mAdapter = new ScheduleAdapter(this::onEpisodeClick);
        binding.scheduleRecyclerView.setAdapter(mAdapter);
    }

    private void onEpisodeClick(com.lineargs.watchnext.data.entity.UpcomingEpisodes episode) {
        if (episode.getSeasonId() != 0) {
            Intent intent = new Intent(getContext(), EpisodesActivity.class);
            intent.putExtra(com.lineargs.watchnext.utils.Constants.SERIE_ID, String.valueOf(episode.getSeriesId()));
            intent.putExtra(com.lineargs.watchnext.utils.Constants.SEASON_NUMBER, episode.getSeasonNumber());
            intent.putExtra(com.lineargs.watchnext.utils.Constants.SEASON_ID, String.valueOf(episode.getSeasonId()));
            intent.putExtra(com.lineargs.watchnext.utils.Constants.EPISODE_NUMBER, episode.getEpisodeNumber());
            
            // Format episodes count for subtitle
            String episodesCount = getResources().getQuantityString(R.plurals.numberOfEpisodes, episode.getEpisodeCount(), episode.getEpisodeCount());
            intent.putExtra(com.lineargs.watchnext.utils.Constants.EPISODES, episodesCount);
            
            startActivity(intent);
        } else {
            // Fallback to series details if we don't have enough info yet
            android.content.Intent intent = new android.content.Intent(getContext(), SeriesDetailsActivity.class);
            intent.setData(com.lineargs.watchnext.data.DataContract.PopularSerieEntry.buildSerieUriWithId(episode.getSeriesId()));
            startActivity(intent);
        }
    }

    private void showData() {
        binding.emptyLayout.getRoot().setVisibility(android.view.View.GONE);
        binding.scheduleRecyclerView.setVisibility(android.view.View.VISIBLE);
    }

    private void hideData() {
        binding.emptyLayout.getRoot().setVisibility(android.view.View.VISIBLE);
        binding.scheduleRecyclerView.setVisibility(android.view.View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
