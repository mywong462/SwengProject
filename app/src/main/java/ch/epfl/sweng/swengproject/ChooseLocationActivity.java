package ch.epfl.sweng.swengproject;

import android.arch.core.util.Function;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import static ch.epfl.sweng.swengproject.MyApplication.LOGTAG;
import static ch.epfl.sweng.swengproject.MyApplication.currentLocation;


public class ChooseLocationActivity extends FragmentActivity implements OnMapReadyCallback {
    static final String LOGTAG_sl = "Tag_sl";
    //private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private GoogleMap.OnMapClickListener onMapClickListener;
    private Button setLocation_btn;
    private GoogleMap mMap_sl;
    private LatLng lastLatLng_sl;
    private LatLng setLatLng;
    private String setLatLng_str;
    private double distance = 0.0; // in km
    private final int max_distance = 5;
    private boolean isOpening;
    private LocationServer currLoc;
    private boolean test = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        isOpening = true;
        Log.d(LOGTAG_sl, "in onCreate");

        LocationServer loc = (LocationServer) getIntent().getSerializableExtra("loc");

        Log.d(LOGTAG, "got the Serializable : " + (loc == null));
        if (loc != null) {
            currLoc = loc;
            lastLatLng_sl = loc.getLastLocation();
            setLatLng = loc.getLastLocation();
            this.test = true;
        } else {
            currLoc = currentLocation;
            launchDefaultLocation();
        }

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
        mMap_sl.setOnMapClickListener(onMapClickListener);
    }

    private void bindSaveLocationButton(){
        setLocation_btn = findViewById(R.id.set_loc_btn);
        setLocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastLatLng_sl == null || (locationTooFar() && !test)) {
                    Log.d(LOGTAG_sl, "in bindSaveLocationButton and sth went wrong");
                    setDefaultLocation();
                } else {
                    setLatLng_str = setLatLng.toString();
                    Intent position = new Intent();
                    position.putExtra("lat_code", setLatLng.latitude);
                    position.putExtra("lng_code", setLatLng.longitude);
                    //position.setData(Uri.parse(setLatLng_str));
                    setResult(RESULT_OK, position);
                    if(!test) {
                        finish();
                    }
                }
            }
        });
    }


    public void launchDefaultLocation() {
        Function<Void, Void> function_sl = new Function<Void, Void>() {
            @Override
            public Void apply(Void input) {
                setDefaultLocation();
                Log.d(LOGTAG_sl, "In launchDefaultLocation, lastLatLng_sl = " + lastLatLng_sl);
                return null;
            }
        };

        currentLocation.setCurrentLocationParameters(this.getApplicationContext(), this, function_sl);

    }

    public void setDefaultLocation() {
        Log.d(LOGTAG_sl, "in setDefaultLocation");

        try {
            if (currLoc.getLocationPermissionStatus()) {
                //mMap_sl.clear(); // removes all markers, overlays... from the map
                 Log.d(LOGTAG_sl, "in setDefaultLocation, lastLatLng = "+lastLatLng_sl);
                 Log.d(LOGTAG_sl, "in setDefaultLocation, setLatLng = "+setLatLng);
                 lastLatLng_sl = currLoc.getLastLocation();

                if(!test) {
                    mMap_sl.setMyLocationEnabled(true); // while enabled, location is available
                    mMap_sl.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap_sl.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng_sl, 12));
                }

            } else {
                Log.d("ERROR", "CAN'T SET LOCATION");
            }
        } catch (SecurityException e) {
            Log.d(LOGTAG_sl, "can't get LocationPermission");
        }
    }


    private void setLocation() {
        onMapClickListener = new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap_sl.clear();
                MarkerOptions markerOptions =
                        new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title("MEET HERE");
                mMap_sl.addMarker(markerOptions);
                setLatLng = markerOptions.getPosition();
                Log.d(LOGTAG_sl, "in setLocation, lastLatLng = "+lastLatLng_sl);
                Log.d(LOGTAG_sl, "in setLocation, setLatLng = "+setLatLng);
            }
        };
    }

    public boolean locationTooFar() {

        distance = DBTools.distanceBetween(new GeoPoint(lastLatLng_sl.latitude, lastLatLng_sl.longitude),
                new GeoPoint(setLatLng.latitude, setLatLng.longitude));
        if (distance > max_distance) {
            Toast.makeText(ChooseLocationActivity.this, "It's too far away, you can't get there in time!",Toast.LENGTH_LONG).show();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!test) {
            currentLocation.callerOnPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isOpening) {
            Log.d(LOGTAG_sl, "inOnResume");
            launchDefaultLocation();
        }
        if(!test) {
            currentLocation.callerOnResume();
        }
    }


}
