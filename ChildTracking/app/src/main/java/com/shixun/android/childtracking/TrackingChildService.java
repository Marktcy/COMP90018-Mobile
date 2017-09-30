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

    public TrackingChildService() {}

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

            //Connect to ChildLocations Databases
            mCTable = mClient.getTable(ChildLocation.class);

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
        } catch (Exception e) {}
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("The process", "run: ");
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // Query of retrieve lastest record on the databases
                    ChildLocation mChildLocation = mCTable.where().orderBy("CreatedAt", QueryOrder.Descending).execute().get().get(0);
                    LatLng mLatLng = new LatLng(mChildLocation.getLatitude(), mChildLocation.getLongtitude());
                    EventBus.getDefault().post(mLatLng);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }, 1000, 3000); //After first 1000ms, execute every 3000ms

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}
