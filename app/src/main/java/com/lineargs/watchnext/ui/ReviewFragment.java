package com.lineargs.watchnext.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.ReviewAdapter;
import com.lineargs.watchnext.data.Reviews;
import com.lineargs.watchnext.data.ReviewsViewModel;
import com.lineargs.watchnext.utils.ServiceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReviewFragment extends Fragment implements ReviewAdapter.OnClickListener {

    @BindView(R.id.review_recycler_view)
    RecyclerView mRecyclerView;
    private ReviewAdapter mAdapter;
    private Unbinder unbinder;
    private int tmdbId;

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public ReviewFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        setupViews(view, savedInstanceState);
        return view;
    }

    private void setupViews(View view, Bundle savedState) {
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ReviewAdapter(getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
        ReviewsViewModel reviewsViewModel = ViewModelProviders.of(this).get(ReviewsViewModel.class);
        reviewsViewModel.getReviews(tmdbId).observe(this, new Observer<List<Reviews>>() {
            @Override
            public void onChanged(@Nullable List<Reviews> reviews) {
                mAdapter.swapReviews(reviews);
            }
        });
    }

    @Override
    public void OnClick(String url) {
        ServiceUtils.openWeb(getActivity(), url);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
