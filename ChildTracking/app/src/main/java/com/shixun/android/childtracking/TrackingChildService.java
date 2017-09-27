package com.shixun.android.childtracking;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.squareup.okhttp.OkHttpClient;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TrackingChildService extends Service {

    private Timer mTimer;
    private MobileServiceClient mClient;
    private MobileServiceTable<ChildLocation>  mCTable;
    private ChildLocation mChildLocation;
    private LatLng mLatLng;

    public TrackingChildService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();

        try {
            // Mobile Service URL and key
            mClient = new MobileServiceClient(getResources().getString(R.string.server), this);

            // Extend timeout from default of 10s to 20s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    OkHttpClient client = new OkHttpClient();
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });

            mCTable = mClient.getTable(ChildLocation.class);
        } catch (Exception e) {

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("The process", "run: ");
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("******************", "run: ");
                try {
                    mChildLocation = mCTable.where().orderBy("CreatedAt", QueryOrder.Descending).execute().get().get(0);
                    mLatLng = new LatLng(mChildLocation.getLatitude(), mChildLocation.getLongtitude());
                    EventBus.getDefault().post(mLatLng);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }, 1000, 3000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}
