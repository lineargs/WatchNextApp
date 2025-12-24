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


import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SeasonsAdapter;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.SeasonsQuery;
import com.lineargs.watchnext.utils.Constants;

import com.lineargs.watchnext.databinding.FragmentSeasonsBinding;

/**
 * Created by goranminov on 27/11/2017.
 * <p>
 * Same old seasons fragment. Or not??? Hmmm....
 */

public class SeasonsFragment extends Fragment implements SeasonsAdapter.OnClickListener {

    private FragmentSeasonsBinding binding;
    private Uri mUri;
    private Handler handler;
    private SeasonsAdapter mAdapter;
    private SeasonViewModel viewModel;

    public SeasonsFragment() {
    }

    public void setmUri(Uri uri) {
        mUri = uri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSeasonsBinding.inflate(inflater, container, false);
        setupViews(getContext(), savedInstanceState);
        return binding.getRoot();
    }

    private void setupViews(Context context, Bundle savedState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.seasonsRecyclerView.setLayoutManager(layoutManager);
        binding.seasonsRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new SeasonsAdapter(context, this);
        binding.seasonsRecyclerView.setAdapter(mAdapter);
        handler = new Handler();
        if (savedState == null) {
            startLoading();
        }

        // Initialize ViewModel
        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(SeasonViewModel.class);
        if (mUri != null) {
            int seriesId = Integer.parseInt(mUri.getLastPathSegment());
            viewModel.setSerieId(seriesId);
            viewModel.getSeasons().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.Seasons>>() {
                 @Override
                 public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.Seasons> seasons) {
                     mAdapter.swapSeasons(seasons);
                     if (seasons != null && !seasons.isEmpty()) {
                        handler.removeCallbacksAndMessages(null);
                        showData();
                    } else if (seasons != null) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showEmpty();
                            }
                        }, 5000);
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
        binding.seasonsRecyclerView.setVisibility(View.GONE);
        binding.emptySeasons.setVisibility(View.GONE);
    }

    private void showData() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
        binding.seasonsRecyclerView.setVisibility(View.VISIBLE);
        binding.emptySeasons.setVisibility(View.GONE);
    }

    private void showEmpty() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
        binding.seasonsRecyclerView.setVisibility(View.GONE);
        binding.emptySeasons.setVisibility(View.VISIBLE);
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
        binding = null;
    }
}
