package ch.epfl.sweng.swengproject;

import com.google.android.gms.maps.model.LatLng;	//for localization
import com.google.firebase.firestore.GeoPoint;

public class Need {


    //fields

    private String emitter;
    private String description;
    private int timeToLive;
    private double latitude;
    private double longitude;

    public Need() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Need(String emitter, String descr, int ttl, double lat, double lon) {
        this.emitter = emitter;
        this.description = descr;
        this.timeToLive = ttl;
        this.latitude = lat;
        this.longitude = lon;
    }


    //getters for all fields

    public String getEmitter(){
        return this.emitter;
    }

    public String getDescription(){
        return this.description;
    }

    public int getTimeToLive(){
        return this.timeToLive;
    }

    public double getLat(){
        return this.latitude;
    }

    public double getLon() { return this.longitude;}



}