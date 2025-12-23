package com.lineargs.watchnext.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.NetworkUtils;

import com.lineargs.watchnext.databinding.FragmentTabbedMoviesBinding;

/**
 * Created by goranminov on 16/11/2017.
 * <p>
 * Abstract class for the Tabbed Movies Fragments.
 * Note we do not use it anymore
 */

public abstract class BaseMoviesFragment extends BaseFragment {

    private FragmentTabbedMoviesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabbedMoviesBinding.inflate(inflater, container, false);
        setupViews();
        return binding.getRoot();
    }

    public void setupViews() {
        if (NetworkUtils.isConnected(getContext())) {
            startLoading();
        }
    }

    private void startLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.tabbedMoviesRecyclerView.setVisibility(View.INVISIBLE);
    }

    public void showData() {
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.tabbedMoviesRecyclerView.setVisibility(View.VISIBLE);
    }
}
