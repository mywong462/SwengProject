package ch.epfl.sweng.swengproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.core.util.Function;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;


public class CurrentLocation extends FragmentActivity implements LocationServer{

    protected static final int LOCATION_REQUEST_CODE = 99;

    protected static final int REQUEST_CHECK_SETTINGS = 555;

    private LocationRequest mLocationRequest;

    private Location mLastKnownLocation;

    private boolean isLocationSettingsDemandDisplayed = false;

    private boolean callerActivityReady = false;

    private boolean updatingLocation = false;

    private Context ctx;

    private Activity activity;

    private Function<Void, Void> function;

    private FusedLocationProviderClient mFusedLocationProviderClient;


    private LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult){
            if(locationResult == null){
                return;
            }
            mLastKnownLocation = locationResult.getLastLocation();
            updatingLocation = true;

            if(function != null && callerActivityReady) {
                function.apply(null);
            }

            Log.d(MainActivity.LOGTAG, "Lat : " + mLastKnownLocation.getLatitude() + ", Lng : " + mLastKnownLocation.getLongitude());
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability){

            if(!locationAvailability.isLocationAvailable()) {

                LocationSettingsRequest.Builder requestBuilder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

                SettingsClient client = LocationServices.getSettingsClient(activity);
                Task<LocationSettingsResponse> task = client.checkLocationSettings(requestBuilder.build());

                task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                    }
                });

                task.addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        activity.setContentView(R.layout.page_location_services_up_demand);
                        isLocationSettingsDemandDisplayed = true;

                        Log.d(MainActivity.LOGTAG, "ask to enable location services");

                    }
                });

            }else{
                if(isLocationSettingsDemandDisplayed){
                    isLocationSettingsDemandDisplayed = false;

                    Log.d(MainActivity.LOGTAG, "location services re-enabled");

                    activity.setContentView(R.layout.activity_maps);
                }
            }
        }
    };


    public void setCurrentLocationParameters(Context context, Activity activity){
        this.setCurrentLocationParameters(context, activity, null);
    }

    public void setCurrentLocationParameters(Context context, Activity activity, Function<Void, Void> function){
        this.ctx = Objects.requireNonNull(context);
        this.activity = Objects.requireNonNull(activity);
        this.function = function;

        if(updatingLocation) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        checkLocationPermission();
    }

    protected void callerOnPause(){
        Log.d(MainActivity.LOGTAG, "callerOnPause" + activity.getLocalClassName());
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    protected void callerOnResume(){
        Log.d(MainActivity.LOGTAG, "callerOnResume" + activity.getLocalClassName());
        if(updatingLocation) {
            startLocationUpdates();
        }
    }

    protected boolean isPermissionGranted(){
        return ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }


    protected void checkLocationPermission(){

        if(!isPermissionGranted()){

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);


        }else {
            Log.d(MainActivity.LOGTAG, "permissions granted");
            createLocationRequest();
        }
    }

    protected void callerActivityReady(){
        callerActivityReady = true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        Log.d(MainActivity.LOGTAG, "resquest permission result");

        if (requestCode == CurrentLocation.LOCATION_REQUEST_CODE) {

            //Request cancelled -> result array is empty
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.d(MainActivity.LOGTAG, "onRequestPermissionsResult_true");
                checkLocationPermission();
            } else {
                //Explain why the app needs to access the location and re-ask for permission
                new AlertDialog.Builder(this)
                        .setTitle("Why the app needs your location")
                        .setMessage("The app need to know your location in order to allow you to create Demands and reply to others' Demands")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CurrentLocation.LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();

            }

        }
    }



    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder requestBuilder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(requestBuilder.build());

        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // Location settings are satisfied

                Log.d(MainActivity.LOGTAG, "createLocationRequest_true");
                startLocationUpdates();
            }
        });

        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Show a layout to ask for location settings to be on and explain why this is total shittery

                if(e instanceof ResolvableApiException){
                    // Location settings are not satisfied, ask the user for it
                    try{
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(activity,
                                REQUEST_CHECK_SETTINGS);
                    }catch (IntentSender.SendIntentException sendEx){}
                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CurrentLocation.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Log.d(MainActivity.LOGTAG, "createLocationRequestAfterAskingViaDialog_true");
                        startLocationUpdates();

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.d(MainActivity.LOGTAG, "USER REFUSED TO ENABLE LOCATION SERVICES");

                        startLocationUpdates();
                        break;
                    default:
                        break;
                }
                break;
        }
    }



    protected void startLocationUpdates(){
        try {
            if (isPermissionGranted()) {
                Log.d(MainActivity.LOGTAG, "OK PERMISSION");

                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);


            } else {
                Log.d(MainActivity.LOGTAG, "NO PERMISSION");

                checkLocationPermission();

            }
        }catch(SecurityException e){}
    }


    public LatLng getLastLocation(){
        Log.d(MainActivity.LOGTAG, "pos : " + mLastKnownLocation.getLatitude() + " " + mLastKnownLocation.getLongitude());
        return new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
    }



    public boolean getLocationPermissionStatus(){
        return isPermissionGranted();
    }

}
