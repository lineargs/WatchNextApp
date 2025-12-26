package com.lineargs.watchnext.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.CastAdapter;
import com.lineargs.watchnext.adapters.CrewAdapter;
import com.lineargs.watchnext.adapters.MovieDetailAdapter;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.sync.syncmovies.MovieSyncUtils;
import com.lineargs.watchnext.utils.Constants;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.lineargs.watchnext.utils.Utils;
import com.lineargs.watchnext.utils.dbutils.DbUtils;
import com.squareup.picasso.Picasso;

import com.lineargs.watchnext.databinding.FragmentMovieDetailBinding;
import com.lineargs.watchnext.data.entity.Favorites;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;

/**
 * A fragment using ViewModel to show details for the movie.
 */
public class MovieDetailsFragment extends Fragment implements CastAdapter.OnClick, CrewAdapter.OnClick {

    private FragmentMovieDetailBinding binding;
    private Uri mUri;
    private CastAdapter mCastAdapter;
    private CrewAdapter mCrewAdapter;
    private MovieDetailAdapter mAdapter;
    private Handler handler;
    private String title = "";
    private long id;
    private boolean dualPane;
    private MovieDetailViewModel viewModel;
    private final Runnable castRunnable = new Runnable() {
        @Override
        public void run() {
            showEmptyCast();
        }
    };
    private final Runnable crewRunnable = new Runnable() {
        @Override
        public void run() {
            showEmptyCrew();
        }
    };

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

        // Initialize ViewModel
        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(MovieDetailViewModel.class);

        if (mUri != null) {
            if (savedState == null) {
                // Perform sync check in background
                viewModel.checkDataAndSync(context, mUri);
            }
            startCastLoading();
            startCrewLoading();
        }
        if (mUri != null) {
            viewModel.setMovieUri(mUri);
            
            // Observe Movie
            viewModel.getMovie().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<com.lineargs.watchnext.data.entity.Movie>() {
                @Override
                public void onChanged(com.lineargs.watchnext.data.entity.Movie movie) {
                    if (movie != null) {
                         imageLoad(movie);
                         mAdapter.swapMovie(movie);
                    }
                }
            });

            // Observe Cast
            viewModel.getCast().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.Credits>>() {
                @Override
                public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.Credits> credits) {
                    mCastAdapter.swapCast(credits);
                    if (credits != null && !credits.isEmpty()) {
                        handler.removeCallbacks(castRunnable);
                        showCastData();
                    } else if (credits != null) { // Empty list
                         handler.removeCallbacks(castRunnable); // Reset timer if empty updates repeatedly
                         handler.postDelayed(castRunnable, 3000); // Reduced to 3s
                    }
                }
            });
            
             // Observe Crew
            viewModel.getCrew().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.Credits>>() {
                @Override
                public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.Credits> credits) {
                    mCrewAdapter.swapCrew(credits);
                    if (credits != null && !credits.isEmpty()) {
                        handler.removeCallbacks(crewRunnable);
                        showCrewData();
                    } else if (credits != null) {
                        handler.removeCallbacks(crewRunnable);
                        handler.postDelayed(crewRunnable, 3000);
                    }
                }
            });

            // Observe Reviews
            viewModel.getReviews().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.Review>>() {
                @Override
                public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.Review> reviews) {
                     if (binding.movieFooter.reviews != null) {
                        if (reviews != null && !reviews.isEmpty()) {
                            binding.movieFooter.reviews.setEnabled(true);
                            binding.movieFooter.reviews.setTextColor(getResources().getColor(R.color.colorBlack));
                        } else {
                            binding.movieFooter.reviews.setEnabled(false);
                            binding.movieFooter.reviews.setTextColor(android.graphics.Color.GRAY);
                        }
                     }
                }
            });

            // Observe Videos
            viewModel.getVideos().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.Videos>>() {
                @Override
                public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.Videos> videos) {
                     if (binding.movieFooter.videos != null) {
                        if (videos != null && !videos.isEmpty()) {
                            binding.movieFooter.videos.setEnabled(true);
                            binding.movieFooter.videos.setTextColor(getResources().getColor(R.color.colorBlack));
                        } else {
                            binding.movieFooter.videos.setEnabled(false);
                            binding.movieFooter.videos.setTextColor(android.graphics.Color.GRAY);
                        }
                     }
                }
            });

            // Observe Favorite Status
            viewModel.getFavorite().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<Favorites>() {
                @Override
                public void onChanged(Favorites favorite) {
                    if (favorite != null) {
                        binding.starFab.setImageDrawable(Utils.starImage(getContext()));
                        binding.starFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorBlack)));
                    } else {
                        binding.starFab.setImageDrawable(Utils.starBorderImage(getContext()));
                        binding.starFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorBlack)));
                    }
                }
            });
        }
        
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
    /* If the activity is opened from anywhere except the Main activity we have an option
     * to add it in our favorites table. We do simple check if we have it already.
     * If we do we delete it(On user choice), if not we just copy the data.
     */
    public void starFabFavorite() {
        boolean isFavorite = false;
        if (viewModel.getFavorite().getValue() != null) {
            isFavorite = true;
        }

        if (isFavorite) {
            viewModel.toggleFavorite(mUri, true);
            Toast.makeText(getContext(), getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
            // FAB update handled by observer
        } else {
            viewModel.toggleFavorite(mUri, false);
            Toast.makeText(getContext(), getString(R.string.toast_add_to_favorites), Toast.LENGTH_SHORT).show();
            // FAB update handled by observer
        }
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

    private void imageLoad(com.lineargs.watchnext.data.entity.Movie movie) {
        title = movie.getTitle();
        id = movie.getTmdbId();
        // String imdb = cursor.getString(Query.IMDB_ID); // PopularMovie has imdbId
        String imdb = movie.getImdbId();
        
        // Button setup handled by Observers for Reviews and Videos
        ServiceUtils.setUpImdbButton(imdb, binding.movieButtons.imdb);
        ServiceUtils.setUpGoogleSearchButton(title, binding.movieButtons.google);
        ServiceUtils.setUpYouTubeButton(title, binding.movieButtons.youtube);
        ServiceUtils.setUpGooglePlayButton(title, binding.movieButtons.googlePlay);
        // The FAB update logic is now handled by the ViewModel observer
        if (binding.coverPoster != null) {
            ServiceUtils.loadPicasso(getContext(), movie.getPosterPath())
                    .fit()
                    .into(binding.coverPoster);
        }
        if (binding.coverBackdrop != null) {
            ServiceUtils.loadPicasso(getContext(), movie.getBackdropPath())
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
