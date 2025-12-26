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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.MoviesPopularAdapter;
import com.lineargs.watchnext.data.entity.PopularMovie;
import com.lineargs.watchnext.databinding.FragmentTrendingBinding;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrendingMoviesFragment extends BaseFragment implements MoviesPopularAdapter.OnItemClickListener {

    private FragmentTrendingBinding binding;
    private MoviesPopularAdapter adapter;
    private TrendingViewModel viewModel;
    private MoviesViewModel moviesViewModel;

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
        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);

        viewModel.getTrendingMovies().observe(getViewLifecycleOwner(), new Observer<List<PopularMovie>>() {
            @Override
            public void onChanged(List<PopularMovie> movies) {
                if (movies != null) {
                    adapter.swapMovies(movies);
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
        moviesViewModel.getFavoriteMovieIds().observe(getViewLifecycleOwner(), new Observer<List<Integer>>() {
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
        adapter = new MoviesPopularAdapter(requireContext(), this);
        binding.trendingRecyclerView.setAdapter(adapter);

        binding.trendingRecyclerView.addOnScrollListener(new androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull androidx.recyclerview.widget.RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.loadNextTrendingMovies();
                }
            }
        });

        binding.swipeRefreshLayout.setEnabled(false); // Disable pull-to-refresh for now as data is static per session or requires reload method
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
        intent.setData(uri);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public void onToggleFavorite(Uri uri, boolean isFavorite) {
        // Fallback or deprecated
        onToggleFavorite(uri, isFavorite, null);
    }

    @Override
    public void onToggleFavorite(Uri uri, boolean isFavorite, Object item) {
        if (item instanceof PopularMovie) {
            moviesViewModel.toggleFavorite(uri, (PopularMovie) item, isFavorite);
        } else {
            moviesViewModel.toggleFavorite(uri, null, isFavorite);
        }

        if (isFavorite) {
            Toast.makeText(getContext(), getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.toast_add_to_favorites), Toast.LENGTH_SHORT).show();
        }
    }
}
