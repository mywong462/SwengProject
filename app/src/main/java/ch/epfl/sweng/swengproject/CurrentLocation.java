package ch.epfl.sweng.swengproject;

import android.Manifest;
import android.app.Activity;
import android.arch.core.util.Function;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;


class CurrentLocation extends FragmentActivity{

    protected static final int LOCATION_REQUEST_CODE = 99;

    protected static final int REQUEST_CHECK_SETTINGS = 555;

    protected boolean mLocationPermission;

    private LocationRequest mLocationRequest;

    private Location mLastKnownLocation;

    private Boolean isLocationSettingsDemandDisplayed = false;

    private Context ctx;

    private Activity activity;

    private Function<Void, Void> function;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private boolean enableLocationCallback;

    private LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult){
            if(locationResult == null){
                return;
            }
            mLastKnownLocation = locationResult.getLastLocation();
            function.apply(null);

            Log.d("HELLO", "Lat : " + mLastKnownLocation.getLatitude() + ", Lng : " + mLastKnownLocation.getLongitude());
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
                        Log.d("HELLO", "ask to enable location services");
                    }
                });

            }else{
                if(isLocationSettingsDemandDisplayed){
                    isLocationSettingsDemandDisplayed = false;
                    Log.d("HELLO", "location services re-enabled");
                    activity.setContentView(R.layout.activity_maps);
                }
            }
        }
    };


    public CurrentLocation(Context context, Activity activity){
        this.ctx = Objects.requireNonNull(context);
        this.activity = Objects.requireNonNull(activity);
        enableLocationCallback = false;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        checkLocationPermission();
    }


    /**
     * Constructor for CurrentLocation with callBack
     */
    public CurrentLocation(Context context, Activity activity, Function<Void, Void> function){
        this.ctx = Objects.requireNonNull(context);
        this.activity = Objects.requireNonNull(activity);
        this.function = Objects.requireNonNull(function);
        enableLocationCallback = true;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        checkLocationPermission();
    }


    protected void checkLocationPermission(){

        if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);


        }else {
            Log.d("HELLO", "CAJOUE");
            mLocationPermission = true;
            createLocationRequest();
        }
    }





    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder requestBuilder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(requestBuilder.build());

        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // Location settings are satisfied
                Log.d("HELLO", "createLocationRequest_true");
                if(enableLocationCallback) {
                    startLocationUpdates();
                }
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



    protected void startLocationUpdates(){
        try {
            if (mLocationPermission) {
                Log.d("HELLO", "OK PERMISSION");

                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);


            } else {
                Log.d("HELLO", "NO PERMISSION");
                checkLocationPermission();

            }
        }catch(SecurityException e){}
    }


    public LatLng getLastLocation() throws SecurityException{


        return new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

    }


    public boolean getLocationPermissionStatus(){
        return mLocationPermission;
    }

}