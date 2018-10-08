package ch.epfl.sweng.swengproject;

        import android.util.Log;

        import com.google.android.gms.maps.model.LatLng;	//for localization
        import com.google.firebase.firestore.GeoPoint;

public class Need {


    //fields
    private String emitter;
    private String description;
    private double longitude;
    private double latitude;
    private int timeToLive;


    public Need() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Need(String emitter, String descr, int ttl, double longitude, double latitude) {
        this.emitter = emitter;
        this.description = descr;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeToLive = ttl;
    }


    //Getters for all fields
    public String getEmitter(){
        return this.emitter;
    }
    public String getDescription(){
        return this.description;
    }
    public double getLongitude(){
        return this.longitude;
    }
    public double getLatitude(){
        return this.latitude;
    }
    public int getTimeToLive(){
        return this.timeToLive;
    }

    //Used to return directly a GeoPoint
    public GeoPoint getPos(){ return new GeoPoint(this.latitude, this.longitude); }

    //Setters, for .toObjects
    public void setEmitter(String emitter){
        this.emitter = emitter;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setTimeToLive(int timeToLive){
        this.timeToLive = timeToLive;
    }

}