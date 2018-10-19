package ch.epfl.sweng.swengproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AddNeedUnitTest {


    @Test
    public void onActResTest(){

        AddNeedActivity act = new AddNeedActivity(new LocationServer() {
            @Override
            public LatLng getLastLocation() {
                return new LatLng(12,13);
            }

            @Override
            public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

            }

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {

            }

            @Override
            public boolean getLocationPermissionStatus() {
                return true;
            }
        });

        Intent mockData = mock(Intent.class);

        when(mockData.getDoubleExtra("lat_code", 0.0)).thenReturn(1.1);
        when(mockData.getDoubleExtra("lng_code", 0.0)).thenReturn(1.2);


        act.onActivityResult(AddNeedActivity.REQUEST_LOCATION, Activity.RESULT_CANCELED,mockData);
        act.onActivityResult(AddNeedActivity.REQUEST_LOCATION, Activity.RESULT_CANCELED,null);

    }


    @Test
    public void canCallFunctions(){

       AddNeedActivity act = mock(AddNeedActivity.class);

       Bundle b = mock(Bundle.class);

       act.onCreate(b);

       act.onRequestPermissionsResult(0,new String[0],new int[0]);
       act.onResume();
       act.onPause();
    }

}
