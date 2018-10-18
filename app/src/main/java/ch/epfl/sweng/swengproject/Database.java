package ch.epfl.sweng.swengproject;

import android.util.Log;

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

    private static final CollectionReference needsRef = needsDB.collection("needs");

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

        return DBTools.filterNeeds(mGeoPoint,range,categories, listNeeds);
    }


}
