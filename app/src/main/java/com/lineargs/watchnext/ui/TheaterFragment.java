package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
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
import com.lineargs.watchnext.data.Favourites;
import com.lineargs.watchnext.data.MoviesViewModel;
import com.lineargs.watchnext.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TheaterFragment extends BaseFragment implements TheaterAdapter.OnItemClickListener {

    private MoviesViewModel moviesViewModel;
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
        theaterAdapter = new TheaterAdapter(getContext(), this);
        recyclerView.setAdapter(theaterAdapter);
        if (NetworkUtils.isConnected(view.getContext())) {
            swipeRefreshLayout.setRefreshing(true);
        }
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        //TODO Change the observer for Theater Movies type, Movies with type == 3
        moviesViewModel.getFavourites().observe(this, new Observer<List<Favourites>>() {
            @Override
            public void onChanged(@Nullable List<Favourites> favourites) {
                theaterAdapter.setMovies(favourites);
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
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
        intent.setData(uri);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
