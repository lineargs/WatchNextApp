package com.lineargs.watchnext.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lineargs.watchnext.R;
import com.lineargs.watchnext.adapters.CreditsCrewAdapter;
import com.lineargs.watchnext.data.CeditsQuery;
import com.lineargs.watchnext.data.DataContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by goranminov on 21/12/2017.
 * <p>
 * A fragment loading and showing list of cast members for now.
 * We will use same class to implement crew.
 */

public class CreditsCrewFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, CreditsCrewAdapter.OnClick {

    static final String ID = "id";
    private static final int LOADER_ID = 333;
    @BindView(R.id.credits_crew_recycler_view)
    RecyclerView mRecyclerView;
    private CreditsCrewAdapter mAdapter;
    private Uri mUri;
    private Uri mDualUri = null;
    private Unbinder unbinder;
    private boolean mDualPane;

    public void setmUri(Uri uri) {
        mUri = uri;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credits_crew, container, false);
        setupViews(getContext(), view);
        return view;
    }

    private void setupViews(Context context, View view) {
        unbinder = ButterKnife.bind(this, view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns());
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CreditsCrewAdapter(context, this);
        mRecyclerView.setAdapter(mAdapter);
        if (getActivity().getIntent().getData() != null) {
            mUri = getActivity().getIntent().getData();
        }
        if (getActivity().getIntent().hasExtra(MovieDetailsFragment.ID)) {
            Long id = Long.valueOf(getActivity().getIntent().getStringExtra(MovieDetailsFragment.ID));
            mDualUri = DataContract.Person.buildPersonUriWithId(id);
        }

        getLoaderManager().initLoader(LOADER_ID, null, this);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(getContext(),
                        mUri,
                        CeditsQuery.CREDITS_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader not implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_ID:
                if (data != null && data.getCount() != 0) {
                    data.moveToFirst();
                    mAdapter.swapCursor(data);
                }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onPersonClick(String id) {

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
            intent.putExtra(ID, id);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /* Binding reset */
        unbinder.unbind();
    }
}

