package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SeriesPopularAdapter;
import com.lineargs.watchnext.data.DataContract;

/**
 * Created by goranminov on 03/11/2017.
 * <p>
 * A fragment for our Tabbed Series View Pager
 */

public class SeriesPopularFragment extends SeriesListFragment implements SeriesPopularAdapter.OnItemClickListener {

    private SeriesPopularAdapter adapter;

    @Override
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(getContext(), SeriesDetailsActivity.class);
        intent.setData(uri);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public void onToggleFavorite(Uri uri, boolean isFavorite) {
        com.lineargs.watchnext.ui.SeriesViewModel viewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.lineargs.watchnext.ui.SeriesViewModel.class);
        viewModel.toggleFavorite(uri, isFavorite);
        if (isFavorite) {
            android.widget.Toast.makeText(getContext(), getString(R.string.toast_remove_from_favorites), android.widget.Toast.LENGTH_SHORT).show();
        } else {
            android.widget.Toast.makeText(getContext(), getString(R.string.toast_add_to_favorites), android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        adapter = new SeriesPopularAdapter(getActivity(), this);
        return adapter;
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull android.view.View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        com.lineargs.watchnext.ui.SeriesViewModel viewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.lineargs.watchnext.ui.SeriesViewModel.class);
        viewModel.getPopularSeries().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.PopularSerie>>() {
            @Override
            public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.PopularSerie> series) {
                if (series != null && !series.isEmpty()) {
                    adapter.swapSeries(series);
                    showData();
                }
            }
        });
        viewModel.getFavoriteSeriesIds().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<Integer>>() {
            @Override
            public void onChanged(java.util.List<Integer> ids) {
                if (ids != null) {
                    java.util.Set<Long> favorites = new java.util.HashSet<>();
                    for (Integer id : ids) {
                        favorites.add(id.longValue());
                    }
                    adapter.setFavorites(favorites);
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.tabbed_series_recycler_view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@androidx.annotation.NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.loadNextPopularPage();
                }
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading != null) {
                    androidx.swiperefreshlayout.widget.SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(isLoading);
                    }
                }
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null) {
                    android.widget.Toast.makeText(getContext(), message, android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

