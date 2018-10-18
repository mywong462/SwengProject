package ch.epfl.sweng.swengproject;

import android.app.Activity;
import android.arch.core.util.Function;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;


public interface LocationServer extends Serializable{

    LatLng getLastLocation();

    void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    boolean getLocationPermissionStatus();

    void callerOnPause();

    void callerOnResume();

    void setCurrentLocationParameters(Context context, Activity activity, Function<Void, Void> function);

    void setCurrentLocationParameters(Context context, Activity activity);

    void callerActivityReady();
}
