package ch.epfl.sweng.swengproject;

        import android.util.Log;

        import com.google.android.gms.maps.model.LatLng;	//for localization
        import com.google.firebase.firestore.GeoPoint;

        import java.util.ArrayList;

public class Need {


    //fields
    private String emitter;
    private String description;
    private double longitude;
    private double latitude;
    private long timeToLive;
    private Categories category;
    private int nbPeopleNeeded;
    private ArrayList<String> participants;

    public Need() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Need(String emitter, String descr, long ttl, double latitude, double longitude, Categories category, int nbPeopleNeeded, ArrayList<String> part) {
        this.emitter = emitter;
        this.description = descr;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeToLive = ttl;
        this.category = category;
        this.nbPeopleNeeded = nbPeopleNeeded;
        this.participants = part;
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
    public long getTimeToLive(){
        return this.timeToLive;
    }
    public Categories getCategory() { return this.category; }
    public int getNbPeopleNeeded() { return this.nbPeopleNeeded; }
    public ArrayList<String> getParticipants() {return this.participants; }

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
    public void setLatitude(double latitude){ this.latitude = latitude; }
    public void setTimeToLive(long timeToLive){ this.timeToLive = timeToLive; }
    public void setCategory(Categories category) { this.category = category; }
    public void setNbPeopleNeeded(int nbPeopleNeeded) { this.nbPeopleNeeded = nbPeopleNeeded; }
    public void setParticipants(ArrayList<String> participants){this.participants = participants;}


}