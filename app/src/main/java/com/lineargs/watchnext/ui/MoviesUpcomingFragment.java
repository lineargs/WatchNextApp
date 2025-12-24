package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.MoviesUpcomingAdapter;
import com.lineargs.watchnext.data.DataContract;

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
    }

    @Override
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
        intent.setData(uri);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent, bundle);
    }
}


