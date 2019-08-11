package com.lineargs.watchnext.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SeasonsAdapter;
import com.lineargs.watchnext.data.Seasons;
import com.lineargs.watchnext.data.SeasonsViewModel;
import com.lineargs.watchnext.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Same old season fragment. Or not??? Hmmm....
 */

public class SeasonFragment extends Fragment implements SeasonsAdapter.OnClickListener {

    @BindView(R.id.season_recycler_view)
    RecyclerView recyclerView;
    private int tmdbId;
    private SeasonsAdapter adapter;
    private Unbinder unbinder;

    public SeasonFragment() {
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_season, container, false);
        setupViews(getContext(), view, savedInstanceState);
        return view;
    }

    private void setupViews(Context context, View view, Bundle savedState) {
        unbinder = ButterKnife.bind(this, view);
        if (savedState != null) {
            tmdbId = savedState.getInt(Constants.ID);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new SeasonsAdapter(context, this);
        recyclerView.setAdapter(adapter);
        SeasonsViewModel seasonsViewModel = ViewModelProviders.of(this).get(SeasonsViewModel.class);
        seasonsViewModel.getSeasons(tmdbId).observe(this, new Observer<List<Seasons>>() {
            @Override
            public void onChanged(@Nullable List<Seasons> seasons) {
                adapter.swapSeasons(seasons);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(Constants.ID, tmdbId);
    }

    //TODO Too many things going on here
    @Override
    public void OnClick(String seasonId, int seasonNumber, String episodes) {
        EpisodesFragment fragment = new EpisodesFragment();
        fragment.setSeasonId(seasonId);
        fragment.setNumber(seasonNumber);
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.episodes_frame_layout, fragment)
                    .commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
