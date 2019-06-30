package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.MoviesUpcomingAdapter;
import com.lineargs.watchnext.data.Movies;
import com.lineargs.watchnext.data.MoviesViewModel;
import com.lineargs.watchnext.utils.Constants;

import java.util.List;

/**
 * Created by goranminov on 03/11/2017.
 * <p>
 * A fragment for our Tabbed Movies View Pager
 */

public class MoviesUpcomingFragment extends MoviesListFragment implements MoviesUpcomingAdapter.OnItemClickListener {

    private MoviesUpcomingAdapter adapter;

    @Override
    public RecyclerView.Adapter getAdapter(View view) {
        adapter = new MoviesUpcomingAdapter(view.getContext(), this);
        return adapter;
    }

    @Override
    public void getObserver(MoviesViewModel viewModel) {
        viewModel.getUpcomingMovies().observe(this, new Observer<List<Movies>>() {
            @Override
            public void onChanged(@Nullable List<Movies> movies) {
                adapter.setUpcomingMovies(movies);
            }
        });
    }

    @Override
    public void onItemSelected(int tmdbId) {
        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
        intent.putExtra(Constants.ID, tmdbId);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent, bundle);
    }
}


