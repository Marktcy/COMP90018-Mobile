package com.shixun.android.childtracking;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.squareup.okhttp.OkHttpClient;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.MalformedURLException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.internal.zzagz.runOnUiThread;

public class FragmentParent extends FragmentGeneral implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap                          mMap;
    private LatLng                             userLocation;
    private MobileServiceClient                mClient;
    private MobileServiceTable<ParentLocation> mTable;
    private MobileServiceTable<ChildLocation>  mCTable;
    private final static int                   PLACE_PICKER_REQUEST = 1;
    private Place                              place;
    private View                               guillotineMenu;
    private ImageView                          setBoundary;
    private ImageView                          startTracking;
    private ImageView                          stopTracking;
    private Marker                             childMarker;
    private String                             radius;
    private TextToSpeech                       mTextToSpeech;

    @BindView(R.id.etRadius)
    EditText etRadius;
    @BindView(R.id.btConfirmBoundary)
    at.markushi.ui.CircleButton btConfirmBoundary;
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.root)
    FrameLayout root;
    @BindView(R.id.content_hamburger)
    View contentHamburger;


    /**
     * Rewrite the method of FragmentGeneral
     * @return the specific layout of fragment
     */
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_monitoring;
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
        guillotineMenu = LayoutInflater.from(getActivity()).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);
        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(10)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();
        setBoundary = (ImageView) guillotineMenu.findViewById(R.id.set_boundary);
        startTracking = (ImageView) guillotineMenu.findViewById(R.id.start_tracking);
        stopTracking = (ImageView) guillotineMenu.findViewById(R.id.stop_tracking);
        setBoundary.setOnClickListener(this);
        startTracking.setOnClickListener(this);
        stopTracking.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);

        mTextToSpeech=new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    mTextToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        if (toolbar != null) {
            // set the title
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            ((AppCompatActivity) getActivity())
                    .getSupportActionBar()
                    .setTitle(null);
        }

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
                userLocation = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_boundary:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.start_tracking:
                if(getActivity() instanceof ActionListener) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Start tracking the child!")
                            .setCancelText("No,cancel plx!")
                            .setConfirmText("Yes,do it!")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    ((ActionListener) getActivity()).startTracking();
                                }
                            })
                            .show();
                }

                break;
            case R.id.stop_tracking:
                if(getActivity() instanceof ActionListener) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Stop tracking the child!")
                            .setCancelText("No,cancel plx!")
                            .setConfirmText("Yes,do it!")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    ((ActionListener) getActivity()).stopTracking();
                                    if (childMarker != null) {
                                        childMarker.remove();
                                    }
                                }
                            })
                            .show();
                }
                break;
        }
    }


    @OnClick(R.id.btConfirmBoundary)
    void setBoundary() {

        radius = etRadius.getText().toString();

        if (userLocation  == null || radius.equals("")) {
            Toast.makeText(getActivity(), "Please set location or radius", Toast.LENGTH_SHORT).show();
        } else {

            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("The boundary " + radius + "M will be set around " + place.getAddress() + "!")
                    .setCancelText("No,cancel plx!")
                    .setConfirmText("Yes,do it!")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            final ParentLocation boundary = new ParentLocation(userLocation.longitude, userLocation.latitude, Integer.parseInt(radius));
                            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    try {
                                        mTable.insert(boundary).get();
                                    } catch (final Exception e) {
                                        createAndShowDialogFromTask(e, "Error");
                                    }
                                    return null;
                                }
                            };

                            runAsyncTask(task);

                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(userLocation).title("Center"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 20));
                            mMap.addCircle(new CircleOptions()
                                    .center(userLocation)
                                    .radius(Integer.parseInt(radius))
                                    .strokeWidth(2)
                                    .strokeColor(Color.GREEN)
                                    .fillColor(Color.argb(128, 255, 0, 0))
                                    .clickable(false));
                        }
                    })
                    .show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LatLng latLng) {
        if (childMarker != null) {
            childMarker.remove();
        }
        childMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Your Child Location"));

        double distance = SphericalUtil.computeDistanceBetween(latLng, place.getLatLng());
        if (distance > Double.parseDouble(radius)) {
            //this.vibrator.vibrate(500);
            mTextToSpeech.speak("Your child is out of boundary", TextToSpeech.QUEUE_FLUSH, null,"1");
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(mTextToSpeech !=null){
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
    }

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
