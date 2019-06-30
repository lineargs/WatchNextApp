package com.lineargs.watchnext.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.SeriesViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class SeriesListFragment extends BaseFragment {

    @BindView(R.id.tabbed_series_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_list_series, container, false);
        setupViews(rootView);
        return rootView;
    }

    private void setupViews(View view) {
        unbinder = ButterKnife.bind(this, view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(getAdapter(view));
        SeriesViewModel seriesViewModel = ViewModelProviders.of(this).get(SeriesViewModel.class);
        getObserver(seriesViewModel);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public abstract RecyclerView.Adapter getAdapter(View view);

    public abstract void getObserver(SeriesViewModel viewModel);
}
