package ch.epfl.sweng.swengproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static ch.epfl.sweng.swengproject.MainActivity.currentLocation;

import com.google.firebase.firestore.DocumentReference;



public class AddNeedActivity extends AppCompatActivity {

    //constant used for input checks

    protected static final int MIN_VALIDITY = 1;
    protected static final int MAX_VALIDITY = 30;
    protected static final int MIN_DESCR_L = 10;

    private static final int MILLS_IN_MINUTES = 60000; //there is 60000 miliseconds in 1 minute

    private Button create_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_need);

        currentLocation.setCurrentLocationParameters(this.getApplicationContext(), this);

        //Update text fields with local variables

        TextView validity = findViewById(R.id.need_validity);
        validity.setText("Validity (must be between "+MIN_VALIDITY+" and "+MAX_VALIDITY+") :");

        TextView description = findViewById(R.id.need_descr);
        description.setText("Description (at least "+MIN_DESCR_L+" characters) :");


        //configure what happens when the create button is clicked

        create_btn = findViewById(R.id.create_btn);

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //register data from UI
                EditText validity = findViewById(R.id.validity_txt);
                EditText description = findViewById(R.id.descr_txt);

                String descr= description.getText().toString();

                int valid = Integer.parseInt(validity.getText().toString());

                //Perform checks

                if(valid < MIN_VALIDITY || valid > MAX_VALIDITY || description.length() < MIN_DESCR_L){

                    Toast.makeText(AddNeedActivity.this,"Incorrect input. Validity must be between "+MIN_VALIDITY+" and "+MAX_VALIDITY+" and the description must be at least 10 characters long",Toast.LENGTH_LONG).show();


                }else{  //try to do something for the concurrency bug


                    LatLng currPos = currentLocation.getLastLocation();

                    writeNewUser(Database.getDBauth.getCurrentUser().getEmail(),descr,(long)(valid*MILLS_IN_MINUTES) + System.currentTimeMillis() , currPos.latitude, currPos.longitude);


                    finish();
                }
            }
        });

    }


    //method used to write in the DB

    private void writeNewUser(String emitter, String descr, long ttl, double lat, double lon) {

        Need newNeed = new Need(emitter, descr, ttl, lat, lon);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        currentLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        currentLocation.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause(){
        super.onPause();
        currentLocation.callerOnPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        currentLocation.callerOnResume();
    }
}