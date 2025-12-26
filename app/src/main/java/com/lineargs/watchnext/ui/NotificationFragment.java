package com.lineargs.watchnext.ui;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.ServiceUtils;

import com.lineargs.watchnext.databinding.FragmentNotificationBinding;

public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding binding;
    private Uri mUri;

    public NotificationFragment() {
    }

    public void setmUri(Uri uri) {
        mUri = uri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mUri != null) {
            int episodeId = Integer.parseInt(mUri.getLastPathSegment());
            EpisodeViewModel viewModel = new androidx.lifecycle.ViewModelProvider(this).get(EpisodeViewModel.class);
            viewModel.getEpisodeById(episodeId).observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<com.lineargs.watchnext.data.entity.Episodes>() {
                @Override
                public void onChanged(com.lineargs.watchnext.data.entity.Episodes episode) {
                    if (episode != null) {
                        loadData(episode);
                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadData(com.lineargs.watchnext.data.entity.Episodes episode) {
        binding.name.setText(episode.getName());
        binding.voteAverage.setText(String.valueOf(episode.getVoteAverage()));
        binding.releaseDate.setText(episode.getReleaseDate());
        binding.overview.setText(episode.getOverview());
        ServiceUtils.loadPicasso(binding.stillPath.getContext(), episode.getStillPath())
                .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                .centerInside()
                .into(binding.stillPath);
    }
}
