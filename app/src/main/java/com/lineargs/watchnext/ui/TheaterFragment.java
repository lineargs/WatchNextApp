package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.TheaterAdapter;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Movies;
import com.lineargs.watchnext.data.MoviesViewModel;
import com.lineargs.watchnext.data.Query;
import com.lineargs.watchnext.utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TheaterFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        TheaterAdapter.OnItemClickListener {

    private MoviesViewModel moviesViewModel;
    private static final int LOADER_ID = 333;
    @BindView(R.id.theater_recycler_view)
    RecyclerView recyclerView;
    private TheaterAdapter theaterAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Unbinder unbinder;

    public TheaterFragment() {
    }

    public void setSwipeRefreshLayout (SwipeRefreshLayout swipeRefreshLayout) {
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
        moviesViewModel.getMovies().observe(this, new Observer<List<Movies>>() {
            @Override
            public void onChanged(@Nullable List<Movies> movies) {
                theaterAdapter.setMovies(movies);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected(view.getContext())) {
                    swipeRefreshLayout.setRefreshing(true);
                    getLoaderManager().restartLoader(LOADER_ID, null, TheaterFragment.this);
                }
            }
        });
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(getContext(),
                        DataContract.TheaterMovieEntry.CONTENT_URI,
                        Query.PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID:
//                theaterAdapter.swapCursor(data);
                if (data != null && data.getCount() != 0) {
                    data.moveToFirst();
                    swipeRefreshLayout.setRefreshing(false);
                }
                break;
            default:
                throw new RuntimeException("Loader not implemented: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
//        theaterAdapter.swapCursor(null);
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
