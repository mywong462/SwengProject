package ch.epfl.sweng.swengproject;


import android.Manifest;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.core.util.Function;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.Display;
import android.view.MotionEvent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener {

    private GeoPoint mGeoPoint;

    private LatLng lastLatLng;

    private Boolean isOpening;

    private GoogleMap mMap;

    private int range; //in kilometers

    private FloatingActionButton createNeed_btn;

    private CurrentLocation currentLocation = new CurrentLocation();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        isOpening = true;

        //TODO: get range in user settings
        range = 3000;

        //button with listener to create new needs
        createNeed_btn = findViewById(R.id.create_need_btn);
        createNeed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, AddNeedActivity.class));
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        currentLocation.setContextAndActivityAndMethodToCall(this.getApplicationContext(), this, new Function<Void, Void>() {
            @Override
            public Void apply(Void input) {
                updateUI();
                return null;
            }
        });
        currentLocation.checkLocationPermission();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        Log.d("HELLO","je passe");
        if(requestCode == CurrentLocation.LOCATION_REQUEST_CODE){

            //Request cancelled -> result array is empty
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("HELLO", "onRequestPermissionsResult_true");
                currentLocation.checkLocationPermission();
            }else{
                //Explain why the app needs to access the location and re-ask for permission
                new AlertDialog.Builder(this)
                        .setTitle("Why the app needs your location")
                        .setMessage("The app need to know your location in order to allow you to create Demands and reply to others' Demands")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CurrentLocation.LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();

            }

        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CurrentLocation.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Log.d("HELLO", "createLocationRequestAfterAskingViaDialog_true");
                        currentLocation.startLocationUpdates();

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.d("HELLO", "USER REFUSED TO ENABLE LOCATION SERVICES");
                        currentLocation.startLocationUpdates();
                        break;
                    default:
                        break;
                }
                break;
        }
    }



    private void updateUI(){

        Log.d("HELLO", "UPDATEUI");

        try {
            if (currentLocation.getLocationPermissionStatus()) {
                mMap.clear();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                lastLatLng = currentLocation.getLastLocation();

                mGeoPoint = new GeoPoint(lastLatLng.latitude, lastLatLng.longitude);
                CircleOptions mCircleOptions = new CircleOptions()
                        .center(lastLatLng)
                        .radius(range)
                        .strokeWidth(0)
                        .fillColor(0x300000cf);
                mMap.clear();
                mMap.addCircle(mCircleOptions);
                showAvailableNeeds();
                if(isOpening) {
                    isOpening = false;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 12));
                }
            }else{
                Log.d("HELLO", "NO UPDATEUI");
            }
        }catch(SecurityException e){}
    }

    private void showAvailableNeeds(){
        ArrayList<Need> availableNeeds = Database.getNeeds(mGeoPoint,range);

        Location here = new Location("");
        here.setLatitude(mGeoPoint.getLatitude());
        here.setLongitude(mGeoPoint.getLongitude());

        for(Need need : availableNeeds){
            Location needLoc = new Location("");
            needLoc.setLatitude(need.getLatitude());
            needLoc.setLongitude(need.getLongitude());

            //If the need isn't in the desired range (range is in kilometer
            if(here.distanceTo(needLoc) > (float)range*1000){
                //Log.d("MISSES", "ONE");
                continue;
            }

            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(need.getLatitude(), need.getLongitude()))
                                    .title("TITLE"));
            // TODO: change color depending on the type of need
            marker.setTag(need);
        }

        mMap.setOnMarkerClickListener(this);
    }

    private void displayOnMenu(View menuView, GeoPoint tempGeo){
        //  TODO: need to update this function when more fields from the needs are available
        //The field to be update
        TextView description = menuView.findViewById(R.id.needDescription);
        Need selectedNeed = null;


        //Searching for the need
        ArrayList<Need> currentNeed = Database.getNeeds(tempGeo,range);
        for (int i = 0; i < currentNeed.size(); i++){

            if ((currentNeed.get(i).getLongitude() == tempGeo.getLongitude()) && (currentNeed.get(i).getLatitude() == tempGeo.getLatitude())){
                selectedNeed = currentNeed.get(i);
                break;
            }
        }

        //Updating information on the screen
        if (selectedNeed != null){
            description.setText(selectedNeed.getDescription());
        }
        else  Log.d("ERROR", "System cannot find Need matched with the GeoPoint");

    }



    @Override
    public boolean onMarkerClick(final Marker marker){
        // TODO: decide what to do on marker click and see https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.OnMarkerClickListener.html#onMarkerClick(com.google.android.gms.maps.model.Marker) for behaviour
        //Get the size of screen and pop up a window
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        LayoutInflater inflater = (LayoutInflater) MapsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_pin_popup_window,null);
        final PopupWindow pw = new PopupWindow(layout, (int)(width*0.8), (int)(height*0.7), true);

        //Get the marker information
        GeoPoint needRequest = new GeoPoint(marker.getPosition().latitude,marker.getPosition().longitude);
        displayOnMenu(layout,needRequest);

        //Implemnent the close button
        ((Button) layout.findViewById(R.id.declineBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pw.dismiss();
            }
        });

        //Clicking outside the window will close the window
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });
        pw.setOutsideTouchable(true);

        //Display the pop-up window
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);


        return true;
    }

}
