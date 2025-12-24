package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.SeriesPopularAdapter;
import com.lineargs.watchnext.data.DataContract;

/**
 * Created by goranminov on 03/11/2017.
 * <p>
 * A fragment for our Tabbed Series View Pager
 */

public class SeriesPopularFragment extends SeriesListFragment implements SeriesPopularAdapter.OnItemClickListener {

    private SeriesPopularAdapter adapter;

    @Override
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(getContext(), SeriesDetailsActivity.class);
        intent.setData(uri);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        adapter = new SeriesPopularAdapter(getActivity(), this);
        return adapter;
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull android.view.View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        com.lineargs.watchnext.ui.SeriesViewModel viewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.lineargs.watchnext.ui.SeriesViewModel.class);
        viewModel.getPopularSeries().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.PopularSerie>>() {
            @Override
            public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.PopularSerie> series) {
                if (series != null && !series.isEmpty()) {
                    adapter.swapSeries(series);
                    showData();
                }
            }
        });
    }
}

