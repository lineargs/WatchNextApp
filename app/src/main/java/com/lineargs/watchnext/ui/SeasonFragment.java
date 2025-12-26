package com.lineargs.watchnext.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SeasonsAdapter;

import com.lineargs.watchnext.databinding.FragmentSeasonBinding;

/**
 * Same old season fragment. Or not??? Hmmm....
 */

public class SeasonFragment extends Fragment implements SeasonsAdapter.OnClickListener {

    private FragmentSeasonBinding binding;
    private Uri mUri;
    private SeasonsAdapter mAdapter;
    private SeasonViewModel viewModel;

    public SeasonFragment() {
    }

    public void setmUri(Uri uri) {
        mUri = uri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSeasonBinding.inflate(inflater, container, false);
        setupViews(getContext());
        return binding.getRoot();
    }

    private void setupViews(Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.seasonRecyclerView.setLayoutManager(layoutManager);
        binding.seasonRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new SeasonsAdapter(context, this);
        binding.seasonRecyclerView.setAdapter(mAdapter);

        // Initialize ViewModel
        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(SeasonViewModel.class);
        if (mUri != null) {
            int seriesId = Integer.parseInt(mUri.getLastPathSegment());
            viewModel.setSerieId(seriesId);
            viewModel.getSeasons().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.Seasons>>() {
                 @Override
                 public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.Seasons> seasons) {
                     mAdapter.swapSeasons(seasons);
                 }
            });
        }
    }

    //TODO Too many things going on here
    @Override
    public void OnClick(String seasonId, int seasonNumber, String serieId, String episodes) {
        EpisodesFragment fragment = new EpisodesFragment();
        fragment.setSeasonId(seasonId);
        fragment.setSerieId(serieId);
        fragment.setNumber(seasonNumber);
        getFragmentManager().beginTransaction()
                .replace(R.id.episodes_frame_layout, fragment)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
