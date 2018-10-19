package ch.epfl.sweng.swengproject;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import static ch.epfl.sweng.swengproject.MyApplication.LOGTAG;


public final class Database {

    protected static List<Need> listNeeds = new ArrayList<>();

    protected static final FirebaseAuth getDBauth = FirebaseAuth.getInstance();

    private static final FirebaseFirestore needsDB = FirebaseFirestore.getInstance();

    private static CollectionReference needsRef = needsDB.collection("needs");

    public static Task<DocumentReference> saveNeed(Need need){
        return needsRef.add(need);
    }


    //If there is no limitation in category, pass null into categories variable
    public static ArrayList<Need> getNeeds(GeoPoint mGeoPoint, int range, ArrayList<Categories> categories){

        DBTools.checkInput(mGeoPoint, range, categories);

        needsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null) {
                    Log.d(LOGTAG, "Got an exception querying the database");

                }else{

                    ArrayList<Need> temp = new ArrayList<>();

                    Log.d(LOGTAG, "Fetch from DB");
                    //get the needs from the database


                    for(DocumentSnapshot dS: queryDocumentSnapshots.getDocuments()){
                        Need current = new Need();

                        current.setLongitude((double)dS.get("longitude"));
                        current.setLatitude((double)dS.get("latitude"));
                        current.setEmitter((String)dS.get("emitter"));
                        current.setDescription((String)dS.get("description"));

                        current.setCategory(DBTools.convertStringToCat((String)dS.get("category")));

                        current.setTimeToLive((long)dS.get("timeToLive"));
                        int people = Integer.parseInt(dS.get("nbPeopleNeeded").toString());
                        current.setNbPeopleNeeded(people);
                        current.setParticipants(dS.get("participants").toString());

                        temp.add(current);
                    }

                    //remove old needs from the database

                    for(DocumentSnapshot old:queryDocumentSnapshots.getDocuments()){
                        if((long)old.get("timeToLive") < System.currentTimeMillis()){
                            old.getReference().delete();
                        }
                    }

                    listNeeds = temp;
                }
            }
        });

        return DBTools.filterNeeds(mGeoPoint,range,categories, listNeeds);
    }

    public static void addParticipant(LatLng pos){

        final LatLng position = pos;

        needsRef.get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()) {
                            for (DocumentSnapshot need : task.getResult().getDocuments()) {
                                if ((double) need.get("latitude") == position.latitude && (double) need.get("longitude") == position.longitude) {

                                    if (DBTools.computeNumber(need.get("participants").toString()) == 0) {
                                        need.getReference().update("participants", getDBauth.getCurrentUser().getEmail());
                                    } else {
                                        need.getReference().update("participants", need.get("participants") + "," + getDBauth.getCurrentUser().getEmail());
                                    }

                                    return;
                                }
                            }
                        }
                        else{
                            Log.d(LOGTAG, "Got an exception querying the database");
                        }
                    }
                }
        );
    }
}
