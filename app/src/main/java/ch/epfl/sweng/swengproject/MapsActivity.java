package ch.epfl.sweng.swengproject;

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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.EnumSet;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener {

    private GeoPoint mGeoPoint;

    private LatLng lastLatLng;

    private Boolean isOpening;

    private GoogleMap mMap;

    private int range; //in kilometers

    private FloatingActionButton createNeed_btn;

    private CurrentLocation currentLocation;


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

        Function<Void, Void> function = new Function<Void, Void>() {
            @Override
            public Void apply(Void input) {
                updateUI();
                return null;
            }
        };

        currentLocation = new CurrentLocation(this.getApplicationContext(),
                this,
                function);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        currentLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        currentLocation.onActivityResult(requestCode, resultCode, data);
    }


    private void updateUI() {

        Log.d("DEBUG", "UPDATEUI");

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
                if (isOpening) {
                    isOpening = false;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 12));
                }
            } else {
                Log.d("ERROR", "NO PERMISSION TO UPDATEUI");
            }
        } catch (SecurityException e) {
        }
    }


    private void showAvailableNeeds() {

        ArrayList<Need> availableNeeds = Database.getNeeds(mGeoPoint, range, null);

        for (Need need : availableNeeds) {
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
        ArrayList<Need> currentNeed = Database.getNeeds(tempGeo,range, null);
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
    public boolean onMarkerClick(final Marker marker) {
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
            public boolean onTouch(View view, MotionEvent event) {
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
