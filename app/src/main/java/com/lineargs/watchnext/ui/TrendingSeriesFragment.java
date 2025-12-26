package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SeriesPopularAdapter;
import com.lineargs.watchnext.data.entity.PopularSerie;
import com.lineargs.watchnext.databinding.FragmentTrendingBinding;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrendingSeriesFragment extends BaseFragment implements SeriesPopularAdapter.OnItemClickListener {

    private FragmentTrendingBinding binding;
    private SeriesPopularAdapter adapter;
    private TrendingViewModel viewModel;
    private SeriesViewModel seriesViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTrendingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        viewModel = new ViewModelProvider(requireActivity()).get(TrendingViewModel.class);
        seriesViewModel = new ViewModelProvider(this).get(SeriesViewModel.class);

        viewModel.getTrendingSeries().observe(getViewLifecycleOwner(), new Observer<List<PopularSerie>>() {
            @Override
            public void onChanged(List<PopularSerie> series) {
                if (series != null) {
                    adapter.swapSeries(series);
                }
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading != null) {
                    binding.swipeRefreshLayout.setRefreshing(isLoading);
                }
            }
        });

        // Observe favorites to update the stars
        seriesViewModel.getFavoriteSeriesIds().observe(getViewLifecycleOwner(), new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> ids) {
                if (ids != null) {
                    Set<Long> favorites = new HashSet<>();
                    for (Integer id : ids) {
                        favorites.add(id.longValue());
                    }
                    adapter.setFavorites(favorites);
                }
            }
        });
    }

    private void setupViews() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        binding.trendingRecyclerView.setLayoutManager(layoutManager);
        adapter = new SeriesPopularAdapter(requireActivity(), this);
        binding.trendingRecyclerView.setAdapter(adapter);

        binding.trendingRecyclerView.addOnScrollListener(new androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull androidx.recyclerview.widget.RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.loadNextTrendingSeries();
                }
            }
        });

        binding.swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(getContext(), SeriesDetailsActivity.class);
        intent.setData(uri);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public void onToggleFavorite(Uri uri, boolean isFavorite) {
        // Fallback
        onToggleFavorite(uri, isFavorite, null);
    }

    @Override
    public void onToggleFavorite(Uri uri, boolean isFavorite, Object item) {
        if (item instanceof PopularSerie) {
            seriesViewModel.toggleFavorite(uri, (PopularSerie) item, isFavorite);
        } else {
            seriesViewModel.toggleFavorite(uri, null, isFavorite);
        }

        if (isFavorite) {
            Toast.makeText(getContext(), getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.toast_add_to_favorites), Toast.LENGTH_SHORT).show();
        }
    }
}
