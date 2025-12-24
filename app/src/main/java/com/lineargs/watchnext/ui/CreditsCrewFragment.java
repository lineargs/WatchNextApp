package com.lineargs.watchnext.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.CreditsCrewAdapter;
import com.lineargs.watchnext.data.CreditsQuery;
import com.lineargs.watchnext.data.DataContract;
import com.lineargs.watchnext.utils.Constants;

import com.lineargs.watchnext.databinding.FragmentCreditsCrewBinding;

/**
 * Created by goranminov on 21/12/2017.
 * <p>
 * A fragment loading and showing list of cast members for now.
 * We will use same class to implement crew.
 */

public class CreditsCrewFragment extends BaseFragment implements CreditsCrewAdapter.OnClick {

    private FragmentCreditsCrewBinding binding;
    private CreditsCrewAdapter mAdapter;
    private Uri mUri;
    private Uri mDualUri = null;
    private boolean mDualPane;
    private CreditsViewModel viewModel;


    public void setmUri(Uri uri) {
        mUri = uri;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreditsCrewBinding.inflate(inflater, container, false);
        setupViews(getContext());
        return binding.getRoot();
    }

    private void setupViews(Context context) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        binding.creditsCrewRecyclerView.setLayoutManager(gridLayoutManager);
        binding.creditsCrewRecyclerView.setHasFixedSize(true);
        mAdapter = new CreditsCrewAdapter(context, this);
        binding.creditsCrewRecyclerView.setAdapter(mAdapter);
        if (getActivity().getIntent().getData() != null) {
            mUri = getActivity().getIntent().getData();
        }
        if (getActivity().getIntent().hasExtra(Constants.ID)) {
            Long id = Long.valueOf(getActivity().getIntent().getStringExtra(Constants.ID));
            mDualUri = DataContract.Person.buildPersonUriWithId(id);
        }

        // Initialize ViewModel
        viewModel = new androidx.lifecycle.ViewModelProvider(this).get(CreditsViewModel.class);
        if (mUri != null) {
            int movieId = Integer.parseInt(mUri.getLastPathSegment());
            viewModel.setMovieId(movieId);
            viewModel.getCrew().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<java.util.List<com.lineargs.watchnext.data.entity.Credits>>() {
                @Override
                public void onChanged(java.util.List<com.lineargs.watchnext.data.entity.Credits> crew) {
                    mAdapter.swapCrew(crew);
                }
            });
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /* Check if the device is tablet/sw600dp */
        View personFrame = getActivity().findViewById(R.id.frame_layout_person);
        mDualPane = personFrame != null && personFrame.getVisibility() == View.VISIBLE;
        if (mDualPane && mDualUri != null && savedInstanceState == null) {
            PersonFragment fragment = new PersonFragment();
            fragment.setmUri(mDualUri);
            fragment.setId(mDualUri.getLastPathSegment());
            getFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_person, fragment)
                    .commit();
        }
    }

    @Override
    public void onPersonClick(String id, String name) {

        /* If the device is tablet use the frame layout
         * to inject a view
         */
        if (mDualPane) {
            PersonFragment fragment = new PersonFragment();
            fragment.setmUri(DataContract.Person.buildPersonUriWithId(Long.parseLong(id)));
            fragment.setId(id);
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout_person, fragment)
                    .commit();
        } else {
            /* Else start activity*/
            Intent intent = new Intent(getContext(), PersonActivity.class);
            intent.setData(DataContract.Person.buildPersonUriWithId(Long.parseLong(id)));
            intent.putExtra(Constants.ID, id);
            intent.putExtra(Constants.NAME, name);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /* Binding reset */
        /* Binding reset */
        binding = null;
    }
}

