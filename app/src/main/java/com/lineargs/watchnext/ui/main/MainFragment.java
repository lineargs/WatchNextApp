package com.lineargs.watchnext.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lineargs.watchnext.R;

/**
 * Created by goranminov on 19/11/2017.
 * <p>
 * Unused for now, we keep it for future implementations
 */

public class MainFragment extends Fragment {

    public MainFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
