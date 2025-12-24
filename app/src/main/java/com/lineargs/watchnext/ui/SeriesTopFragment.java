package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SeriesTopAdapter;
import com.lineargs.watchnext.data.DataContract;

/**
 * Created by goranminov on 04/11/2017.
 * <p>
 * A fragment for our Tabbed Series View Pager
 */

public class SeriesTopFragment extends SeriesListFragment implements SeriesTopAdapter.OnItemClickListener {

    private SeriesTopAdapter adapter;

    @Override
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(getContext(), SeriesDetailsActivity.class);
        intent.setData(uri);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        adapter = new SeriesTopAdapter(getActivity(), this);
        return adapter;
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull android.view.View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        com.lineargs.watchnext.ui.SeriesViewModel viewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.lineargs.watchnext.ui.SeriesViewModel.class);
        viewModel.getTopRatedSeries().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.TopRatedSerie>>() {
            @Override
            public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.TopRatedSerie> series) {
                if (series != null && !series.isEmpty()) {
                    adapter.swapSeries(series);
                    showData();
                }
            }
        });
        viewModel.getFavoriteSeriesIds().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<Integer>>() {
            @Override
            public void onChanged(java.util.List<Integer> ids) {
                if (ids != null) {
                    java.util.Set<Long> favorites = new java.util.HashSet<>();
                    for (Integer id : ids) {
                        favorites.add(id.longValue());
                    }
                    adapter.setFavorites(favorites);
                }
            }
        });
    }
}


