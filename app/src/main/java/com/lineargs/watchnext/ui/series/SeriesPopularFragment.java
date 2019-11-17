package com.lineargs.watchnext.ui.series;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SeriesPopularAdapter;
import com.lineargs.watchnext.data.series.Series;
import com.lineargs.watchnext.data.series.SeriesViewModel;
import com.lineargs.watchnext.ui.base.SeriesListFragment;
import com.lineargs.watchnext.ui.series.SeriesDetailsActivity;
import com.lineargs.watchnext.utils.Constants;

import java.util.List;

/**
 * Created by goranminov on 03/11/2017.
 * <p>
 * A fragment for our Tabbed Series View Pager
 */

public class SeriesPopularFragment extends SeriesListFragment implements SeriesPopularAdapter.OnItemClickListener {

    private SeriesPopularAdapter adapter;

    @Override
    public void onItemSelected(int tmdbId) {
        Intent intent = new Intent(getContext(), SeriesDetailsActivity.class);
        intent.putExtra(Constants.ID, tmdbId);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public RecyclerView.Adapter getAdapter(View view) {
        adapter = new SeriesPopularAdapter(view.getContext(), this);
        return adapter;
    }

    @Override
    public void getObserver(SeriesViewModel viewModel) {
        viewModel.getPopularSeries().observe(this, new Observer<List<Series>>() {
            @Override
            public void onChanged(@Nullable List<Series> popularSeries) {
                adapter.setPopularSeries(popularSeries);
            }
        });
    }
}

