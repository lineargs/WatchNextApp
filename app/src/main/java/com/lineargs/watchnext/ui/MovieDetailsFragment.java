package com.lineargs.watchnext.ui;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.lineargs.watchnext.utils.Constants;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.lineargs.watchnext.utils.Utils;
import com.lineargs.watchnext.utils.dbutils.DbUtils;
import com.squareup.picasso.Picasso;

import com.lineargs.watchnext.databinding.FragmentMovieDetailBinding;

/**
 * A fragment using Loaders to show details for the movie.
 */
public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, CastAdapter.OnClick, CrewAdapter.OnClick {


    private static final int MAIN_LOADER_ID = 223, CAST_LOADER_ID = 333, CREW_LOADER_ID = 445;

    private FragmentMovieDetailBinding binding;
    private Uri mUri;
    private CastAdapter mCastAdapter;
    private CrewAdapter mCrewAdapter;
    private MovieDetailAdapter mAdapter;
    private Handler handler;
    private String title = "";
    private long id;
    private boolean dualPane;

    public MovieDetailsFragment() {
    }

    public void setmUri(Uri uri) {
        mUri = uri;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mUri = Uri.parse(savedInstanceState.getString(Constants.URI));
        }
        setHasOptionsMenu(true);
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false);
        setupViews(getContext(), savedInstanceState);
        return binding.getRoot();
    }


    private void setupViews(Context context, Bundle savedState) {
        handler = new Handler();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        binding.movieDetailsRecyclerView.setLayoutManager(layoutManager);
        binding.movieDetailsRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new MovieDetailAdapter(context);
        binding.movieDetailsRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.castHeaderLayout.castRecyclerView.setLayoutManager(mLayoutManager);
        binding.castHeaderLayout.castRecyclerView.setHasFixedSize(true);
        mCastAdapter = new CastAdapter(context, this);
        binding.castHeaderLayout.castRecyclerView.setAdapter(mCastAdapter);

        LinearLayoutManager crewManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.crewHeaderLayout.crewRecyclerView.setLayoutManager(crewManager);
        binding.crewHeaderLayout.crewRecyclerView.setHasFixedSize(true);
        mCrewAdapter = new CrewAdapter(context, this);
        binding.crewHeaderLayout.crewRecyclerView.setAdapter(mCrewAdapter);

        if (getActivity().getIntent().getIntExtra(MainActivity.FAB_ID, 0) == 1) {
            binding.starFab.setVisibility(View.GONE);
        }

        if (mUri != null) {
            if (savedState == null && !DbUtils.checkForCredits(context, mUri.getLastPathSegment())) {
                MovieSyncUtils.syncFullMovieDetail(context, mUri);
            } else if (savedState == null && DbUtils.checkForExtras(context, mUri)) {
                MovieSyncUtils.syncUpdateMovieDetail(context, mUri);
            }
            startCastLoading();
            startCrewLoading();
        }

        getLoaderManager().initLoader(MAIN_LOADER_ID, null, this);
        getLoaderManager().initLoader(CAST_LOADER_ID, null, this);
        getLoaderManager().initLoader(CREW_LOADER_ID, null, this);
        
        binding.starFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starFabFavorite();
            }
        });
        
        binding.castHeaderLayout.castHeaderLayout.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCast();
            }
        });

        binding.crewHeaderLayout.crewHeaderLayout.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCrew();
            }
        });

        if (binding.movieFooter.reviews != null) {
            binding.movieFooter.reviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadReviews();
                }
            });
        }
        
        if (binding.movieFooter.videos != null) {
            binding.movieFooter.videos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadVideos();
                }
            });
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View detailFrame = getActivity().findViewById(R.id.review_frame_layout);
        dualPane = detailFrame != null && detailFrame.getVisibility() == View.VISIBLE;
        if (dualPane && savedInstanceState == null) {
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
        outState.putString(Constants.URI, String.valueOf(mUri));
    }

    private void startCastLoading() {
        binding.castHeaderLayout.castProgressBar.setVisibility(View.VISIBLE);
        binding.castHeaderLayout.castLinearLayout.setVisibility(View.GONE);
        binding.castHeaderLayout.emptyCast.setVisibility(View.GONE);
    }

    private void showCastData() {
        binding.castHeaderLayout.castProgressBar.setVisibility(View.GONE);
        binding.castHeaderLayout.castLinearLayout.setVisibility(View.VISIBLE);
        binding.castHeaderLayout.emptyCast.setVisibility(View.GONE);
    }

    private void showEmptyCast() {
        binding.castHeaderLayout.castProgressBar.setVisibility(View.GONE);
        binding.castHeaderLayout.castLinearLayout.setVisibility(View.GONE);
        binding.castHeaderLayout.emptyCast.setVisibility(View.VISIBLE);
    }

    private void startCrewLoading() {
        binding.crewHeaderLayout.crewProgressBar.setVisibility(View.VISIBLE);
        binding.crewHeaderLayout.crewLinearLayout.setVisibility(View.GONE);
        binding.crewHeaderLayout.emptyCrew.setVisibility(View.GONE);
    }

    private void showCrewData() {
        binding.crewHeaderLayout.crewProgressBar.setVisibility(View.GONE);
        binding.crewHeaderLayout.crewLinearLayout.setVisibility(View.VISIBLE);
        binding.crewHeaderLayout.emptyCrew.setVisibility(View.GONE);
    }

    private void showEmptyCrew() {
        binding.crewHeaderLayout.crewProgressBar.setVisibility(View.GONE);
        binding.crewHeaderLayout.crewLinearLayout.setVisibility(View.GONE);
        binding.crewHeaderLayout.emptyCrew.setVisibility(View.VISIBLE);
    }

    /* If the activity is opened from anywhere except the Main activity we have an option
     * to add it in our favorites table. We do simple check if we have it already.
     * If we do we delete it(On user choice), if not we just copy the data.
     */
    public void starFabFavorite() {
        if (DbUtils.isFavorite(getContext(), Long.parseLong(mUri.getLastPathSegment()))) {
            DbUtils.removeFromFavorites(getContext(), mUri);
            Toast.makeText(getContext(), getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
            binding.starFab.setImageDrawable(Utils.starBorderImage(getContext()));
        } else {
            DbUtils.addMovieToFavorites(getContext(), mUri);
            Toast.makeText(getContext(), getString(R.string.toast_add_to_favorites), Toast.LENGTH_SHORT).show();
            binding.starFab.setImageDrawable(Utils.starImage(getContext()));
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
        binding = null;
    }

    public void loadReviews() {
        Intent intent = new Intent(getContext(), ReviewActivity.class);
        intent.setData(DataContract.Review.buildReviewUriWithId(Long.parseLong(mUri.getLastPathSegment())));
        intent.putExtra(Constants.TITLE, title);
        startActivity(intent);
    }

    public void loadVideos() {
        Intent intent = new Intent(getContext(), VideosActivity.class);
        Uri uri = DataContract.Videos.buildVideoUriWithId(Long.parseLong(mUri.getLastPathSegment()));
        intent.putExtra(Constants.TITLE, title);
        intent.setData(uri);
        startActivity(intent);
    }

    public void loadCast() {
        Intent intent = (new Intent(getContext(), CreditsCastActivity.class));
        Uri uri = DataContract.Credits.buildCastUriWithId(Long.parseLong(mUri.getLastPathSegment()));
        intent.setData(uri);
        startActivity(intent);
    }

    public void loadCrew() {
        Intent intent = (new Intent(getContext(), CreditsCrewActivity.class));
        Uri uri = DataContract.Credits.buildCrewUriWithId(Long.parseLong(mUri.getLastPathSegment()));
        intent.setData(uri);
        startActivity(intent);
    }

    private void imageLoad(Cursor cursor) {
        title = cursor.getString(Query.TITLE);
        id = cursor.getInt(Query.ID);
        String imdb = cursor.getString(Query.IMDB_ID);
        if (binding.movieFooter.reviews != null) {
            ServiceUtils.setUpCommentsButton(getContext(), mUri.getLastPathSegment(), binding.movieFooter.reviews);
        }
        if (binding.movieFooter.videos != null) {
            ServiceUtils.setUpVideosButton(getContext(), mUri.getLastPathSegment(), binding.movieFooter.videos);
        }
        ServiceUtils.setUpImdbButton(imdb, binding.movieButtons.imdb);
        ServiceUtils.setUpGoogleSearchButton(title, binding.movieButtons.google);
        ServiceUtils.setUpYouTubeButton(title, binding.movieButtons.youtube);
        ServiceUtils.setUpGooglePlayButton(title, binding.movieButtons.googlePlay);
        if (DbUtils.isFavorite(getContext(), id)) {
            binding.starFab.setImageDrawable(Utils.starImage(getContext()));
        } else {
            binding.starFab.setImageDrawable(Utils.starBorderImage(getContext()));
        }
        if (binding.coverPoster != null) {
            ServiceUtils.loadPicasso(getContext(), cursor.getString(Query.POSTER_PATH))
                    .fit()
                    .into(binding.coverPoster);
        }
        if (binding.coverBackdrop != null) {
            ServiceUtils.loadPicasso(getContext(), cursor.getString(Query.BACKDROP_PATH))
                    .fit()
                    .into(binding.coverBackdrop);
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



    @Override
    public void onPersonClick(String id, String name) {
        if (dualPane) {
            Intent intent = (new Intent(getContext(), CreditsCastActivity.class));
            Uri uri = DataContract.Credits.buildCastUriWithId(Long.parseLong(mUri.getLastPathSegment()));
            intent.setData(uri);
            intent.putExtra(Constants.ID, id);
            intent.putExtra(Constants.NAME, name);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), PersonActivity.class);
            intent.setData(DataContract.Person.buildPersonUriWithId(Long.parseLong(id)));
            intent.putExtra(Constants.ID, id);
            intent.putExtra(Constants.NAME, name);
            startActivity(intent);
        }
    }

    @Override
    public void onCrewClick(String id, String name) {
        if (dualPane) {
            Intent intent = (new Intent(getContext(), CreditsCrewActivity.class));
            Uri uri = DataContract.Credits.buildCrewUriWithId(Long.parseLong(mUri.getLastPathSegment()));
            intent.setData(uri);
            intent.putExtra(Constants.ID, id);
            intent.putExtra(Constants.NAME, name);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), PersonActivity.class);
            intent.setData(DataContract.Person.buildPersonUriWithId(Long.parseLong(id)));
            intent.putExtra(Constants.ID, id);
            intent.putExtra(Constants.NAME, name);
            startActivity(intent);
        }
    }
}
