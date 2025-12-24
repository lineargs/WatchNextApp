package com.lineargs.watchnext.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.ReviewAdapter;
import com.lineargs.watchnext.data.ReviewQuery;
import com.lineargs.watchnext.utils.ServiceUtils;

import com.lineargs.watchnext.databinding.FragmentReviewBinding;

public class ReviewFragment extends Fragment implements ReviewAdapter.OnClickListener {

    private FragmentReviewBinding binding;
    private ReviewAdapter mAdapter;
    private Uri mUri;
    private Handler handler;
    private ReviewViewModel viewModel;

    public ReviewFragment() {
    }

    public void setmUri(Uri uri) {
        this.mUri = uri;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        setupViews(savedInstanceState);
        return binding.getRoot();
    }

    private void setupViews(Bundle savedState) {
        handler = new Handler();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.reviewRecyclerView.setLayoutManager(layoutManager);
        binding.reviewRecyclerView.setHasFixedSize(true);
        mAdapter = new ReviewAdapter(getContext(), this);
        binding.reviewRecyclerView.setAdapter(mAdapter);

        if (savedState == null) {
            startLoading();
        }

        // Initialize ViewModel
        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(ReviewViewModel.class);
        if (mUri != null) {
            int movieId = Integer.parseInt(mUri.getLastPathSegment());
            viewModel.setMovieId(movieId);
            viewModel.getReviews().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.Review>>() {
                @Override
                public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.Review> reviews) {
                    mAdapter.swapReviews(reviews);
                    if (reviews != null && !reviews.isEmpty()) {
                        handler.removeCallbacksAndMessages(null);
                        showData();
                    } else if (reviews != null) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showEmpty();
                            }
                        }, 3000);
                    }
                }
            });
        }
    }

    private void startLoading() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setEnabled(false);
            binding.swipeRefreshLayout.setRefreshing(true);
        }
        binding.reviewRecyclerView.setVisibility(View.GONE);
        binding.emptyReview.setVisibility(View.GONE);
    }

    private void showData() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
        binding.reviewRecyclerView.setVisibility(View.VISIBLE);
        binding.emptyReview.setVisibility(View.GONE);
    }

    private void showEmpty() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
        binding.reviewRecyclerView.setVisibility(View.GONE);
        binding.emptyReview.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnClick(String url) {
        ServiceUtils.openWeb(getActivity(), url);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
