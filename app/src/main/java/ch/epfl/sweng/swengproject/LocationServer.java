package ch.epfl.sweng.swengproject;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;


public interface LocationServer extends Serializable{

    LatLng getLastLocation();

    void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    boolean getLocationPermissionStatus();
}
