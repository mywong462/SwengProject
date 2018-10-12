package ch.epfl.sweng.swengproject;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

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

}
