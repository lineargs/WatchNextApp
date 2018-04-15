package com.lineargs.watchnext.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by goranminov on 16/11/2017.
 * <p>
 * Abstract class for the Tabbed Movies Fragments.
 * Note we do not use it anymore
 */

public abstract class BaseMoviesFragment extends BaseFragment {

    @BindView(R.id.tabbed_movies_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabbed_movies, container, false);
        setupViews(rootView);
        return rootView;
    }

    public void setupViews(View view) {
        ButterKnife.bind(this, view);
        if (NetworkUtils.isConnected(view.getContext())) {
            startLoading();
        }
    }

    private void startLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    public void showData() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
