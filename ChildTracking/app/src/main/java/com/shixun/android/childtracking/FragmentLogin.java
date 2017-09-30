package com.shixun.android.childtracking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by shixunliu on 26/9/17.
 */

public class FragmentLogin extends FragmentGeneral {
    private MobileServiceClient mobileServiceClient;
    private MobileServiceTable<UserDatabase> mobileServiceTable;

    @BindView(R.id.btLogin)
    Button btLogin;
    @BindView(R.id.member)
    Spinner spMemeber;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_login;
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.login));
        super.onResume();
    }

    @OnClick(R.id.btLogin)
    void login() {
        try {
            mobileServiceClient = new MobileServiceClient(getResources().getString(R.string.server), getActivity());
            mobileServiceTable = mobileServiceClient.getTable(UserDatabase.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (isAccountPasswordValid()) {
            String userAccount = email.getText().toString();
            String userPassword = password.getText().toString();
            //String selectedMember = spMemeber.getSelectedItem().toString().trim();
            //int result = UserDataManager.findUser(userAccount, userPassword);
            UserDatabase user = new UserDatabase(userAccount,userPassword);
            parameters.add(new Pair<>("email" , userAccount));
            parameters.add(new Pair<>("password" , userPassword));





            if (result == 1) {
                if (selectedMember == getResources().getString(R.string.parent)) {
                    ((ActionListener) getActivity()).loginAsParent();
                } else if (selectedMember == getResources().getString(R.string.child)) {
                    ((ActionListener) getActivity()).loginAsChild();
                }
                Toast.makeText(getActivity(), "Login in successful", Toast.LENGTH_SHORT).show();
            } else if (result == 0) {
                Toast.makeText(getActivity(), "Login in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isAccountPasswordValid() {
        if (email.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Account cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}



