package com.shixun.android.childtracking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gongmengyu on 2017/9/29.
 */

public class FragmentRegister extends FragmentGeneral {
    private MobileServiceClient mobileServiceClient;
    private MobileServiceTable<UserDatabase> mobileServiceTable;
    private List<Pair<String, String>> parameters = new ArrayList<>();

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

    @OnClick(R.id.btRegister)
    void register() {
        try {
            mobileServiceClient = new MobileServiceClient(getResources().getString(R.string.server), getActivity());
            mobileServiceTable = mobileServiceClient.getTable(UserDatabase.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (isAccountPasswordValid()) {

            UserDatabase newUser = new UserDatabase(email.getText().toString(),password.getText().toString(),name.getText().toString(), spMemeber.getSelectedItem().toString().equals("Parent"));

            String userAccount = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            parameters.add(new Pair<>("email", userAccount));
            parameters.add(new Pair<>("password", userPassword));
            parameters.add(new Pair<>("type", "isEmailExist"));



           // ListenableFuture<CheckEmail> result = mobileServiceClient.invokeApi("Authentication", "GET", parameters, CheckEmail.class);

            mobileServiceTable.insert(newUser);
//            int user = UserDataManager.findUserByName(userAccount);
//
//            if (user > 0) {
//                Toast.makeText(getActivity(), "User has existed", Toast.LENGTH_SHORT).show();
//                return;
//            } else {
//                UserDatabase newUser = new UserDatabase(userAccount, userPassword);
//                mobileServiceTable.insert(newUser);
//                if (add == -1) {
//                    Toast.makeText(getActivity(), "Register fail", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), "Rigister success", Toast.LENGTH_SHORT).show();
//                    ((ActionListener) getActivity()).jumpToLogin();
//                }
//            }
        }
    }

    public boolean isAccountPasswordValid() {
        if (name.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }else if (email.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Account cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (repassword.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please verify your password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!repassword.getText().toString().trim().equals(password.getText().toString().trim())){
            Toast.makeText(getActivity(), "Password is not the same", Toast.LENGTH_SHORT).show();
            return false;
        } else if (spMemeber.getSelectedItem().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please select your role", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }



}
