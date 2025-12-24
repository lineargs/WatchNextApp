package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.TheaterAdapter;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Query;
import com.lineargs.watchnext.utils.NetworkUtils;
import com.lineargs.watchnext.utils.WorkManagerUtils;

import com.lineargs.watchnext.databinding.FragmentTheaterBinding;

public class TheaterFragment extends BaseFragment implements TheaterAdapter.OnItemClickListener {

    private FragmentTheaterBinding binding;
    private TheaterAdapter theaterAdapter;

    public TheaterFragment() {
    }

    public void setSwipeRefreshLayout (SwipeRefreshLayout swipeRefreshLayout) {
        // this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTheaterBinding.inflate(inflater, container, false);
        setupViews();
        return binding.getRoot();
    }

    private void setupViews() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        binding.theaterRecyclerView.setLayoutManager(layoutManager);
        binding.theaterRecyclerView.setHasFixedSize(true);
        theaterAdapter = new TheaterAdapter(getContext(), this);
        binding.theaterRecyclerView.setAdapter(theaterAdapter);
        if (NetworkUtils.isConnected(getContext())) {
            binding.swipeRefreshLayout.setRefreshing(true);
        }
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected(getContext())) {
                    binding.swipeRefreshLayout.setRefreshing(true);
                    WorkManagerUtils.syncImmediately(getContext());
                }
            }
        });
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        com.lineargs.watchnext.ui.MoviesViewModel viewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.lineargs.watchnext.ui.MoviesViewModel.class);
        viewModel.getTheaterMovies().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.TheaterMovie>>() {
            @Override
            public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.TheaterMovie> movies) {
                if (movies != null && !movies.isEmpty()) {
                    theaterAdapter.swapMovies(movies);
                    binding.swipeRefreshLayout.setRefreshing(false);
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
        binding = null;
    }
}
