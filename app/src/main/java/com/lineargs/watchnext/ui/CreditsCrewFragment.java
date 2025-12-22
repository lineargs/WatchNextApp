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

    private static final int LOADER_ID = 333;

    private CreditsCrewAdapter mAdapter;
    private Uri mUri;
    private Uri mDualUri = null;
    private Unbinder unbinder;
    private boolean mDualPane;

    @BindView(R.id.credits_crew_recycler_view)
    RecyclerView mRecyclerView;

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
        if (getActivity().getIntent().hasExtra(Constants.ID)) {
            Long id = Long.valueOf(getActivity().getIntent().getStringExtra(Constants.ID));
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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(getContext(),
                        mUri,
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
            case LOADER_ID:
                if (data != null && data.getCount() != 0) {
                    data.moveToFirst();
                    mAdapter.swapCursor(data);
                }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
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
        unbinder.unbind();
    }
}

