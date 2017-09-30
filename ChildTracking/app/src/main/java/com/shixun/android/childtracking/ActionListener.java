package com.shixun.android.childtracking;

import android.os.Bundle;

/**
 * Created by shixunliu on 26/9/17.
 */

public interface ActionListener {
    void loginAsParent();
    void loginAsChild();
    void startTracking();
    void stopTracking();
    void jumpToLogin(Bundle bundle);
    void jumpToRegister();

}