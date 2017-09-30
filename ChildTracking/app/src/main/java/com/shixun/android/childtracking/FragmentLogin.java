package com.shixun.android.childtracking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by shixunliu on 26/9/17.
 */

public class FragmentLogin extends FragmentGeneral {
    private MobileServiceClient mobileServiceClient;

    @BindView(R.id.btLogin)
    com.dd.processbutton.iml.ActionProcessButton buttonLogin;
    @BindView(R.id.btRegister)
    com.dd.processbutton.iml.ActionProcessButton buttonRegister;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_login;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            email.setText(bundle.getString("account"), TextView.BufferType.EDITABLE);
            password.setText(bundle.getString("password"), TextView.BufferType.EDITABLE);
        }
    }

    @Override
    public void onResume() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.login));
        super.onResume();
    }

    @OnClick(R.id.btRegister)
    void register() {
        if(getActivity() instanceof ActionListener) {
            ((ActionListener) getActivity()).jumpToRegister();
        }
    }

    @OnClick(R.id.btLogin)
    void login() {
        try {
            mobileServiceClient = new MobileServiceClient(getResources().getString(R.string.server), getActivity());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (isAccountPasswordValid()) {
            String userAccount = email.getText().toString();
            String userPassword = password.getText().toString();

            ArrayList<Pair<String, String>> parameters = new ArrayList<>();

            parameters.add(new Pair<>("email" , userAccount));
            parameters.add(new Pair<>("password" , userPassword));
            parameters.add(new Pair<>("type", "checkPassword"));

            ListenableFuture<CheckAccount> result = mobileServiceClient.invokeApi("Authentication", "GET", parameters, CheckAccount.class);
            Futures.addCallback(result, new FutureCallback<CheckAccount>() {
                @Override
                public void onSuccess(CheckAccount result) {
                    if (result.result.equals("Parent")) {
                        if(getActivity() instanceof ActionListener) {
                            ((ActionListener) getActivity()).loginAsParent();
                        }
                    } else if (result.result.equals("Children")) {
                        if(getActivity() instanceof ActionListener) {
                            ((ActionListener) getActivity()).loginAsChild();
                        }
                    } else {
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Wrong account and password!")
                                .show();
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("SignIn failed!")
                            .show();
                }
            });
        }
    }

    public boolean isAccountPasswordValid() {
        if (email.getText().toString().trim().equals("") || password.getText().toString().trim().equals("")) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Account and password cannot be empty")
                    .show();
            return false;
        }
        return true;
    }

}



