package com.shixun.android.childtracking;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.internal.zzagz.runOnUiThread;

public class FragmentParent extends FragmentGeneral implements OnMapReadyCallback {

    private GoogleMap                          mMap;
    private LatLng                             userLocation;
    private MobileServiceClient                mClient;
    private MobileServiceTable<ParentLocation> mTable;
    private MobileServiceTable<ChildLocation>  mCTable;
    private final static int                   PLACE_PICKER_REQUEST = 1;
    private Place                              place;
//    private TrackingChildService.TrackingBinder mTrackingBinder;

    @BindView(R.id.tvPlaceName)
    TextView placeNameText;
    @BindView(R.id.tvPlaceAddress)
    TextView placeAddressText;
    @BindView(R.id.etRadius)
    EditText radius;
    @BindView(R.id.btSetBoundary)
    Button setBoundary;
    @BindView(R.id.btGetPlace)
    Button getPlaceButton;
    @BindView(R.id.btStartTracking)
    Button btStartTracking;
    @BindView(R.id.map)
    MapView mapView;

    /**
     * Rewrite the method of FragmentGeneral
     * @return the specific layout of fragment
     */
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_set_boundary;
    }

    /**
     * Create the mapView
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);

        // set the title
        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity())
                .getSupportActionBar()
                .setTitle(getResources().getString(R.string.setBoundary));
        super.onResume();

        try {
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    getResources().getString(R.string.server),
                    getActivity());

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

            // Get the Mobile Service Table instance to use
            mTable = mClient.getTable(ParentLocation.class);
            mCTable = mClient.getTable(ChildLocation.class);

        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e) {
            createAndShowDialog(e, "Error");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(getActivity(), data);
                placeNameText.setText(place.getName());
                placeAddressText.setText(place.getAddress());
                userLocation = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @OnClick(R.id.btGetPlace)
    void getPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = builder.build(getActivity());
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.btSetBoundary)
    void setBoundary() {

        final ParentLocation boundary;

        if (userLocation  == null || radius.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Please set location or radius", Toast.LENGTH_SHORT).show();
        } else {
            boundary = new ParentLocation(userLocation.longitude, userLocation.latitude, Integer.parseInt(radius.getText().toString()));
            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        mTable.insert(boundary).get();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final Exception e) {
                        createAndShowDialogFromTask(e, "Error");
                    }
                    return null;
                }
            };
            runAsyncTask(task);

            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation).title("Center"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            mMap.addCircle(new CircleOptions()
                    .center(userLocation)
                    .radius(Integer.parseInt(radius.getText().toString()))
                    .strokeWidth(10)
                    .strokeColor(Color.GREEN)
                    .fillColor(Color.argb(128, 255, 0, 0))
                    .clickable(true));
        }
    }

    @OnClick(R.id.btStartTracking)
    void startTracking() {
        if(getActivity() instanceof ActionListener) {
            ((ActionListener) getActivity()).startTracking();
        }
    }

    @OnClick(R.id.btStopTracking)
    void stopTracking() {
        if(getActivity() instanceof ActionListener) {
            ((ActionListener) getActivity()).stopTracking();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LatLng latLng) {
        Log.d("##############", Double.toString(latLng.latitude));
        Log.d("##############", Double.toString(latLng.longitude));

        mMap.addMarker(new MarkerOptions().position(latLng).title("Your Child Location"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //    private ServiceConnection mConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            mTrackingBinder = (TrackingChildService.TrackingBinder) service;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };

    /**
     * Creates a dialog and shows it
     *
     * @param exception The exception to show in the dialog
     * @param title     The dialog title
     */
    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createAndShowDialog(exception, "Error");
            }
        });
    }


    /**
     * Creates a dialog and shows it
     *
     * @param exception The exception to show in the dialog
     * @param title     The dialog title
     */
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if (exception.getCause() != null) {
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param message The dialog message
     * @param title   The dialog title
     */
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    /**
     * Run an ASync task on the corresponding executor
     *
     * @param task
     * @return
     */
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }
}
