package ch.epfl.sweng.swengproject;

import android.arch.core.util.Function;
import android.content.Context;
import android.content.Intent;

import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.PopupWindow;
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

import static ch.epfl.sweng.swengproject.Database.currentLocation;
import static ch.epfl.sweng.swengproject.MyApplication.LOGTAG;


import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener{

    private GeoPoint mGeoPoint;

    private LatLng lastLatLng;

    private Boolean isOpening;

    private GoogleMap mMap;

    private LocationServer currLoc;

    private int range; //in kilometers

    private FloatingActionButton createNeed_btn;

    private static final String KEY_LOCATION = "location";

    private  ArrayList<Need> availableNeeds = null;

    private boolean normalExec = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOGTAG, "onCreate");
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_maps);
        isOpening = true;
        //TODO: get range in user settings
        range = 3000;

        LocationServer loc = (LocationServer) getIntent().getSerializableExtra("loc");
        if(loc != null){
            this.normalExec = false;
            currLoc = loc;
            ArrayList<Need> needList = new ArrayList<>();
            long ttl = System.currentTimeMillis() + 100000;
            needList.add(new Need("hedi.sassi@epfl.ch", "my description", ttl, currLoc.getLastLocation().latitude, currLoc.getLastLocation().longitude,Categories.ALL ,1 ));
            availableNeeds = needList;
        }
        else {
            this.normalExec = true;
            Log.d(LOGTAG,"Normal code section");

            currLoc = currentLocation;
            launchCurrentLocation();
        }

        bindAddNeedButton();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }

    private void bindAddNeedButton(){
        //button with listener to create new needs
        createNeed_btn = findViewById(R.id.create_need_btn);
        createNeed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, AddNeedActivity.class));
            }
        });
    }

    private void launchCurrentLocation(){
        Function<Void, Void> function = new Function<Void, Void>() {
            @Override
            public Void apply(Void input) {
                updateUI();
                return null;
            }
        };

        currentLocation.setCurrentLocationParameters(this.getApplicationContext(), this, function);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            Log.d(LOGTAG, "saving instance of map");
            outState.putParcelable(KEY_LOCATION, lastLatLng);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d(LOGTAG, "getting old instance");
            lastLatLng = savedInstanceState.getParcelable(KEY_LOCATION);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        if (lastLatLng != null) {
            updateUI();
        }

        currentLocation.callerActivityReady();
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentLocation.callerOnPause();
    }

    @Override
    protected void onResume() {
        //Log.d(MainActivity.LOGTAG, "onResume before super");
        super.onResume();
        //Log.d(MainActivity.LOGTAG, "onResume after super");
        if(!isOpening) {
            launchCurrentLocation();
        }
        currentLocation.callerOnResume();
        //Log.d(MainActivity.LOGTAG, "onResume before super");
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

        Log.d(LOGTAG, "UPDATEUI");

        try {
            if (currLoc.getLocationPermissionStatus()) {
                mMap.clear();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                lastLatLng = currLoc.getLastLocation();

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

        //arrayCategories while the user choosing them is not implemented
        ArrayList<Categories> arrayCategories = new ArrayList<>();
        arrayCategories.add(Categories.ALL);
        if(this.normalExec) {
            Log.d("DEBUG","normal code");
            this.availableNeeds = Database.getNeeds(mGeoPoint, range, arrayCategories);

        }
        for (Need need : availableNeeds) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(need.getLatitude(), need.getLongitude()))
                    .title("TITLE"));
            // TODO: change color depending on the type of need
            marker.setTag(need);
        }

         mMap.setOnMarkerClickListener(this);
    }

    private void displayOnMenu(View menuView, GeoPoint tempGeo) {
        //  TODO: need to update this function when more fields from the needs are available
        //The field to be update
        TextView description = menuView.findViewById(R.id.needDescription);

        Need selectedNeed = null;

        //arrayCategories while the user choosing them is not implemented
        ArrayList<Categories> arrayCategories = new ArrayList<>();
        arrayCategories.add(Categories.ALL);


        //Searching for the need

        ArrayList<Need> currentNeed = Database.getNeeds(tempGeo, range, arrayCategories);
        for (int i = 0; i < currentNeed.size(); i++){
            if ((currentNeed.get(i).getLongitude() == tempGeo.getLongitude()) && (currentNeed.get(i).getLatitude() == tempGeo.getLatitude())){
                selectedNeed = currentNeed.get(i);
                break;
            }
        }

        //Updating information on the screen
        if (selectedNeed != null) {
            description.setText(selectedNeed.getDescription());
        } else Log.d("ERROR", "System cannot find Need matched with the GeoPoint");

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
        View layout = inflater.inflate(R.layout.activity_pin_popup_window, null);
        final PopupWindow pw = new PopupWindow(layout, (int) (width * 0.8), (int) (height * 0.7), true);

        //Get the marker information
        GeoPoint needRequest = new GeoPoint(marker.getPosition().latitude, marker.getPosition().longitude);
        displayOnMenu(layout, needRequest);

        //Implemnent the close button
        layout.findViewById(R.id.declineBtn).setOnClickListener(new View.OnClickListener() {
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
