package ch.epfl.sweng.swengproject;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public final class Database {

    protected static List<Need> listNeeds = new ArrayList<>();

    public static final FirebaseAuth getDBauth = FirebaseAuth.getInstance();

    private static final FirebaseFirestore needsDB = FirebaseFirestore.getInstance();

    private static final CollectionReference needsRef = needsDB.collection("needs");

    public static Task<DocumentReference> saveNeed(Need need){
        return needsRef.add(need);
    }

    //If there is no limitation in category, pass null into categories variable
    public static ArrayList<Need> getNeeds(GeoPoint mGeoPoint, int range, Categories categories){

        ArrayList<Need> availableNeeds = new ArrayList<>();

        needsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null) {
                    Log.d("Debug", "Got an exception querying the database");
                }else{

                    //get the needs from the database

                    listNeeds = queryDocumentSnapshots.toObjects(Need.class);

                    //remove old needs from the database

                    for(DocumentSnapshot old:queryDocumentSnapshots.getDocuments()){
                        if(old.toObject(Need.class).getTimeToLive() < System.currentTimeMillis()){
                            old.getReference().delete();
                        }
                    }

                }
            }
        });

        //To remove the needs that aren't in the range
        Location here = createAndSetLoc(mGeoPoint.getLatitude(),mGeoPoint.getLongitude());

        for (Need need : listNeeds) {
            Location needLoc = createAndSetLoc(need.getLatitude(),need.getLongitude());

            //If the need isn't in the desired range (range is in kilometer) and the need isn't outdated
            if (here.distanceTo(needLoc) <= (float) range * 1000 && need.getTimeToLive() > System.currentTimeMillis()) {
                if ((categories == need.getCategory())||(categories == null)){
                    availableNeeds.add(need);
                }
            }


        }

        return availableNeeds;
    }

    private static Location createAndSetLoc(double lat, double lon){

        Location loc = new Location("");
        loc.setLatitude(lat);
        loc.setLongitude(lon);

        return loc;
    }

}
