package ch.epfl.sweng.swengproject;

import android.arch.core.util.Function;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;





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
                if (isOpening) {
                    isOpening = false;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 12));
                }
            } else {
                Log.d("HELLO", "NO UPDATEUI");
            }
        } catch (SecurityException e) {
        }
    }

    private void showAvailableNeeds() {
        ArrayList<Need> availableNeeds = Database.getNeeds(mGeoPoint);

        Location here = new Location("");
        here.setLatitude(mGeoPoint.getLatitude());
        here.setLongitude(mGeoPoint.getLongitude());

        for (Need need : availableNeeds) {
            Location needLoc = new Location("");
            needLoc.setLatitude(need.getLatitude());
            needLoc.setLongitude(need.getLongitude());

            //If the need isn't in the desired range (range is in kilometer
            if (here.distanceTo(needLoc) > (float) range * 1000) {
                Log.d("THEONE", "MISSES : " + here.distanceTo(needLoc));
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



    @Override
    public boolean onMarkerClick(final Marker marker) {
        // TODO: decide what to do on marker click and see https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMap.OnMarkerClickListener.html#onMarkerClick(com.google.android.gms.maps.model.Marker) for behaviour
        Toast hehe = Toast.makeText(this, "doot doot", Toast.LENGTH_LONG);
        hehe.show();

        return true;
    }

}
