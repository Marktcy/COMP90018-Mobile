package com.shixun.android.childtracking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
 * Created by gongmengyu on 2017/9/29.
 */

public class FragmentRegister extends FragmentGeneral {
    private MobileServiceClient mobileServiceClient;
    private MobileServiceTable<UserDatabase> mobileServiceTable;

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.repassword)
    EditText repassword;
    @BindView(R.id.member)
    Spinner spMemeber;
    @BindView(R.id.btRegister)
    Button register;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_register;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Spinner spinner = (Spinner) getActivity().findViewById(R.id.member);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.member, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Back");
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getFragmentManager().popBackStack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btRegister)
    void signup() {
        try {
            mobileServiceClient = new MobileServiceClient(getResources().getString(R.string.server), getActivity());
            mobileServiceTable = mobileServiceClient.getTable(UserDatabase.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (isAccountPasswordValid()) {

            final UserDatabase newUser = new UserDatabase(email.getText().toString(),password.getText().toString(),name.getText().toString(), spMemeber.getSelectedItem().toString().equals("Parent"));

            final String userAccount = email.getText().toString();
            final String userPassword = password.getText().toString();

            ArrayList<Pair<String, String>> parameters = new ArrayList<>();

            parameters.add(new Pair<>("email", userAccount));
            parameters.add(new Pair<>("password", userPassword));
            parameters.add(new Pair<>("type", "isEmailExist"));

            ListenableFuture<CheckEmail> result = mobileServiceClient.invokeApi("Authentication", "GET", parameters, CheckEmail.class);
            Futures.addCallback(result, new FutureCallback<CheckEmail>() {
                @Override
                public void onSuccess(CheckEmail result) {
                    if (result.result.equals("NotExist")) {
                        mobileServiceTable.insert(newUser);
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Good job!")
                                .setContentText("You register the account!")
                                .show();

                        Bundle bundle = new Bundle();
                        bundle.putString("account", userAccount);
                        bundle.putString("password", userPassword);
                        if(getActivity() instanceof ActionListener) {
                            ((ActionListener) getActivity()).jumpToLogin(bundle);
                        }
                    } else {
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("This Email is already registered!")
                                .show();
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Register failed!")
                            .show();
                }
            });
        }
    }

    public boolean isAccountPasswordValid() {
        if (name.getText().toString().trim().equals("")) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please enter your name!")
                    .show();
            return false;
        }else if (email.getText().toString().trim().equals("")) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please enter your email!")
                    .show();
            return false;
        } else if (password.getText().toString().trim().equals("")) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please enter your password!")
                    .show();
            return false;
        } else if (repassword.getText().toString().trim().equals("")) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please verify password!")
                    .show();
            return false;
        } else if (!repassword.getText().toString().trim().equals(password.getText().toString().trim())){
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Password is not the same!")
                    .show();
            return false;
        } else if (spMemeber.getSelectedItem().toString().trim().equals("")) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Please select your role!")
                    .show();
            return false;
        }
        return true;
    }
}
