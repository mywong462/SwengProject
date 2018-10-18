package ch.epfl.sweng.swengproject.util;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import ch.epfl.sweng.swengproject.LocationServer;

public class FakeLocation implements LocationServer {


    @Override
    public LatLng getLastLocation() {
        return new LatLng(23,56);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public boolean getLocationPermissionStatus(){
        return true;
    }

    @Override
    public void callerOnPause(){}

    @Override
    public void callerOnResume(){}

}
