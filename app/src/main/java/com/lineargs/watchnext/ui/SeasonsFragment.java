package com.lineargs.watchnext.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
 * Created by goranminov on 27/11/2017.
 * <p>
 * Same old seasons fragment. Or not??? Hmmm....
 */

public class SeasonsFragment extends Fragment implements SeasonsAdapter.OnClickListener {

    @BindView(R.id.seasons_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.empty_seasons)
    AppCompatTextView empty;
    @Nullable
    @BindView(R.id.seasons_nested_view)
    NestedScrollView seasonsNestedView;
    private int tmdbId;
    private SeasonsAdapter adapter;
    private Unbinder unbinder;

    public SeasonsFragment() {
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
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
                loadViews(seasons);
            }
        });
    }

    private void loadViews(List<Seasons> seasons) {
        if (seasons != null) {
            if (seasons.size() != 0) {
                showData();
                adapter.swapSeasons(seasons);
            } else {
                showEmpty();
            }
        }
    }

    private void showData() {
        if (seasonsNestedView != null) {
            seasonsNestedView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }
    }

    private void showEmpty() {
        if (seasonsNestedView != null) {
            seasonsNestedView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(Constants.ID, tmdbId);
    }

    //TODO Too many things going on here
    @Override
    public void OnClick(String seasonId, int seasonNumber, String episodes) {
        if (isTablet(getContext())) {
            Intent intent = new Intent(getContext(), SeasonActivity.class);
            intent.putExtra(Constants.ID, tmdbId);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), EpisodesActivity.class);
            intent.putExtra(Constants.SEASON_ID, seasonId);
            intent.putExtra(Constants.SEASON_NUMBER, seasonNumber);
            intent.putExtra(Constants.EPISODES, episodes);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
