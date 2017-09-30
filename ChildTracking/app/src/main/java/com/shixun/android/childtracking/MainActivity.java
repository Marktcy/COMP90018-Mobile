package com.shixun.android.childtracking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

/**
 * Created by shixunliu on 27/9/17.
 */

public class MainActivity extends AbstractFragmentActivity implements ActionListener, View.OnClickListener {

    private final static int MY_PERMISSION_FINE_LOCATION = 101;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        requestPermission();
    }

    @Override
    protected Fragment createFragment() {
        return new FragmentLogin();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void loginAsParent() {
        nevigateToFragment(FragmentParent.class, false, null);
    }

    @Override
    public void loginAsChild() {
        nevigateToFragment(FragmentChild.class, false, null);
    }

    @Override
    public void startTracking() {
        Intent startIntent = new Intent(this, TrackingChildService.class);
        startService(startIntent);
    }

    @Override
    public void stopTracking() {
        Intent stopIntent = new Intent(this, TrackingChildService.class);
        stopService(stopIntent);
    }

    @Override
    public void jumpToLogin(Bundle bundle) {
        nevigateToFragment(FragmentLogin.class, false, bundle);
    }

    @Override
    public void jumpToRegister() {
        nevigateToFragment(FragmentRegister.class, false, null);
    }

    private void nevigateToFragment(Class FragmentGeneral, boolean isUpSet, Bundle bundle) {

        try{
            Object obj = FragmentGeneral.newInstance();

            FragmentGeneral generalFragment = (FragmentGeneral) obj;

            if(bundle != null) {
                generalFragment.setArguments(bundle);
            }

            if(isUpSet) {
                this.mFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, generalFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                this.mFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, generalFragment)
                        .commit();
            }
        } catch (Exception e) {

        }
    }

    /**
     * ask for permission of fine location service
     */
    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }
    }

    /**
     * check the permission result, if denied, cancel the app
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "This app requests location permissions to be granted", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
