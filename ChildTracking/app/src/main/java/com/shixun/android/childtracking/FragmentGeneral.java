package com.shixun.android.childtracking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by shixunliu on 26/9/17.
 *
 * general method of fragment are abstracted into FragmentGeneral
 * any fragment extends from it will be instantiated
 */


public class FragmentGeneral extends Fragment {
    protected int getLayoutID() {return 0;}
    private static FragmentGeneral fragment;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutID(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
