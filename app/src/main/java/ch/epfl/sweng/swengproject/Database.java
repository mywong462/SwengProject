package ch.epfl.sweng.swengproject;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public final class Database {

    protected static List<Need> listNeeds = new ArrayList<>();

    protected static final FirebaseAuth getDBauth = FirebaseAuth.getInstance();

    private static final FirebaseFirestore needsDB = FirebaseFirestore.getInstance();

    private static final CollectionReference needsRef = needsDB.collection("needs");

    public static Task<DocumentReference> saveNeed(Need need){
        return needsRef.add(need);
    }

    public static ArrayList<Need> getNeeds(GeoPoint myLocation){

        needsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null) {
                    Log.d("HELLO", "Got an exception querying the database");
                }else{
                    listNeeds = queryDocumentSnapshots.toObjects(Need.class);
                }
            }
        });

        return new ArrayList(listNeeds);
    }


}
