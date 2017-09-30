package com.shixun.android.childtracking;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.OnClick;

/**
 * Created by gongmengyu on 2017/9/29.
 */

public class FragmentHome extends FragmentGeneral {

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.btLogin)
    void jumpToLogin() {
    }

    @OnClick(R.id.register)
    void register() {
        if(getActivity() instanceof ActionListener) {
            ((ActionListener) getActivity()).jumpToRegister();
        }
    }
}
