package com.lineargs.watchnext.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.sync.syncpeople.PersonSyncUtils;
import com.lineargs.watchnext.utils.Constants;
import com.lineargs.watchnext.utils.ServiceUtils;

import com.lineargs.watchnext.databinding.FragmentPersonBinding;

public class PersonFragment extends Fragment {

    private FragmentPersonBinding binding;
    private Uri mUri;
    private String id = "";
    private String profilePath = ""; // To store path for fullscreen
    private PersonViewModel viewModel;

    public PersonFragment() {
    }

    public void setmUri(Uri uri) {
        mUri = uri;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonBinding.inflate(inflater, container, false);
        if (savedInstanceState == null) {
            PersonSyncUtils.syncPerson(getContext(), id);
            startLoading();
        }
        
        binding.stillPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFullscreen();
            }
        });
        
        // Initialize ViewModel
        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(PersonViewModel.class);
        if (!TextUtils.isEmpty(id)) {
            try {
                int personId = Integer.parseInt(id);
                viewModel.setPersonId(personId);
                viewModel.getPerson().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<com.lineargs.watchnext.data.entity.Person>() {
                     @Override
                     public void onChanged(com.lineargs.watchnext.data.entity.Person person) {
                         if (person != null) {
                             loadViews(person);
                             showData();
                         }
                     }
                });
            } catch (NumberFormatException e) {
                // Handle parsing error
            }
        }
        
        return binding.getRoot();
    }

    private void startLoading() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setEnabled(false);
            binding.swipeRefreshLayout.setRefreshing(true);
        }
        binding.personNestedView.setVisibility(View.INVISIBLE);
    }

    private void showData() {
        if (binding.swipeRefreshLayout != null) {
            binding.swipeRefreshLayout.setRefreshing(false);
        }
        binding.personNestedView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void openFullscreen() {
        if (!TextUtils.isEmpty(profilePath)) {
            Intent fullscreen = new Intent(getActivity(), PictureActivity.class);
            fullscreen.putExtra(Constants.STILL_PATH, profilePath);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(getActivity(),
                            binding.stillPath,
                            ViewCompat.getTransitionName(binding.stillPath));
            startActivity(fullscreen, optionsCompat.toBundle());
        }
    }

    private void loadViews(com.lineargs.watchnext.data.entity.Person person) {
        profilePath = person.getProfilePath();
        if (getActivity() != null) {
            getActivity().setTitle(person.getName());
            if (getActivity() instanceof androidx.appcompat.app.AppCompatActivity) {
                androidx.appcompat.app.ActionBar actionBar = ((androidx.appcompat.app.AppCompatActivity) getActivity()).getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(person.getName());
                }
            }
        }
        ServiceUtils.loadPicasso(binding.stillPath.getContext(), person.getProfilePath())
                .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                .centerCrop()
                .into(binding.stillPath);
        if (TextUtils.isEmpty(person.getBiography())) {
            binding.biography.setText(getString(R.string.biography_not_available));
        } else {
            binding.biography.setText(person.getBiography());
        }
        
        if (TextUtils.isEmpty(person.getPlaceOfBirth())) {
            binding.placeOfBirth.setVisibility(View.GONE);
        } else {
            binding.placeOfBirth.setText(person.getPlaceOfBirth());
        }
        if (TextUtils.isEmpty(person.getHomepage())) {
            binding.homepage.setVisibility(View.GONE);
        } else {
            binding.homepage.setText(person.getHomepage());
        }
    }
}
