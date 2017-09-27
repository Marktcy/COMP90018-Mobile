package com.shixun.android.childtracking;

/**
 * Created by shixunliu on 26/9/17.
 */

public interface ActionListener {
    void loginAsParent();
    void loginAsChild();
    void startTracking();
    void stopTracking();
}
