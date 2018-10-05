package ch.epfl.sweng.swengproject;


import com.google.android.gms.maps.model.LatLng;	//for localization

public class Need {


    //fields

    private String emitter;
    private String description;
    private int timeToLive;
    private LatLng position;

    public Need() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Need(String emitter, String descr, int ttl, LatLng pos) {
        this.emitter = emitter;
        this.description = descr;
        this.timeToLive = ttl;
        this.position = pos;
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

    public LatLng getPos(){
        return this.position;
    }



}
