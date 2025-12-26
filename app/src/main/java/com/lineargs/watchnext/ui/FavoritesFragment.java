package com.lineargs.watchnext.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.FavoritesAdapter;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.data.entity.Favorites;
import com.lineargs.watchnext.databinding.FragmentFavoritesBinding;

public class FavoritesFragment extends Fragment {

    private static final String PREF_SORT_ORDER = "pref_sort_order";
    private static final String ASC = " ASC";
    
    private FragmentFavoritesBinding binding;
    private FavoritesAdapter mAdapter;
    private FavoritesViewModel viewModel;
    private String currentSortOrder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(requireActivity()).get(FavoritesViewModel.class);
        
        setupViews();
    }

    private void setupViews() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        binding.mainRecyclerView.setLayoutManager(layoutManager);
        binding.mainRecyclerView.setHasFixedSize(true);
        
        mAdapter = new FavoritesAdapter(getContext(), this::onItemSelected, this::onStarClicked);
        binding.mainRecyclerView.setAdapter(mAdapter);
        
        android.content.SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        currentSortOrder = sharedPreferences.getString(PREF_SORT_ORDER, DataContract.PopularMovieEntry.COLUMN_TITLE + ASC);

        viewModel.getFavorites().observe(getViewLifecycleOwner(), favorites -> {
            if (favorites != null && !favorites.isEmpty()) {
                mAdapter.submitList(favorites, () -> {
                    if (binding.mainRecyclerView != null) {
                        binding.mainRecyclerView.scrollToPosition(0);
                    }
                });
                showData();
            } else {
                hideData();
            }
        });
    }

    private void onItemSelected(Uri uri, boolean isSeries) {
        if (isSeries) {
            Intent intent = new Intent(getContext(), SeriesDetailsActivity.class);
            intent.setData(uri);
            intent.putExtra(MainActivity.FAB_ID, 1);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
            intent.setData(uri);
            intent.putExtra(MainActivity.FAB_ID, 1);
            startActivity(intent);
        }
    }

    private void onStarClicked(Favorites favorite) {
        viewModel.removeFavorite(favorite.getTmdbId());
        Toast.makeText(getContext(), getString(R.string.toast_remove_from_favorites), Toast.LENGTH_SHORT).show();
    }

    private void showData() {
        binding.emptyLayout.getRoot().setVisibility(View.GONE);
        binding.mainRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideData() {
        binding.emptyLayout.getRoot().setVisibility(View.VISIBLE);
        binding.mainRecyclerView.setVisibility(View.GONE);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 900;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        return Math.max(nColumns, 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
