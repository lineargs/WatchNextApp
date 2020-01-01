package com.lineargs.watchnext.ui.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.CastAdapter;
import com.lineargs.watchnext.adapters.CrewAdapter;
import com.lineargs.watchnext.adapters.MovieDetailAdapter;
import com.lineargs.watchnext.data.credits.Credits;
import com.lineargs.watchnext.data.movies.MovieDetailsViewModel;
import com.lineargs.watchnext.data.movies.Movies;
import com.lineargs.watchnext.ui.person.PersonActivity;
import com.lineargs.watchnext.ui.reviews.ReviewActivity;
import com.lineargs.watchnext.ui.reviews.ReviewFragment;
import com.lineargs.watchnext.ui.videos.VideosActivity;
import com.lineargs.watchnext.ui.videos.VideosFragment;
import com.lineargs.watchnext.ui.credits.CreditsCastActivity;
import com.lineargs.watchnext.ui.credits.CreditsCrewActivity;
import com.lineargs.watchnext.ui.main.MainActivity;
import com.lineargs.watchnext.utils.Constants;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.lineargs.watchnext.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

/**
 * A fragment using Loaders to show details for the movieDetailAdapter.
 */
public class MovieDetailsFragment extends Fragment implements CastAdapter.OnClick, CrewAdapter.OnClick {

    private int tmdbId;
    private String title = "";
    private Unbinder unbinder;
    private boolean dualPane;
    private CrewAdapter crewAdapter;
    private CastAdapter castAdapter;

    @Nullable
    @BindView(R.id.cover_poster)
    ImageView posterPath;
    @Nullable
    @BindView(R.id.cover_backdrop)
    ImageView backdropPath;
    @BindView(R.id.star_fab)
    FloatingActionButton starFab;
    @BindView(R.id.cast_recycler_view)
    RecyclerView castRecyclerView;
    @BindView(R.id.movie_details_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.cast_linear_layout)
    LinearLayout castLayout;
    @BindView(R.id.crew_recycler_view)
    RecyclerView crewRecyclerView;
    @BindView(R.id.crew_linear_layout)
    LinearLayout crewLayout;
    @BindView(R.id.empty_crew)
    AppCompatTextView emptyCrew;
    @BindView(R.id.empty_cast)
    AppCompatTextView emptyCast;
    @BindView(R.id.imdb)
    Button imdbButton;
    @BindView(R.id.google)
    Button googleButton;
    @BindView(R.id.youtube)
    Button youTubeButton;
    @BindView(R.id.google_play)
    Button googlePlayButton;
    @Nullable
    @BindView(R.id.reviews)
    Button reviewsButton;
    @Nullable
    @BindView(R.id.videos)
    Button videosButton;

    public MovieDetailsFragment() {
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            tmdbId = savedInstanceState.getInt(Constants.ID);
        }
        setHasOptionsMenu(true);
        View mRootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        setupViews(getContext(), mRootView, savedInstanceState);
        return mRootView;
    }


    private void setupViews(Context context, View view, Bundle savedState) {
        unbinder = ButterKnife.bind(this, view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        final MovieDetailAdapter movieDetailAdapter = new MovieDetailAdapter(context);
        recyclerView.setAdapter(movieDetailAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        castRecyclerView.setLayoutManager(mLayoutManager);
        castRecyclerView.setHasFixedSize(true);
        castAdapter = new CastAdapter(context, this);
        castRecyclerView.setAdapter(castAdapter);

        LinearLayoutManager crewManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        crewRecyclerView.setLayoutManager(crewManager);
        crewRecyclerView.setHasFixedSize(true);
        crewAdapter = new CrewAdapter(context, this);
        crewRecyclerView.setAdapter(crewAdapter);

        if (getActivity().getIntent().getIntExtra(MainActivity.FAB_ID, 0) == 1) {
            starFab.setVisibility(View.GONE);
        }

        MovieDetailsViewModel movieViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
        if (savedState == null) {
            movieViewModel.getMovieDetails(String.valueOf(tmdbId));
        }
        movieViewModel.getMovie(tmdbId).observe(this, new Observer<Movies>() {
            @Override
            public void onChanged(Movies movie) {
                movieDetailAdapter.swapMovie(movie);
                imageLoad(movie, getContext());
            }
        });
        movieViewModel.getCast(tmdbId).observe(this, new Observer<List<Credits>>() {
            @Override
            public void onChanged(@Nullable List<Credits> credits) {
                loadCastViews(credits);
            }
        });
        movieViewModel.getCrew(tmdbId).observe(this, new Observer<List<Credits>>() {
            @Override
            public void onChanged(@Nullable List<Credits> credits) {
                loadCrewViews(credits);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View detailFrame = getActivity().findViewById(R.id.review_frame_layout);
        dualPane = detailFrame != null && detailFrame.getVisibility() == View.VISIBLE;
        if (dualPane && savedInstanceState == null) {
            ReviewFragment reviewFragment = new ReviewFragment();
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.review_frame_layout, reviewFragment)
                        .commit();
            }
            VideosFragment videosFragment = new VideosFragment();
            videosFragment.setTmdbId(tmdbId);
            getFragmentManager().beginTransaction()
                    .replace(R.id.videos_frame_layout, videosFragment)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(Constants.ID, tmdbId);
    }

    /* If the activity is opened from anywhere except the Main activity we have an option
     * to add it in our favorites table. We do simple check if we have it already.
     * If we do we delete it(On user choice), if not we just copy the data.
     */
    @OnClick(R.id.star_fab)
    public void starFabFavorite() {
//        if (DbUtils.isFavorite(getContext(), Long.parseLong(uri.getLastPathSegment()))) {
//            DbUtils.removeFromFavorites(getContext(), uri);
//            Toast.makeText(getContext(), getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
//        starFab.setImageDrawable(Utils.starBorderImage(getContext()));
//        } else {
//            DbUtils.addMovieToFavorites(getContext(), uri);
//            Toast.makeText(getContext(), getString(R.string.toast_add_to_favorites), Toast.LENGTH_SHORT).show();
//            starFab.setImageDrawable(Utils.starImage(getContext()));
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Optional
    @OnClick(R.id.reviews)
    public void loadReviews() {
        Intent intent = new Intent(getContext(), ReviewActivity.class);
        intent.putExtra(Constants.TITLE, title);
        intent.putExtra(Constants.ID, tmdbId);
        startActivity(intent);
    }

    @Optional
    @OnClick(R.id.videos)
    public void loadVideos() {
        Intent intent = new Intent(getContext(), VideosActivity.class);
        intent.putExtra(Constants.ID, tmdbId);
        intent.putExtra(Constants.TITLE, title);
        startActivity(intent);
    }

    @OnClick(R.id.cast_header_layout)
    public void loadCast() {
        Intent intent = (new Intent(getContext(), CreditsCastActivity.class));
        intent.putExtra(Constants.ID, tmdbId);
        startActivity(intent);
    }

    private void showCastData() {
        emptyCast.setVisibility(View.GONE);
        castLayout.setVisibility(View.VISIBLE);
    }

    private void showCastEmpty() {
        emptyCast.setVisibility(View.VISIBLE);
        castLayout.setVisibility(View.GONE);
    }

    private void loadCastViews(List<Credits> credits) {
        if (credits != null) {
            if (credits.size() != 0) {
                showCastData();
                castAdapter.swapCast(credits);
            } else {
                showCastEmpty();
            }
        }
    }

    @OnClick(R.id.crew_header_layout)
    public void loadCrew() {
        Intent intent = (new Intent(getContext(), CreditsCrewActivity.class));
        intent.putExtra(Constants.ID, tmdbId);
        startActivity(intent);
    }

    private void showCrewData() {
        emptyCrew.setVisibility(View.GONE);
        crewLayout.setVisibility(View.VISIBLE);
    }

    private void showCrewEmpty() {
        emptyCrew.setVisibility(View.VISIBLE);
        crewLayout.setVisibility(View.GONE);
    }

    private void loadCrewViews(List<Credits> credits) {
        if (credits != null) {
            if (credits.size() != 0) {
                showCrewData();
                crewAdapter.swapCrew(credits);
            } else {
                showCrewEmpty();
            }
        }
    }

    private void imageLoad(Movies movies, Context context) {
        title = movies.getTitle();
        String imdb = movies.getImdbId();
        ServiceUtils.setUpImdbButton(imdb, imdbButton);
        ServiceUtils.setUpGoogleSearchButton(title, googleButton);
        ServiceUtils.setUpYouTubeButton(title, youTubeButton);
        ServiceUtils.setUpGooglePlayButton(title, googlePlayButton);
//        if (DbUtils.isFavorite(getContext(), id)) {
//            starFab.setImageDrawable(Utils.starImage(getContext()));
//        } else {
        starFab.setImageDrawable(Utils.starBorderImage(context));
//        }
        if (posterPath != null) {
            Picasso.with(posterPath.getContext())
                    .load(movies.getPosterPath())
                    .fit()
                    .into(posterPath);
        }
        if (backdropPath != null) {
            Picasso.with(backdropPath.getContext())
                    .load(movies.getBackdropPath())
                    .fit()
                    .into(backdropPath);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_share, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            ServiceUtils.shareMovie(getActivity(), String.valueOf(tmdbId));
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onPersonClick(String id, String name) {
        if (dualPane) {
            Intent intent = (new Intent(getContext(), CreditsCastActivity.class));
            intent.putExtra(Constants.ID, id);
            intent.putExtra(Constants.NAME, name);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), PersonActivity.class);
            intent.putExtra(Constants.ID, id);
            intent.putExtra(Constants.NAME, name);
            startActivity(intent);
        }
    }

    @Override
    public void onCrewClick(String id, String name) {
        if (dualPane) {
            Intent intent = (new Intent(getContext(), CreditsCrewActivity.class));
            intent.putExtra(Constants.ID, id);
            intent.putExtra(Constants.NAME, name);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), PersonActivity.class);
            intent.putExtra(Constants.ID, id);
            intent.putExtra(Constants.NAME, name);
            startActivity(intent);
        }
    }
}