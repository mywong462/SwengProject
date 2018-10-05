package ch.epfl.sweng.swengproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;


public class AddNeedActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_need);



        //register data from UI



        writeNewUser("new@epfl.ch","Test pour savoir si l'objet est online", 60,7.0594, 47.1330);
    }

    //method used to write in the DB

    private void writeNewUser(String emitter, String descr, int ttl, double longitude, double latitude) {

        Need newNeed = new Need(emitter, descr, ttl, longitude, latitude);
        Database.saveNeed(newNeed).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){

                    Toast.makeText(AddNeedActivity.this,"Need Successfully added",Toast.LENGTH_SHORT).show();

                }
                else{

                    Toast.makeText(AddNeedActivity.this,"Error : Please verify your connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
