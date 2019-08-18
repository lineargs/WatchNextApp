package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.TheaterAdapter;
import com.lineargs.watchnext.data.Movies;
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

    void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
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
