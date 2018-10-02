package ch.epfl.sweng.swengproject;


import android.Manifest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int LOCATION_REQUEST_CODE = 99;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location mLastKnownLocation;

    private LocationCallback mLocationCallback;

    private LocationRequest mLocationRequest;

    private Boolean mLocationPermission;

    private Boolean isOpening;

    private Boolean isLocationSettingsDemandDisplayed;

    private final int REQUEST_CHECK_SETTINGS = 555;

    private GoogleMap mMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        isOpening = true;
        isLocationSettingsDemandDisplayed = false;

        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult){
                if(locationResult == null){
                    return;
                }
                mLastKnownLocation = locationResult.getLastLocation();
                updateUI();
                Log.d("HELLO", "NON NULL");
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability){

                if(!locationAvailability.isLocationAvailable()) {

                    LocationSettingsRequest.Builder requestBuilder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

                    SettingsClient client = LocationServices.getSettingsClient(MapsActivity.this);
                    Task<LocationSettingsResponse> task = client.checkLocationSettings(requestBuilder.build());

                    task.addOnSuccessListener(MapsActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                        }
                    });

                    task.addOnFailureListener(MapsActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            setContentView(R.layout.activity_main);
                            isLocationSettingsDemandDisplayed = true;
                            Log.d("HELLO", "askkkkkkkkkkk1");
                        }
                    });

                }else{
                    if(isLocationSettingsDemandDisplayed){
                        isLocationSettingsDemandDisplayed = false;
                        setContentView(R.layout.activity_maps);
                    }
                }
            }
        };


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }


    private Boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

            return false;

        }else {
            createLocationRequest();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if(requestCode == LOCATION_REQUEST_CODE){

            //Request cancelled -> result array is empty
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("HELLO", "onRequestPermissionsResult_true");
                mLocationPermission = true;
                createLocationRequest();
            }else{
                //Explain why the app needs to access the location and re-ask for permission
                new AlertDialog.Builder(this)
                        .setTitle("Why the app needs your location")
                        .setMessage("The app need to know your location in order to allow you to create Demands and reply to others' Demands")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();

            }

        }
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder requestBuilder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(requestBuilder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // Location settings are satisfied
                Log.d("HELLO", "createLocationRequest_true");
                startLocationUpdates();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Show a layout to ask for location settings to be on and explain why this is total shittery

                if(e instanceof ResolvableApiException){
                    // Location settings are not satisfied, ask the user for it
                    try{
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MapsActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    }catch (IntentSender.SendIntentException sendEx){}
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Log.d("HELLO", "createLocationRequestAfterAskingViaDialog_true");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.d("HELLO", "MOTHERFUCKER");
                        startLocationUpdates();
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap){

        mMap = googleMap;

        mLocationPermission = checkLocationPermission();

    }

    private void startLocationUpdates(){
        try {
            if (mLocationPermission) {
                Log.d("HELLO", "OK PERMISSION");

                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

            } else {
                Log.d("HELLO", "NO PERMISSION");
                checkLocationPermission();

            }
        }catch(SecurityException e){}
    }


    private void updateUI(){

        Log.d("HELLO", "UPDATEUI");

        try {
            if (mLocationPermission) {
                mMap.clear();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                LatLng mLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                CircleOptions mCircleOptions = new CircleOptions()
                        .center(mLatLng)
                        .radius(4000)
                        .strokeWidth(0)
                        .fillColor(0x300000cf);
                mMap.clear();
                mMap.addCircle(mCircleOptions);
                if(isOpening) {
                    isOpening = false;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 12));
                }
            }
        }catch(SecurityException e){}
    }
}
