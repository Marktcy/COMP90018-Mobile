package com.shixun.android.childtracking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by shixunliu on 26/9/17.
 */

public class FragmentLogin extends FragmentGeneral {

    @BindView(R.id.btLogin)
    Button btLogin;
    @BindView(R.id.member)
    Spinner spMemeber;

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

        if(getActivity() instanceof ActionListener) {

            String selectedMember = spMemeber.getSelectedItem().toString();

            if (selectedMember == getResources().getString(R.string.parent)) {
                ((ActionListener) getActivity()).loginAsParent();
            } else if (selectedMember == getResources().getString(R.string.child)) {
                ((ActionListener) getActivity()).loginAsChild();
            }
        }
    }


}
