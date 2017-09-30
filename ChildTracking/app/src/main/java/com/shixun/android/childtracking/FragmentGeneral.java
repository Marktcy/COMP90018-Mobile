package com.shixun.android.childtracking;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by shixunliu on 26/9/17.
 */

public class FragmentGeneral extends Fragment {
    public Vibrator vibrator;
    protected int getLayoutID() {return 0;}
    private static FragmentGeneral fragment;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        vibrator=(Vibrator)getActivity().getSystemService(Service.VIBRATOR_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutID(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public static FragmentGeneral newInstance() {

        Bundle args = new Bundle();
        if(fragment == null) {
            fragment = new FragmentGeneral();
            fragment.setArguments(args);
        }
        return fragment;
    }
}
