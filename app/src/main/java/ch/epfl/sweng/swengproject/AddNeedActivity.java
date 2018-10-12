package ch.epfl.sweng.swengproject;

import android.arch.core.util.Function;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class AddNeedActivity extends AppCompatActivity {

    //constant used for input checks

    protected static final int MIN_VALIDITY = 1;
    protected static final int MAX_VALIDITY = 30;
    protected static final int MIN_DESCR_L = 10;
    protected static final int MIN_NB_PEOPLE = 1;
    protected static final int MAX_NB_PEOPLE = 50;

    private static final int MILLS_IN_MINUTES = 60000; //there is 60000 miliseconds in 1 minute

    private Categories category = Categories.HELP;

    private Button create_btn;

    private CurrentLocation currLoc;

    //All the categories in the array listCategory
    private ArrayList<Categories> listCategory = new ArrayList<>(EnumSet.allOf(Categories.class));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_need);

        //For categories
        Spinner spin = findViewById(R.id.spinnerCategories);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = Categories.valueOf(listCategory.get(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return ;
            }
        });
        //Adapt the array for the spinner
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listCategory);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        currLoc = new CurrentLocation(this.getApplicationContext(), this);

        //Update text fields with local variables
        TextView validity = findViewById(R.id.need_validity);
        validity.setText("Validity (between "+MIN_VALIDITY+" and "+MAX_VALIDITY+") :");

        TextView description = findViewById(R.id.need_descr);
        description.setText("Description (at least "+MIN_DESCR_L+" characters) :");

        TextView nbPeopleNeeded = findViewById(R.id.need_nbPeople);
        nbPeopleNeeded.setText("Number of people needed (between "+MIN_NB_PEOPLE+" and "+MAX_NB_PEOPLE+") :");


        //configure what happens when the create button is clicked

        create_btn = findViewById(R.id.create_btn);

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //register data from UI
                EditText validity = findViewById(R.id.validity_txt);
                EditText description = findViewById(R.id.descr_txt);
                EditText nbPeopleNeeded = findViewById(R.id.nbPeople_txt);

                String descr = description.getText().toString();
                int valid = Integer.parseInt(validity.getText().toString());
                int nbPeople = Integer.parseInt(nbPeopleNeeded.getText().toString());


                //Perform checks

                if (valid < MIN_VALIDITY || valid > MAX_VALIDITY || description.length() < MIN_DESCR_L) {

                    Toast.makeText(AddNeedActivity.this, "Incorrect input. Validity must be between " + MIN_VALIDITY + " and " + MAX_VALIDITY + " and the description must be at least 10 characters long", Toast.LENGTH_LONG).show();


                }else if(nbPeople < MIN_NB_PEOPLE || nbPeople > MAX_NB_PEOPLE){

                    Toast.makeText(AddNeedActivity.this, "Incorrect input. The number of people needed must be between "+MIN_NB_PEOPLE+" and "+MAX_NB_PEOPLE, Toast.LENGTH_LONG).show();

                }else{  //try to do something for the concurrency bug


                    LatLng currPos = currLoc.getLastLocation();;

                    writeNewUser(Database.getDBauth.getCurrentUser().getEmail(),descr,(long)(valid*MILLS_IN_MINUTES) + System.currentTimeMillis() , currPos.latitude, currPos.longitude, category, nbPeople);

                    startActivity(new Intent(AddNeedActivity.this, MapsActivity.class));

                }
            }
        });

    }


    //method used to write in the DB

    private void writeNewUser(String emitter, String descr, long ttl, double lat, double lon, Categories category, int nbPeopleNeeded) {

        Need newNeed = new Need(emitter, descr, ttl, lat, lon, category, nbPeopleNeeded);
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
        currLoc.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        currLoc.onActivityResult(requestCode, resultCode, data);
    }
}