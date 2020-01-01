package com.lineargs.watchnext.ui.episodes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.EpisodesAdapter;
import com.lineargs.watchnext.data.episodes.EpisodesViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by goranminov on 09/12/2017.
 * <p>
 * ???
 */

public class EpisodesFragment extends Fragment {

    @BindView(R.id.episodes_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private int number = -1;
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
        EpisodesViewModel episodesViewModel = ViewModelProviders.of(this).get(EpisodesViewModel.class);
        if (savedState == null) {
            episodesViewModel.syncEpisodes(serieId, number, seasonId);
            startLoading();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        /* Simulating View Pager on our Recycler View */
        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        snapHelper.attachToRecyclerView(recyclerView);
        EpisodesAdapter adapter = new EpisodesAdapter(context);
        recyclerView.setAdapter(adapter);
        episodesViewModel.getEpisodes(Integer.parseInt(seasonId)).observe(this, episodes -> {
            adapter.swapData(episodes);
            showData();
        });

    }

    private void startLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showData() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
