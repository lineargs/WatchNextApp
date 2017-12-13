package com.lineargs.watchnext.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

/**
 * Created by goranminov on 16/11/2017.
 * <p>
 * Abstract class for fragments using Grid Layout Manager
 */

public abstract class BaseFragment extends Fragment {

    BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @return number of columns depending on the
     * device screen size
     */
    protected int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 3) {
            return 3;
        }
        return nColumns;
    }
}
