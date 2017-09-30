package com.shixun.android.childtracking;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public abstract class AbstractFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();
    protected FragmentManager mFragmentManager = null;
    //the system time when click back button at first time
    private long lastBackTime = 0;
    //the system time when click back button at second time
    private long currentBackTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_abstract);

        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            mFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        } else {
            Log.d("activity", "Not null");
        }

        //unable the screen orientatoin
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //when click on back button
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //get the current system time
            currentBackTime = System.currentTimeMillis();
            //if interval between two clicking is bigger than 2 seconds
            if(currentBackTime - lastBackTime > 2 * 1000){
                Toast.makeText(this, "Press again to quit the APP", Toast.LENGTH_SHORT).show();
                lastBackTime = currentBackTime;
            }else{
                //if interval is less than 2 seconds, quit the app
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
