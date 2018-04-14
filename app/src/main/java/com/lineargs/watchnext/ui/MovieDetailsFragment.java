package com.lineargs.watchnext.ui;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.CastAdapter;
import com.lineargs.watchnext.adapters.CrewAdapter;
import com.lineargs.watchnext.adapters.MovieDetailAdapter;
import com.lineargs.watchnext.data.CreditsQuery;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.Query;
import com.lineargs.watchnext.sync.syncmovies.MovieSyncUtils;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.lineargs.watchnext.utils.dbutils.DbUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

/**
 * A fragment using Loaders to show details for the movie.
 */
public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, CastAdapter.OnClick, CrewAdapter.OnClick {


    static final String ID = "id", TITLE = "title", NAME = "name";
    private static final int MAIN_LOADER_ID = 223, CAST_LOADER_ID = 333, CREW_LOADER_ID = 445;
    private static final String URI = "uri";
    @Nullable
    @BindView(R.id.cover_poster)
    ImageView mPosterPath;
    @Nullable
    @BindView(R.id.cover_backdrop)
    ImageView mBackdropPath;
    @BindView(R.id.star_fab)
    FloatingActionButton starFab;
    @BindView(R.id.cast_recycler_view)
    RecyclerView mCastRecyclerView;
    @BindView(R.id.movie_details_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.cast_progress_bar)
    ProgressBar mCastProgressBar;
    @BindView(R.id.cast_linear_layout)
    LinearLayout mCastLayout;
    @BindView(R.id.empty_cast)
    AppCompatTextView mEmptyCast;
    @BindView(R.id.crew_recycler_view)
    RecyclerView mCrewRecyclerView;
    @BindView(R.id.crew_linear_layout)
    LinearLayout mCrewLayout;
    @BindView(R.id.empty_crew)
    AppCompatTextView mEmptyCrew;
    @BindView(R.id.crew_progress_bar)
    ProgressBar mCrewProgressBar;
    private Uri mUri;
    private CastAdapter mCastAdapter;
    private CrewAdapter mCrewAdapter;
    private MovieDetailAdapter mAdapter;
    private Handler handler;
    private String title = "";
    private Unbinder unbinder;
    private long id;
    private boolean mDualPane;

    public MovieDetailsFragment() {
    }

    public void setmUri(Uri uri) {
        mUri = uri;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mUri = Uri.parse(savedInstanceState.getString(URI));
        }
        setHasOptionsMenu(true);
        View mRootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        setupViews(getContext(), mRootView, savedInstanceState);
        return mRootView;
    }


    private void setupViews(Context context, View view, Bundle savedState) {
        unbinder = ButterKnife.bind(this, view);
        handler = new Handler();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new MovieDetailAdapter(context);
        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mCastRecyclerView.setLayoutManager(mLayoutManager);
        mCastRecyclerView.setHasFixedSize(true);
        mCastAdapter = new CastAdapter(context, this);
        mCastRecyclerView.setAdapter(mCastAdapter);

        LinearLayoutManager crewManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mCrewRecyclerView.setLayoutManager(crewManager);
        mCrewRecyclerView.setHasFixedSize(true);
        mCrewAdapter = new CrewAdapter(context, this);
        mCrewRecyclerView.setAdapter(mCrewAdapter);

        if (getActivity().getIntent().getIntExtra(MainActivity.FAB_ID, 0) == 1) {
            starFab.setVisibility(View.GONE);
        }

        if (mUri != null) {
            if (savedState == null && !checkForCredits(getContext(), mUri.getLastPathSegment())) {
                MovieSyncUtils.syncMovieDetail(context, mUri);
            }
            startCastLoading();
            startCrewLoading();
        }

        getLoaderManager().initLoader(MAIN_LOADER_ID, null, this);
        getLoaderManager().initLoader(CAST_LOADER_ID, null, this);
        getLoaderManager().initLoader(CREW_LOADER_ID, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View detailFrame = getActivity().findViewById(R.id.review_frame_layout);
        mDualPane = detailFrame != null && detailFrame.getVisibility() == View.VISIBLE;
        if (mDualPane && savedInstanceState == null) {
            ReviewFragment reviewFragment = new ReviewFragment();
            reviewFragment.setmUri(DataContract.Review.buildReviewUriWithId(Long.parseLong(mUri.getLastPathSegment())));
            getFragmentManager().beginTransaction()
                    .replace(R.id.review_frame_layout, reviewFragment)
                    .commit();
            VideosFragment videosFragment = new VideosFragment();
            videosFragment.setmUri(DataContract.Videos.buildVideoUriWithId(Long.parseLong(mUri.getLastPathSegment())));
            getFragmentManager().beginTransaction()
                    .replace(R.id.videos_frame_layout, videosFragment)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(URI, String.valueOf(mUri));
    }

    private void startCastLoading() {
        mCastProgressBar.setVisibility(View.VISIBLE);
        mCastLayout.setVisibility(View.GONE);
        mEmptyCast.setVisibility(View.GONE);
    }

    private void showCastData() {
        mCastProgressBar.setVisibility(View.GONE);
        mCastLayout.setVisibility(View.VISIBLE);
        mEmptyCast.setVisibility(View.GONE);
    }

    private void showEmptyCast() {
        mCastProgressBar.setVisibility(View.GONE);
        mCastLayout.setVisibility(View.GONE);
        mEmptyCast.setVisibility(View.VISIBLE);
    }

    private void startCrewLoading() {
        mCrewProgressBar.setVisibility(View.VISIBLE);
        mCrewLayout.setVisibility(View.GONE);
        mEmptyCrew.setVisibility(View.GONE);
    }

    private void showCrewData() {
        mCrewProgressBar.setVisibility(View.GONE);
        mCrewLayout.setVisibility(View.VISIBLE);
        mEmptyCrew.setVisibility(View.GONE);
    }

    private void showEmptyCrew() {
        mCrewProgressBar.setVisibility(View.GONE);
        mCrewLayout.setVisibility(View.GONE);
        mEmptyCrew.setVisibility(View.VISIBLE);
    }

    /* If the activity is opened from anywhere except the Main activity we have an option
     * to add it in our favorites table. We do simple check if we have it already.
     * If we do we delete it(On user choice), if not we just copy the data.
     */
    @OnClick(R.id.star_fab)
    public void starFabFavorite() {
        if (isFavorite(getContext(), Long.parseLong(mUri.getLastPathSegment()))) {
            DbUtils.removeFromFavorites(getContext(), mUri);
            Toast.makeText(getContext(), getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
            starFab.setImageDrawable(starBorderImage());
        } else {
            DbUtils.addMovieToFavorites(getContext(), mUri);
            Toast.makeText(getContext(), getString(R.string.toast_add_to_favorites), Toast.LENGTH_SHORT).show();
            starFab.setImageDrawable(starImage());
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MAIN_LOADER_ID:
                return new CursorLoader(getContext(),
                        mUri,
                        Query.PROJECTION,
                        null,
                        null,
                        null);
            case CAST_LOADER_ID:
                Uri uri = DataContract.Credits.buildCastUriWithId(Long.parseLong(mUri.getLastPathSegment()));
                return new CursorLoader(getContext(),
                        uri,
                        CreditsQuery.CREDITS_PROJECTION,
                        null,
                        null,
                        null);
            case CREW_LOADER_ID:
                Uri crewUri = DataContract.Credits.buildCrewUriWithId(Long.parseLong(mUri.getLastPathSegment()));
                return new CursorLoader(getContext(),
                        crewUri,
                        CreditsQuery.CREDITS_PROJECTION,
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
            case MAIN_LOADER_ID:
                if (data != null && data.getCount() != 0) {
                    data.moveToFirst();
                    imageLoad(data);
                    mAdapter.swapCursor(data);
                }
                break;
            case CAST_LOADER_ID:
                mCastAdapter.swapCursor(data);
                /* We use handler to manipulate with the user experience.
                 * The Loaders are much faster than the load from Network, so
                 * we delay the empty data show by calling the handler. Once
                 * we will get the data from the Network, the Loader is calling again
                 * onLoadFinished thanks to our Notify in the Adapter.
                 * We cancel the handler call just so we will not show empty data by mistake after
                 * populating the list.
                 */
                if (data != null && data.getCount() != 0) {
                    handler.removeCallbacksAndMessages(null);
                    data.moveToFirst();
                    showCastData();
                } else if (data != null && data.getCount() == 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showEmptyCast();
                        }
                    }, 7000);
                }
                break;
            case CREW_LOADER_ID:
                mCrewAdapter.swapCursor(data);
                if (data != null && data.getCount() != 0) {
                    handler.removeCallbacksAndMessages(null);
                    data.moveToFirst();
                    showCrewData();
                } else if (data != null && data.getCount() == 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showEmptyCrew();
                        }
                    }, 7000);
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
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
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
        intent.setData(DataContract.Review.buildReviewUriWithId(Long.parseLong(mUri.getLastPathSegment())));
        intent.putExtra(TITLE, title);
        startActivity(intent);
    }

    @Optional
    @OnClick(R.id.videos)
    public void loadVideos() {
        Intent intent = new Intent(getContext(), VideosActivity.class);
        Uri uri = DataContract.Videos.buildVideoUriWithId(Long.parseLong(mUri.getLastPathSegment()));
        intent.putExtra(TITLE, title);
        intent.setData(uri);
        startActivity(intent);
    }

    @OnClick(R.id.cast_header_layout)
    public void loadCast() {
        Intent intent = (new Intent(getContext(), CreditsCastActivity.class));
        Uri uri = DataContract.Credits.buildCastUriWithId(Long.parseLong(mUri.getLastPathSegment()));
        intent.setData(uri);
        startActivity(intent);
    }

    @OnClick(R.id.crew_header_layout)
    public void loadCrew() {
        Intent intent = (new Intent(getContext(), CreditsCrewActivity.class));
        Uri uri = DataContract.Credits.buildCrewUriWithId(Long.parseLong(mUri.getLastPathSegment()));
        intent.setData(uri);
        startActivity(intent);
    }

    private void imageLoad(Cursor cursor) {
        title = cursor.getString(Query.TITLE);
        id = cursor.getInt(Query.ID);
        if (isFavorite(getContext(), id)) {
            starFab.setImageDrawable(starImage());
        } else {
            starFab.setImageDrawable(starBorderImage());
        }
        if (mPosterPath != null) {
            Picasso.with(mPosterPath.getContext())
                    .load(cursor.getString(Query.POSTER_PATH))
                    .fit()
                    .into(mPosterPath);
        }
        if (mBackdropPath != null) {
            Picasso.with(mBackdropPath.getContext())
                    .load(cursor.getString(Query.BACKDROP_PATH))
                    .fit()
                    .into(mBackdropPath);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_share, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                ServiceUtils.shareMovie(getActivity(), String.valueOf(id));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private boolean isFavorite(Context context, long id) {
        Uri uri = DataContract.Favorites.buildFavoritesUriWithId(id);
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        if (cursor == null) {
            return false;
        }
        boolean favorite = cursor.getCount() > 0;
        cursor.close();
        return favorite;
    }

    /* Used so we can save on bandwith and network calls */
    private boolean checkForCredits(Context context, String id) {
        Uri uri = DataContract.Credits.buildCastUriWithId(Long.parseLong(id));
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        if (cursor == null) {
            return false;
        }
        boolean contains = cursor.getCount() > 0;
        cursor.close();
        return contains;
    }

    private VectorDrawableCompat starImage() {
        return VectorDrawableCompat.create(getContext().getResources(), R.drawable.icon_star_white, getContext().getTheme());
    }

    private VectorDrawableCompat starBorderImage() {
        return VectorDrawableCompat.create(getContext().getResources(), R.drawable.icon_star_border_white, getContext().getTheme());
    }

    @Override
    public void onPersonClick(String id, String name) {
        if (mDualPane) {
            Intent intent = (new Intent(getContext(), CreditsCastActivity.class));
            Uri uri = DataContract.Credits.buildCastUriWithId(Long.parseLong(mUri.getLastPathSegment()));
            intent.setData(uri);
            intent.putExtra(ID, id);
            intent.putExtra(NAME, name);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), PersonActivity.class);
            intent.setData(DataContract.Person.buildPersonUriWithId(Long.parseLong(id)));
            intent.putExtra(ID, id);
            intent.putExtra(NAME, name);
            startActivity(intent);
        }
    }

    @Override
    public void onCrewClick(String id, String name) {
        if (mDualPane) {
            Intent intent = (new Intent(getContext(), CreditsCrewActivity.class));
            Uri uri = DataContract.Credits.buildCrewUriWithId(Long.parseLong(mUri.getLastPathSegment()));
            intent.setData(uri);
            intent.putExtra(ID, id);
            intent.putExtra(NAME, name);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), PersonActivity.class);
            intent.setData(DataContract.Person.buildPersonUriWithId(Long.parseLong(id)));
            intent.putExtra(ID, id);
            intent.putExtra(NAME, name);
            startActivity(intent);
        }
    }
}
