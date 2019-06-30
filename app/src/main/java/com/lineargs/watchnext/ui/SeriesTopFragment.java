package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SeriesTopAdapter;
import com.lineargs.watchnext.data.Series;
import com.lineargs.watchnext.data.SeriesViewModel;

import java.util.List;

/**
 * Created by goranminov on 04/11/2017.
 * <p>
 * A fragment for our Tabbed Series View Pager
 */

public class SeriesTopFragment extends SeriesListFragment implements SeriesTopAdapter.OnItemClickListener {

    private SeriesTopAdapter adapter;

    @Override
    public void onItemSelected(int tmdbId) {
        Intent intent = new Intent(getContext(), SeriesDetailsActivity.class);
//        intent.setData(uri);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public RecyclerView.Adapter getAdapter(View view) {
        adapter = new SeriesTopAdapter(view.getContext(), this);
        return adapter;
    }

    @Override
    public void getObserver(final SeriesViewModel viewModel) {
        viewModel.getTopRatedSeries().observe(this, new Observer<List<Series>>() {
            @Override
            public void onChanged(@Nullable List<Series> topRatedSeries) {
                adapter.setTopRatedSeries(topRatedSeries);
            }
        });
    }
}


