package com.lineargs.watchnext.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.PersonQuery;
import com.lineargs.watchnext.sync.syncpeople.PersonSyncUtils;
import com.lineargs.watchnext.utils.Constants;
import com.lineargs.watchnext.utils.ServiceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PersonFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 455;

    @BindView(R.id.still_path)
    ImageView photo;
    @BindView(R.id.biography)
    AppCompatTextView biography;
    @BindView(R.id.place_of_birth)
    AppCompatTextView placeOfBirth;
    @BindView(R.id.homepage)
    AppCompatTextView homepage;
    @BindView(R.id.person_nested_view)
    NestedScrollView mNestedView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    private Uri mUri;
    private String id = "";
    private Unbinder unbinder;
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
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (savedInstanceState == null) {
            PersonSyncUtils.syncReviews(getContext(), id);
            startLoading();
        }
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return view;
    }

    private void startLoading() {
        mNestedView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void showData() {
        mNestedView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
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
        unbinder.unbind();
    }

    @OnClick(R.id.still_path)
    public void openFullscreen() {
        Intent fullscreen = new Intent(getActivity(), PictureActivity.class);
        fullscreen.putExtra(Constants.STILL_PATH, cursor.getString(PersonQuery.PROFILE_PATH));
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(),
                        photo,
                        ViewCompat.getTransitionName(photo));
        startActivity(fullscreen, optionsCompat.toBundle());
    }

    private void loadViews(Cursor cursor) {
        ServiceUtils.loadPicasso(photo.getContext(), cursor.getString(PersonQuery.PROFILE_PATH))
                .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                .centerCrop()
                .into(photo);
        if (TextUtils.isEmpty(cursor.getString(PersonQuery.BIOGRAPHY))) {
            biography.setText(getString(R.string.biography_not_available));
        } else {
            biography.setText(cursor.getString(PersonQuery.BIOGRAPHY));
        }
        /* We can use same logic from the biography for the place of birth or
         * homepage, just one small but big step for the mankind is that the Movie Db
         * API returns {String or null} for these. Still no bother implementing for now.
         * Just sayin'
         * UPDATE: Took only less than a minute to implement that. Do not be lazy please :)
         */
        if (TextUtils.isEmpty(cursor.getString(PersonQuery.PLACE_OF_BIRTH))) {
            placeOfBirth.setVisibility(View.GONE);
        } else {
            placeOfBirth.setText(cursor.getString(PersonQuery.PLACE_OF_BIRTH));
        }
        if (TextUtils.isEmpty(cursor.getString(PersonQuery.HOMEPAGE))) {
            homepage.setVisibility(View.GONE);
        } else {
            homepage.setText(cursor.getString(PersonQuery.HOMEPAGE));
        }
    }
}
