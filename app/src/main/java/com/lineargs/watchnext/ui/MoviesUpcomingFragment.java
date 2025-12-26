package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.MoviesUpcomingAdapter;

/**
 * Created by goranminov on 03/11/2017.
 * <p>
 * A fragment for our Tabbed Movies View Pager
 */

public class MoviesUpcomingFragment extends MoviesListFragment implements MoviesUpcomingAdapter.OnItemClickListener {

    private MoviesUpcomingAdapter mAdapter;

    @Override
    public RecyclerView.Adapter getAdapter() {
        mAdapter = new MoviesUpcomingAdapter(getActivity(), this);
        return mAdapter;
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull android.view.View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        com.lineargs.watchnext.ui.MoviesViewModel viewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.lineargs.watchnext.ui.MoviesViewModel.class);
        viewModel.getUpcomingMovies().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.UpcomingMovie>>() {
            @Override
            public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.UpcomingMovie> movies) {
                if (movies != null && !movies.isEmpty()) {
                    mAdapter.swapMovies(movies);
                    showData();
                }
            }
        });
        viewModel.getFavoriteMovieIds().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<Integer>>() {
            @Override
            public void onChanged(java.util.List<Integer> ids) {
                if (ids != null) {
                    java.util.Set<Long> favorites = new java.util.HashSet<>();
                    for (Integer id : ids) {
                        favorites.add(id.longValue());
                    }
                    mAdapter.setFavorites(favorites);
                }
            }
        });
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
        com.lineargs.watchnext.ui.MoviesViewModel viewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.lineargs.watchnext.ui.MoviesViewModel.class);
        viewModel.toggleFavorite(uri, isFavorite);
        if (isFavorite) {
            android.widget.Toast.makeText(getContext(), getString(R.string.toast_remove_from_favorites), android.widget.Toast.LENGTH_SHORT).show();
        } else {
            android.widget.Toast.makeText(getContext(), getString(R.string.toast_add_to_favorites), android.widget.Toast.LENGTH_SHORT).show();
        }
    }
}


