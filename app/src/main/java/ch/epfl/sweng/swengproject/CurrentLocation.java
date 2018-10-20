package ch.epfl.sweng.swengproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.core.util.Function;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import static ch.epfl.sweng.swengproject.MyApplication.LOGTAG;


public class CurrentLocation implements LocationServer, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_REQUEST_CODE = 99;

    private static final int REQUEST_CHECK_SETTINGS = 555;

    private LocationRequest mLocationRequest;

    private Location mLastKnownLocation;

    private boolean callerActivityReady = false;

    private boolean updatingLocation = false;

    private boolean alreadyAskingForLocation = false;

    private Context context;

    private Activity activity;

    private LocationSettingsRequest request;

    private Function<Void, Void> function;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                Log.d(LOGTAG, "return");
                return;
            }
            mLastKnownLocation = locationResult.getLastLocation();
            updatingLocation = true;

            if (function != null && callerActivityReady) {
                function.apply(null);
            }

            Log.d(LOGTAG, "Lat : " + mLastKnownLocation.getLatitude() + ", Lng : " + mLastKnownLocation.getLongitude());
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {

            if (!locationAvailability.isLocationAvailable()) {

                LocationSettingsRequest.Builder requestBuilder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

                SettingsClient client = LocationServices.getSettingsClient(activity);
                Task<LocationSettingsResponse> task = client.checkLocationSettings(requestBuilder.build());

                task.addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        controlLocationRequest();
                        Log.d(LOGTAG, "ask to enable location services");
                    }
                });

            }
        }
    };


    private Function<Void, Void> donePermission = new Function<Void, Void>() {
        @Override
        public Void apply(Void input) {
            permissionDialog.dismiss();
            permDialogUp = false;
            checkLocationPermission();
            return null;
        }
    };

    private Function<Void, Void> bringMeToManagement = new Function<Void, Void>() {
        @Override
        public Void apply(Void input) {
            activity.startActivity(new Intent()
                    .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.fromParts("package", "ch.epfl.sweng.swengproject", null)));
            return null;
        }
    };

    private AlertDialog permissionDialog;

    private boolean permDialogUp = false;

    public CurrentLocation(){
        createLocationRequest();
    }

    public void setCurrentLocationParameters(Context context, Activity activity) {
        this.setCurrentLocationParameters(context, activity, null);
    }

    public void setCurrentLocationParameters(Context context, Activity activity, Function<Void, Void> function) {
        this.context = Objects.requireNonNull(context);
        this.activity = Objects.requireNonNull(activity);
        this.function = function;

        if (updatingLocation) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        updatingLocation = false;

        checkLocationPermission();
    }

    @Override
    public void callerOnPause() {
        if (isPermissionGranted()) {
            Log.d(LOGTAG, "callerOnPause" + activity.getLocalClassName());
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void callerOnResume() {

        Log.d(LOGTAG, "callerOnResume" + activity.getLocalClassName());
        if (updatingLocation) {
            startLocationUpdates();
        }

        if (permDialogUp && isPermissionGranted()) {
            permissionDialog.dismiss();
            permDialogUp = false;
        }
        if(isPermissionGranted() && !alreadyAskingForLocation) {
            Log.d(LOGTAG, "controlLocationRequest call from onResume");
            controlLocationRequest();
        }
    }

    private AlertDialog getNewPermissionDialog() {
        return permissionDialog = MyApplication.showCustomAlert2Buttons("SwengProject needs your location to continue :(",
                "To allow SwengProject to TRACKK U please go to the settings and allow" +
                        " SwngProject to use your location",
                "Settings", "Done !",
                bringMeToManagement, donePermission, activity);
    }


    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    private void checkLocationPermission() {

        if (!isPermissionGranted()) {
            //Permission not granted

            //Should the app give an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {

                Log.d(LOGTAG, "explain the need for location");

                getNewPermissionDialog().show();
                permDialogUp = true;

            } else {

                //Just request permission, callback method is onRequestPermissionResult
                Log.d(LOGTAG, "Ask without explanation");
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }


        } else {

            //Permission has already benn granted
            Log.d(LOGTAG, "permissions granted");
            controlLocationRequest();
        }
    }

    public void callerActivityReady() {
        callerActivityReady = true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        Log.d(LOGTAG, "resquest permission result");

        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission was granted, go on
                    controlLocationRequest();
                } else {
                    Log.d(LOGTAG, "Permission denied, asking again");
                }
                return;
            }
        }

    }

    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        request = (new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)).build();
    }


    private void controlLocationRequest() {
        Log.d(LOGTAG, "Who is asking for location : " + activity.getLocalClassName());
        if(!alreadyAskingForLocation) {
            alreadyAskingForLocation = true;
            SettingsClient client = LocationServices.getSettingsClient(activity);
            Task<LocationSettingsResponse> task = client.checkLocationSettings(request);
            Log.d(LOGTAG, "Asking to enable location services");
            task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    // Location settings are satisfied

                    Log.d(LOGTAG, "createLocationRequest_true");
                    startLocationUpdates();
                }
            });

            task.addOnFailureListener(activity, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Show a layout to ask for location settings to be on and explain why this is total shittery

                    if (e instanceof ResolvableApiException) {
                        // Location settings are not satisfied, ask the user for it
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(activity,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                        }
                    }

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CurrentLocation.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Log.d(LOGTAG, "createLocationRequestAfterAskingViaDialog_true");
                        alreadyAskingForLocation = false;
                        startLocationUpdates();

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.d(LOGTAG, "USER REFUSED TO ENABLE LOCATION SERVICES");
                        alreadyAskingForLocation = false;
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    private void startLocationUpdates() {
        try {
            if (isPermissionGranted()) {
                Log.d(LOGTAG, "OK PERMISSION");

                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

            } else {
                Log.d(LOGTAG, "NO PERMISSION");
                checkLocationPermission();
            }
        } catch (SecurityException e) {
        }
    }

    @Override
    public LatLng getLastLocation() {
        Log.d(LOGTAG, "pos : " + mLastKnownLocation.getLatitude() + " " + mLastKnownLocation.getLongitude());
        return new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
    }


    @Override
    public boolean getLocationPermissionStatus() {
        return isPermissionGranted();
    }
/*
    public void injectionForTest(Location mockLocation){
        try {
            Log.d(LOGTAG, "injection");
            mFusedLocationProviderClient.setMockLocation(mockLocation);
            mFusedLocationProviderClient.setMockMode(true);
            mLocationRequest = new LocationRequest().
            startLocationUpdates();
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.d(LOGTAG, location.toString());
                }
            });
        }catch(SecurityException e){}
    }*/
}
