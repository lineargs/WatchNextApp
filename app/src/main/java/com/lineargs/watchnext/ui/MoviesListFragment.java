package com.lineargs.watchnext.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.Query;
import com.lineargs.watchnext.jobs.WorkManagerUtils;
import com.lineargs.watchnext.utils.NetworkUtils;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.lineargs.watchnext.databinding.FragmentListMoviesBinding;

public abstract class MoviesListFragment extends BaseFragment {

    private FragmentListMoviesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentListMoviesBinding.inflate(inflater, container, false);
        setupViews();
        return binding.getRoot();
    }

    private void setupViews() {
        if (NetworkUtils.isConnected(getContext())) {
            startLoading();
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        binding.tabbedMoviesRecyclerView.setLayoutManager(layoutManager);
        binding.tabbedMoviesRecyclerView.setAdapter(getAdapter());
        
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected(getContext())) {
                    binding.swipeRefreshLayout.setRefreshing(true);
                    WorkManagerUtils.syncImmediately(getContext());
                    // Refresh logic will be handled by LiveData observation in child fragments
                } else {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        super.onDestroyView();
        binding = null;
    }

    private void startLoading() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(true);
        }
    }

    public void showData() {
        binding.tabbedMoviesRecyclerView.setVisibility(View.VISIBLE);
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
    }

    public abstract RecyclerView.Adapter getAdapter();
}
