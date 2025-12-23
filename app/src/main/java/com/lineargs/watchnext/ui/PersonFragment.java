package com.lineargs.watchnext.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.PersonQuery;
import com.lineargs.watchnext.sync.syncpeople.PersonSyncUtils;
import com.lineargs.watchnext.utils.Constants;
import com.lineargs.watchnext.utils.ServiceUtils;

import com.lineargs.watchnext.databinding.FragmentPersonBinding;

public class PersonFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 455;

    private FragmentPersonBinding binding;
    private Uri mUri;
    private String id = "";
    private Cursor cursor;

    public PersonFragment() {
    }

    public void setmUri(Uri uri) {
        mUri = uri;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonBinding.inflate(inflater, container, false);
        if (savedInstanceState == null) {
            PersonSyncUtils.syncReviews(getContext(), id);
            startLoading();
        }
        
        binding.stillPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFullscreen();
            }
        });
        
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return binding.getRoot();
    }

    private void startLoading() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setEnabled(false);
            binding.swipeRefreshLayout.setRefreshing(true);
        }
        binding.personNestedView.setVisibility(View.INVISIBLE);
    }

    private void showData() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
        binding.personNestedView.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(getContext(),
                        mUri,
                        PersonQuery.PERSON_PROJECTION,
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
                    data.moveToFirst();
                    loadViews(data);
                    cursor = data;
                    showData();
                }
                break;
            default:
                throw new RuntimeException("Loader not implemented: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void openFullscreen() {
        Intent fullscreen = new Intent(getActivity(), PictureActivity.class);
        fullscreen.putExtra(Constants.STILL_PATH, cursor.getString(PersonQuery.PROFILE_PATH));
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(),
                        binding.stillPath,
                        ViewCompat.getTransitionName(binding.stillPath));
        startActivity(fullscreen, optionsCompat.toBundle());
    }

    private void loadViews(Cursor cursor) {
        ServiceUtils.loadPicasso(binding.stillPath.getContext(), cursor.getString(PersonQuery.PROFILE_PATH))
                .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                .centerCrop()
                .into(binding.stillPath);
        if (TextUtils.isEmpty(cursor.getString(PersonQuery.BIOGRAPHY))) {
            binding.biography.setText(getString(R.string.biography_not_available));
        } else {
            binding.biography.setText(cursor.getString(PersonQuery.BIOGRAPHY));
        }
        /* We can use same logic from the biography for the place of birth or
         * homepage, just one small but big step for the mankind is that the Movie Db
         * API returns {String or null} for these. Still no bother implementing for now.
         * Just sayin'
         * UPDATE: Took only less than a minute to implement that. Do not be lazy please :)
         */
        if (TextUtils.isEmpty(cursor.getString(PersonQuery.PLACE_OF_BIRTH))) {
            binding.placeOfBirth.setVisibility(View.GONE);
        } else {
            binding.placeOfBirth.setText(cursor.getString(PersonQuery.PLACE_OF_BIRTH));
        }
        if (TextUtils.isEmpty(cursor.getString(PersonQuery.HOMEPAGE))) {
            binding.homepage.setVisibility(View.GONE);
        } else {
            binding.homepage.setText(cursor.getString(PersonQuery.HOMEPAGE));
        }
    }
}
