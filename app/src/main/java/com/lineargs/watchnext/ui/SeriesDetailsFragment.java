package com.lineargs.watchnext.ui;

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
import com.lineargs.watchnext.adapters.TVDetailAdapter;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.sync.syncseries.SerieDetailUtils;
import com.lineargs.watchnext.utils.Constants;
import com.lineargs.watchnext.utils.ServiceUtils;
import com.lineargs.watchnext.utils.Utils;
import com.lineargs.watchnext.utils.dbutils.DbUtils;
import com.squareup.picasso.Picasso;

import com.lineargs.watchnext.databinding.FragmentSeriesDetailBinding;

public class SeriesDetailsFragment extends Fragment implements CastAdapter.OnClick {

    private FragmentSeriesDetailBinding binding;
    private Uri mUri;
    private CastAdapter mCastAdapter;
    private TVDetailAdapter mAdapter;
    private Handler handler;
    private String title = "";
    private long id;
    private boolean mDualPane;
    private SeriesDetailViewModel viewModel;

    public SeriesDetailsFragment() {
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
        binding = FragmentSeriesDetailBinding.inflate(inflater, container, false);
        setupViews(savedInstanceState);
        return binding.getRoot();
    }

    private void setupViews(Bundle savedState) {
        handler = new Handler();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        binding.movieDetailsRecyclerView.setLayoutManager(layoutManager);
        binding.movieDetailsRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new TVDetailAdapter(getContext());
        binding.movieDetailsRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.castHeaderLayout.castRecyclerView.setLayoutManager(mLayoutManager);
        binding.castHeaderLayout.castRecyclerView.setHasFixedSize(true);
        mCastAdapter = new CastAdapter(getContext(), this);
        binding.castHeaderLayout.castRecyclerView.setAdapter(mCastAdapter);

        if (getActivity().getIntent().getIntExtra(MainActivity.FAB_ID, 0) == 1) {
            binding.starFab.setVisibility(View.GONE);
        }

        if (mUri != null) {
            if (savedState == null && !DbUtils.checkForCredits(getContext(), mUri.getLastPathSegment())) {
                SerieDetailUtils.syncSeasons(getContext(), mUri);
            } else if (savedState == null) {
                SerieDetailUtils.updateDetails(getContext(), mUri);
            }
            startCastLoading();
        }

        // Initialize ViewModel
        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(SeriesDetailViewModel.class);
        if (mUri != null) {
            int seriesId = Integer.parseInt(mUri.getLastPathSegment());
            viewModel.setSeriesUri(mUri);

            // Observe Serie
            viewModel.getSerie().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<com.lineargs.watchnext.data.entity.PopularSerie>() {
                @Override
                public void onChanged(com.lineargs.watchnext.data.entity.PopularSerie serie) {
                    if (serie != null) {
                        imageLoad(serie);
                        mAdapter.swapSerie(serie);
                    }
                }
            });

            // Observe Cast
            viewModel.getCast().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.Credits>>() {
                @Override
                public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.Credits> credits) {
                     mCastAdapter.swapCast(credits);
                      if (credits != null && !credits.isEmpty()) {
                        handler.removeCallbacksAndMessages(null);
                        showCastData();
                    } else if (credits != null) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showEmptyCast();
                            }
                        }, 7000);
                    }
                }
            });


            // Observe Seasons
            viewModel.getSeasons().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.Seasons>>() {
                @Override
                public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.Seasons> seasons) {
                     if (binding.seriesFooter.seasons != null) {
                        if (seasons != null && !seasons.isEmpty()) {
                            binding.seriesFooter.seasons.setEnabled(true);
                            binding.seriesFooter.seasons.setTextColor(getResources().getColor(R.color.colorBlack));
                        } else {
                            binding.seriesFooter.seasons.setEnabled(false);
                            binding.seriesFooter.seasons.setTextColor(android.graphics.Color.GRAY);
                        }
                     }
                }
            });

            // Observe Videos
            viewModel.getVideos().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.Videos>>() {
                @Override
                public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.Videos> videos) {
                     if (binding.seriesFooter.videos != null) {
                        if (videos != null && !videos.isEmpty()) {
                            binding.seriesFooter.videos.setEnabled(true);
                            binding.seriesFooter.videos.setTextColor(getResources().getColor(R.color.colorBlack));
                        } else {
                            binding.seriesFooter.videos.setEnabled(false);
                            binding.seriesFooter.videos.setTextColor(android.graphics.Color.GRAY);
                        }
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
        
        if (binding.seriesFooter.seasons != null) {
            binding.seriesFooter.seasons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSeasons();
            }
        });
        }
        
        if (binding.seriesFooter.videos != null) {
            binding.seriesFooter.videos.setOnClickListener(new View.OnClickListener() {
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
        View detailFrame = getActivity().findViewById(R.id.seasons_frame_layout);
        mDualPane = detailFrame != null && detailFrame.getVisibility() == View.VISIBLE;
        if (mDualPane && savedInstanceState == null) {
            SeasonsFragment fragment = new SeasonsFragment();
            fragment.setmUri(DataContract.Seasons.buildSeasonUriWithId(Long.parseLong(mUri.getLastPathSegment())));
            getFragmentManager().beginTransaction()
                    .replace(R.id.seasons_frame_layout, fragment)
                    .commit();
            VideosTvFragment videosFragment = new VideosTvFragment();
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

    public void starFabFavorite() {
        if (DbUtils.isFavorite(getContext(), Long.parseLong(mUri.getLastPathSegment()))) {
            DbUtils.removeFromFavorites(getContext(), mUri);
            Toast.makeText(getContext(), getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
            binding.starFab.setImageDrawable(Utils.starBorderImage(getContext()));
        } else {
            DbUtils.addTVToFavorites(getContext(), mUri);
            Toast.makeText(getContext(), getString(R.string.toast_add_to_favorites), Toast.LENGTH_SHORT).show();
            binding.starFab.setImageDrawable(Utils.starImage(getContext()));
        }
    }

    public void loadCast() {
        Intent intent = (new Intent(getContext(), CreditsCastActivity.class));
        Uri uri = DataContract.Credits.buildCastUriWithId(Long.parseLong(mUri.getLastPathSegment()));
        intent.setData(uri);
        intent.putExtra(Constants.TITLE, title);
        startActivity(intent);
    }



    public void loadSeasons() {
        Intent intent = new Intent(getContext(), SeasonsActivity.class);
        Uri uri = DataContract.Seasons.buildSeasonUriWithId(Long.parseLong(mUri.getLastPathSegment()));
        intent.setData(uri);
        intent.putExtra(Constants.TITLE, title);
        startActivity(intent);
    }

    public void loadVideos() {
        Intent intent = new Intent(getContext(), VideosTvActivity.class);
        Uri uri = DataContract.Videos.buildVideoUriWithId(Long.parseLong(mUri.getLastPathSegment()));
        intent.setData(uri);
        intent.putExtra(Constants.TITLE, title);
        startActivity(intent);
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
                ServiceUtils.shareSerie(getActivity(), String.valueOf(id));
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private void imageLoad(com.lineargs.watchnext.data.entity.PopularSerie serie) {
        title = serie.getTitle();
        id = serie.getTmdbId();
        if (binding.seriesFooter.videos != null) {
            ServiceUtils.setUpVideosButton(getContext(), mUri.getLastPathSegment(), binding.seriesFooter.videos);
        }
        ServiceUtils.setUpGoogleSearchButton(title, binding.seriesButtons.google);
        ServiceUtils.setUpYouTubeButton(title, binding.seriesButtons.youtube);
        ServiceUtils.setUpGooglePlayButton(title, binding.seriesButtons.googlePlay);
        if (DbUtils.isFavorite(getContext(), id)) {
            binding.starFab.setImageDrawable(Utils.starImage(getContext()));
        } else {
            binding.starFab.setImageDrawable(Utils.starBorderImage(getContext()));
        }
        if (binding.coverPoster != null) {
            Picasso.get()
                    .load(serie.getPosterPath())
                    .fit()
                    .into(binding.coverPoster);
        }
        if (binding.coverBackdrop != null) {
            Picasso.get()
                    .load(serie.getBackdropPath())
                    .fit()
                    .into(binding.coverBackdrop);
        }
    }

    @Override
    public void onPersonClick(String id, String name) {
        if (mDualPane) {
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
}

