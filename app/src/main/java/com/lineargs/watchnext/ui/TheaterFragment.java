package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.TheaterAdapter;
import com.lineargs.watchnext.data.Movies;
import com.lineargs.watchnext.data.MoviesViewModel;
import com.lineargs.watchnext.data.TheaterViewModel;
import com.lineargs.watchnext.utils.Constants;
import com.lineargs.watchnext.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TheaterFragment extends BaseFragment implements TheaterAdapter.OnItemClickListener {

    @BindView(R.id.theater_recycler_view)
    RecyclerView recyclerView;
    private TheaterAdapter theaterAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Unbinder unbinder;

    public TheaterFragment() {
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_theater, container, false);
        setupViews(rootView);
        return rootView;
    }

    private void setupViews(final View view) {
        unbinder = ButterKnife.bind(this, view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        theaterAdapter = new TheaterAdapter(view.getContext(), this);
        recyclerView.setAdapter(theaterAdapter);
        if (NetworkUtils.isConnected(view.getContext())) {
            swipeRefreshLayout.setRefreshing(true);
        }
        final TheaterViewModel theaterViewModel = ViewModelProviders.of(this).get(TheaterViewModel.class);
        theaterViewModel.getTheaterMovies().observe(this, new Observer<List<Movies>>() {
            @Override
            public void onChanged(@Nullable List<Movies> theaterMovies) {
                theaterAdapter.setTheatreMovies(theaterMovies);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected(view.getContext())) {
                    swipeRefreshLayout.setRefreshing(true);
                }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
