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
import android.widget.ProgressBar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.ReviewAdapter;
import com.lineargs.watchnext.data.ReviewQuery;
import com.lineargs.watchnext.utils.ServiceUtils;

import com.lineargs.watchnext.databinding.FragmentReviewBinding;

public class ReviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ReviewAdapter.OnClickListener {

    private static final int LOADER_ID = 112;
    private FragmentReviewBinding binding;
    private ReviewAdapter mAdapter;
    private Uri mUri;
    private Handler handler;

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
        binding.reviewRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new ReviewAdapter(getContext(), this);
        binding.reviewRecyclerView.setAdapter(mAdapter);


        if (savedState == null) {
            startLoading();
        }
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void startLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.reviewRecyclerView.setVisibility(View.GONE);
        binding.emptyReview.setVisibility(View.GONE);
    }

    private void showData() {
        binding.progressBar.setVisibility(View.GONE);
        binding.reviewRecyclerView.setVisibility(View.VISIBLE);
        binding.emptyReview.setVisibility(View.GONE);
    }

    private void showEmpty() {
        binding.progressBar.setVisibility(View.GONE);
        binding.reviewRecyclerView.setVisibility(View.GONE);
        binding.emptyReview.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(getContext(),
                        mUri,
                        ReviewQuery.REVIEW_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID:
                if (data != null && data.getCount() != 0) {
                    handler.removeCallbacksAndMessages(null);
                    data.moveToFirst();
                    mAdapter.swapCursor(data);
                    showData();
                } else if (data != null && data.getCount() == 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showEmpty();
                        }
                    }, 3000);

                }
                break;
            default:
                throw new RuntimeException("Loader not implemented: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
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
