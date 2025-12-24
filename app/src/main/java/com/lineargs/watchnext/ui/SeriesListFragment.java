package com.lineargs.watchnext.ui;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.utils.NetworkUtils;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.lineargs.watchnext.utils.WorkManagerUtils;

import com.lineargs.watchnext.databinding.FragmentListSeriesBinding;

public abstract class SeriesListFragment extends BaseFragment {

    private FragmentListSeriesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentListSeriesBinding.inflate(inflater, container, false);
        setupViews();
        return binding.getRoot();
    }

    private void setupViews() {
        if (NetworkUtils.isConnected(getContext())) {
            startLoading();
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        binding.tabbedSeriesRecyclerView.setLayoutManager(layoutManager);
        binding.tabbedSeriesRecyclerView.setAdapter(getAdapter());

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected(getContext())) {
                    binding.swipeRefreshLayout.setRefreshing(true);
                    WorkManagerUtils.syncImmediately(getContext());
                    // Refresh logic handled by LiveData in subclasses
                } else {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void startLoading() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(true);
        }
    }

    public void showData() {
        binding.tabbedSeriesRecyclerView.setVisibility(View.VISIBLE);
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
    }

    public abstract RecyclerView.Adapter getAdapter();
}
