package com.lineargs.watchnext.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.EpisodesQuery;
import com.lineargs.watchnext.utils.ServiceUtils;

import com.lineargs.watchnext.databinding.FragmentNotificationBinding;

public class NotificationFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 445;
    private FragmentNotificationBinding binding;
    private Uri mUri;

    public NotificationFragment() {
    }

    public void setmUri(Uri uri) {
        mUri = uri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
        binding.name.setText(cursor.getString(EpisodesQuery.NAME));
        binding.voteAverage.setText(cursor.getString(EpisodesQuery.VOTE_AVERAGE));
        binding.releaseDate.setText(cursor.getString(EpisodesQuery.RELEASE_DATE));
        binding.overview.setText(cursor.getString(EpisodesQuery.OVERVIEW));
        ServiceUtils.loadPicasso(binding.stillPath.getContext(), cursor.getString(EpisodesQuery.STILL_PATH))
                .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                .centerInside()
                .into(binding.stillPath);
    }
}
