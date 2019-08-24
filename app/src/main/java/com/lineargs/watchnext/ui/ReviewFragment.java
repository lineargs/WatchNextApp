package com.lineargs.watchnext.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.ReviewAdapter;
import com.lineargs.watchnext.data.Reviews;
import com.lineargs.watchnext.data.ReviewsViewModel;
import com.lineargs.watchnext.utils.Constants;
import com.lineargs.watchnext.utils.ServiceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReviewFragment extends Fragment implements ReviewAdapter.OnClickListener {

    @BindView(R.id.review_recycler_view)
    RecyclerView mRecyclerView;
    @Nullable
    @BindView(R.id.review_nested_view)
    NestedScrollView mNestedView;
    @BindView(R.id.empty_review)
    AppCompatTextView mEmptyReview;
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
        if (savedState != null) {
            tmdbId = savedState.getInt(Constants.ID);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ReviewAdapter(view.getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
        ReviewsViewModel reviewsViewModel = ViewModelProviders.of(this).get(ReviewsViewModel.class);
        reviewsViewModel.getReviews(tmdbId).observe(this, new Observer<List<Reviews>>() {
            @Override
            public void onChanged(List<Reviews> reviews) {
                loadViews(reviews);
            }
        });
    }

    private void showEmpty() {
        if (mNestedView != null) {
            mEmptyReview.setVisibility(View.VISIBLE);
            mNestedView.setVisibility(View.INVISIBLE);
        } else {
            mEmptyReview.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void showData() {
        if (mNestedView != null) {
            mEmptyReview.setVisibility(View.INVISIBLE);
            mNestedView.setVisibility(View.VISIBLE);
        } else {
            mEmptyReview.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void loadViews(List<Reviews> reviews) {
        if (reviews != null) {
            if (reviews.size() != 0) {
                showData();
                mAdapter.swapReviews(reviews);
            } else {
                showEmpty();
            }
        }
    }

    @Override
    public void OnClick(String url) {
        ServiceUtils.openWeb(getActivity(), url);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(Constants.ID, tmdbId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
