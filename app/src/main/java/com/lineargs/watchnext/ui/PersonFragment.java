package com.lineargs.watchnext.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.data.Person;
import com.lineargs.watchnext.data.PersonViewModel;
import com.lineargs.watchnext.utils.Constants;
import com.lineargs.watchnext.utils.ServiceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PersonFragment extends Fragment {

    @BindView(R.id.still_path)
    ImageView photo;
    @BindView(R.id.biography)
    AppCompatTextView biography;
    @BindView(R.id.place_of_birth)
    AppCompatTextView placeOfBirth;
    @BindView(R.id.homepage)
    AppCompatTextView homepage;
    @BindView(R.id.person_nested_view)
    NestedScrollView mNestedView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    private String id = "";
    private Unbinder unbinder;
    private Person people;

    public PersonFragment() {
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        unbinder = ButterKnife.bind(this, view);
        PersonViewModel personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        if (savedInstanceState == null) {
            personViewModel.getPersonDetails(id);
        } else {
            id = savedInstanceState.getString(Constants.ID);
        }
        personViewModel.getPerson(Integer.parseInt(id)).observe(this, new Observer<Person>() {
            @Override
            public void onChanged(Person person) {
                loadViews(person);
                people = person;
            }
        });
        return view;
    }

    private void startLoading() {
        mNestedView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void showData() {
        mNestedView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(Constants.ID, id);
    }

    @OnClick(R.id.still_path)
    public void openFullscreen() {
        Intent fullscreen = new Intent(getActivity(), PictureActivity.class);
        fullscreen.putExtra(Constants.STILL_PATH, people.getProfilePath());
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(),
                        photo,
                        ViewCompat.getTransitionName(photo));
        startActivity(fullscreen, optionsCompat.toBundle());
    }

    private void loadViews(Person person) {
        if (person != null) {
            showData();
            ServiceUtils.loadPicasso(photo.getContext(), person.getProfilePath())
                    .resizeDimen(R.dimen.movie_poster_width_default, R.dimen.movie_poster_height_default)
                    .centerCrop()
                    .into(photo);
            if (person.getBiography() == null) {
                biography.setText(getString(R.string.biography_not_available));
            } else {
                biography.setText(person.getBiography());
            }
            /* We can use same logic from the biography for the place of birth or
             * homepage, just one small but big step for the mankind is that the Movie Db
             * API returns {String or null} for these. Still no bother implementing for now.
             * Just sayin'
             * UPDATE: Took only less than a minute to implement that. Do not be lazy please :)
             */
            if (person.getPlaceOfBirth() == null) {
                placeOfBirth.setVisibility(View.GONE);
            } else {
                placeOfBirth.setText(person.getPlaceOfBirth());
            }
            if (person.getHomepage() == null) {
                homepage.setVisibility(View.GONE);
            } else {
                homepage.setText(person.getHomepage());
            }
        } else {
            startLoading();
        }
    }
}
