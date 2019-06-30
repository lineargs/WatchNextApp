package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SeriesOnTheAirAdapter;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Favourites;
import com.lineargs.watchnext.data.MoviesViewModel;
import com.lineargs.watchnext.data.Series;
import com.lineargs.watchnext.data.SeriesViewModel;

import java.util.List;

/**
 * Created by goranminov on 04/11/2017.
 * <p>
 * A fragment for our Tabbed Series View Pager
 */

public class SeriesOnTheAirFragment extends SeriesListFragment implements SeriesOnTheAirAdapter.OnItemClickListener {

    private SeriesOnTheAirAdapter adapter;

    @Override
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(getActivity(), SeriesDetailsActivity.class);
        intent.setData(uri);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        adapter = new SeriesOnTheAirAdapter(getActivity(), this);
        return adapter;
    }

    @Override
    public void getObserver(SeriesViewModel viewModel) {
        viewModel.getOnTheAirSeries().observe(this, new Observer<List<Series>>() {
            @Override
            public void onChanged(@Nullable List<Series> onTheAirSeries) {
                adapter.setOnTheAirSeries(onTheAirSeries);
            }
        });
    }
}



