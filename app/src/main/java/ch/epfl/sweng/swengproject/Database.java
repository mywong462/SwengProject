package ch.epfl.sweng.swengproject;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


public final class Database {

    protected static final FirebaseAuth getDBauth = FirebaseAuth.getInstance();

    private static final FirebaseFirestore needsDB = FirebaseFirestore.getInstance();


    private static final CollectionReference needsRef = needsDB.collection("needs");

    public static Task<DocumentReference> saveNeed(Need need){
        return needsRef.add(need);
    }

    public static ArrayList<Need> getNeeds(LatLng myLocation){
        //Query query = needsRef.whereLessThanOrEqualTo()

    }

}
