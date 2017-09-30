package com.shixun.android.childtracking;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.sync.pull.IncrementalPullStrategy;

import java.net.MalformedURLException;

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

    @OnClick(R.id.btRegister)
    void register() {
        if(getActivity() instanceof ActionListener) {
            ((ActionListener) getActivity()).jumpToRegister();
        }
    }
}
