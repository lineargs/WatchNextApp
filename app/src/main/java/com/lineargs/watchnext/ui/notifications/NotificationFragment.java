package com.lineargs.watchnext.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.lineargs.watchnext.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NotificationFragment extends Fragment {

    //TODO View Model implementation

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
    private Unbinder unbinder;

    public NotificationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /*
    private void loadData(Cursor cursor) {
        name.setText(cursor.getString(EpisodesQuery.NAME));
        voteAverage.setText(cursor.getString(EpisodesQuery.VOTE_AVERAGE));
        releaseDate.setText(cursor.getString(EpisodesQuery.RELEASE_DATE));
        overview.setText(cursor.getString(EpisodesQuery.OVERVIEW));
        ServiceUtils.loadPicasso(poster.getContext(), cursor.getString(EpisodesQuery.STILL_PATH))
                .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                .centerInside()
                .into(poster);
    } */
}
