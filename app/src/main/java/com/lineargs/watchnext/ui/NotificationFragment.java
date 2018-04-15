package com.lineargs.watchnext.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.EpisodesQuery;
import com.lineargs.watchnext.utils.ServiceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NotificationFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 445;
    @BindView(R.id.name)
    AppCompatTextView name;
    @BindView(R.id.vote_average)
    AppCompatTextView voteAverage;
    @BindView(R.id.release_date)
    AppCompatTextView releaseDate;
    @BindView(R.id.overview)
    AppCompatTextView overview;
    @BindView(R.id.still_path)
    ImageView poster;
    private Uri mUri;
    private Unbinder unbinder;

    public NotificationFragment() {
    }

    public void setmUri(Uri uri) {
        mUri = uri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, view);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(getContext(),
                        mUri,
                        EpisodesQuery.EPISODE_PROJECTION,
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
                    loadData(data);
                }
                break;
            default:
                throw new RuntimeException("Loader not implemented: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void loadData(Cursor cursor) {
        name.setText(cursor.getString(EpisodesQuery.NAME));
        voteAverage.setText(cursor.getString(EpisodesQuery.VOTE_AVERAGE));
        releaseDate.setText(cursor.getString(EpisodesQuery.RELEASE_DATE));
        overview.setText(cursor.getString(EpisodesQuery.OVERVIEW));
        ServiceUtils.loadPicasso(poster.getContext(), cursor.getString(EpisodesQuery.STILL_PATH))
                .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                .centerInside()
                .into(poster);
    }
}
