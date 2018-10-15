package ch.epfl.sweng.swengproject;

import android.arch.core.util.Function;
import android.content.Context;
import android.content.Intent;

import android.location.Location;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.PopupWindow;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.Display;
import android.view.MotionEvent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import static ch.epfl.sweng.swengproject.MainActivity.currentLocation;

import java.util.ArrayList;

public class ChooseLocationActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    static final String LOGTAG_sl = "Tag_sl";
    private Button setLocation_btn;
    private GoogleMap mMap_sl;
    private LatLng lastLatLng_sl;
    private LatLng setLatLng;
    private String setLatLng_str;
    private CurrentLocation currentLocation_sl;
    private float[] distance; // in meters
    private int max_distance;
    private boolean shouldMove;
    private boolean shouldCall;
    private boolean isOpening;
    private boolean click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        isOpening = true;
        click = false;
        max_distance = 500;
        launchDefaultLocation();
        bindSaveLocationButton();

        SupportMapFragment mapFragment_loc = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_ch_loc);
        mapFragment_loc.getMapAsync(this);

        setLocation();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap_sl = googleMap;
        currentLocation.callerActivityReady();
        mMap_sl.setOnCameraIdleListener(onCameraIdleListener);
    }

    private void bindSaveLocationButton(){
        setLocation_btn = findViewById(R.id.set_loc_btn);
        setLocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOGTAG_sl, "clicked on button");
                if (lastLatLng_sl != null) {
                    Log.d(LOGTAG_sl, "lastLatLng_sl = " + lastLatLng_sl);
                    while (locationTooFar()) {

                    }
                }
                Intent position = new Intent();
                setLatLng_str = setLatLng.toString();
                position.setData(Uri.parse(setLatLng_str));
                setResult(RESULT_OK, position);
                finish();
            }
        });
    }


    private void launchDefaultLocation() {
        Function<Void, Void> function_sl = new Function<Void, Void>() {
            @Override
            public Void apply(Void input) {
                setDefaultLocation();
                Log.d(LOGTAG_sl, "In launchDefaultLocation, lastLatLng_sl = " + lastLatLng_sl);
                Log.d(LOGTAG_sl, "In launchDefaultLocation, setLatLng = " + setLatLng);
                return null;
            }
        };

        currentLocation.setCurrentLocationParameters(this.getApplicationContext(), this, function_sl);

    }

    private void setDefaultLocation() {
        Log.d(LOGTAG_sl, "in setDefaultLocation");

        try {
            if (currentLocation.getLocationPermissionStatus()) {
                Log.d(LOGTAG_sl, "got locationPermission");
                mMap_sl.clear(); // removes all markers, overlays... from the map
                mMap_sl.setMyLocationEnabled(true); // while enabled, location is available
                mMap_sl.getUiSettings().setMyLocationButtonEnabled(true);

                Log.d(LOGTAG_sl, "set locationEnabled");

                // request the last know location -> returns a Task
                lastLatLng_sl = currentLocation.getLastLocation();
                Log.d(LOGTAG_sl, "got lastlocation");

                mMap_sl.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng_sl, 12));
            } else {
                Log.d("ERROR", "CAN'T SET LOCATION");
            }
        } catch (SecurityException e) {
            Log.d(LOGTAG_sl, "can't get LocationPermission");

        }


    }


    private void setLocation() {
        Log.d(LOGTAG_sl, "in setLocation");
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                setLatLng = mMap_sl.getCameraPosition().target;
                Log.d(LOGTAG_sl, "in setLocation: "+setLatLng);
            }
        };
    }


    private boolean locationTooFar() {
        Log.d(LOGTAG_sl, "in locationTooFar");
        Log.d(LOGTAG_sl, "lastLatLng_sl = " + lastLatLng_sl);
        Log.d(LOGTAG_sl, "setLatLng = " + setLatLng);
        Location.distanceBetween(lastLatLng_sl.latitude, lastLatLng_sl.longitude,
                setLatLng.latitude, setLatLng.longitude, distance);
        Log.d(LOGTAG_sl, "distance computed successfully");
        if (distance[0] > max_distance) {
            Log.d(LOGTAG_sl, "yes, location is too far");
            Toast.makeText(ChooseLocationActivity.this, "It's too far away, you can't get there in time!",Toast.LENGTH_LONG).show();
            return true;
        } else {
            return false;
        }
    }

}
