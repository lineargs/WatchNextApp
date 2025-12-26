package com.lineargs.watchnext.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.TrendingPeopleAdapter;
import com.lineargs.watchnext.databinding.FragmentTrendingBinding;
import com.lineargs.watchnext.utils.retrofit.people.Person;

import java.util.List;

public class TrendingPeopleFragment extends BaseFragment implements TrendingPeopleAdapter.OnItemClickListener {

    private FragmentTrendingBinding binding;
    private TrendingPeopleAdapter adapter;
    private TrendingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTrendingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        viewModel = new ViewModelProvider(requireActivity()).get(TrendingViewModel.class);

        viewModel.getTrendingPeople().observe(getViewLifecycleOwner(), new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> people) {
                if (people != null) {
                    adapter.swapPeople(people);
                }
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading != null) {
                    binding.swipeRefreshLayout.setRefreshing(isLoading);
                }
            }
        });
    }

    private void setupViews() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        binding.trendingRecyclerView.setLayoutManager(layoutManager);
        adapter = new TrendingPeopleAdapter(requireContext(), this);
        binding.trendingRecyclerView.setAdapter(adapter);

        binding.swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(Uri uri) {
        Intent intent = new Intent(getContext(), PersonActivity.class);
        intent.setData(uri);
        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public void onToggleFavorite(Uri uri, boolean isFavorite) {
        // Favorites not supported for people yet
    }
}
