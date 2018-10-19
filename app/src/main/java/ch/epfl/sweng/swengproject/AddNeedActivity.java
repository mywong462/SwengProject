package ch.epfl.sweng.swengproject;

import android.content.Intent;
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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static ch.epfl.sweng.swengproject.MainActivity.LOGTAG;
import static ch.epfl.sweng.swengproject.MainActivity.currentLocation;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.EnumSet;

import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class AddNeedActivity extends AppCompatActivity {

    //constant used for input checks
    
    public static int REQUEST_LOCATION = 133;

    protected static final int MIN_VALIDITY = 1;
    protected static final int MAX_VALIDITY = 30;
    protected static final int MIN_DESCR_L = 10;
    protected static final int MIN_NB_PEOPLE = 1;
    protected static final int MAX_NB_PEOPLE = 50;


    private final String validityInterval = "between " + MIN_VALIDITY + " and " + MAX_VALIDITY;
    private final String peopleInterval = "between "+MIN_NB_PEOPLE+" and "+MAX_NB_PEOPLE;

    private static final int MILLS_IN_MINUTES = 60000; //there is 60000 miliseconds in 1 minute

    private Categories category = Categories.HELP;

    private Button create_btn;
    private Button chooseLocation_btn;
    private LatLng setLocation;
    private String setLocation_str;
    private Double lat;
    private Double lng;

    private LocationServer currLoc;

    //All the categories in the array listCategory
    private ArrayList<Categories> listCategory = new ArrayList<>(EnumSet.allOf(Categories.class));

    public AddNeedActivity(LocationServer locationServer){
        this.currLoc = locationServer;
    }
    public AddNeedActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_need);

        bindChooseLocationButton();

        //For categories
        Spinner spin = findViewById(R.id.spinnerCategories);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = Categories.valueOf(listCategory.get(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        //Adapt the array for the spinner
        listCategory.remove(Categories.ALL); //A need cannot choose 'ALL' as a category
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listCategory);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);


        LocationServer loc = (LocationServer) getIntent().getSerializableExtra("loc");

        Log.d(MainActivity.LOGTAG, "got the Serializable : " + (loc == null));
        if (loc != null) {
            currLoc = loc;
        } else {
            Log.d(MainActivity.LOGTAG, "Normal code section");
            currentLocation.setCurrentLocationParameters(this.getApplicationContext(), this);
            currLoc = currentLocation;
            Log.d(MainActivity.LOGTAG, "currloc is null ? : " + (currLoc == null));
        }
        //Update text fields with local variables
        TextView validity = findViewById(R.id.need_validity);
        validity.setText("Validity (" + validityInterval + ") :");

        TextView description = findViewById(R.id.need_descr);
        description.setText("Description (at least " + MIN_DESCR_L + " characters) :");

        TextView nbPeopleNeeded = findViewById(R.id.need_nbPeople);
        nbPeopleNeeded.setText("Number of people needed (" + peopleInterval + ") :");


        //configure what happens when the create button is clicked

        create_btn = findViewById(R.id.create_btn);

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //register data from UI
                EditText validity = findViewById(R.id.validity_txt);
                EditText description = findViewById(R.id.descr_txt);
                EditText nbPeopleNeeded = findViewById(R.id.nbPeople_txt);

                Log.d(MainActivity.LOGTAG, "VALUE IS : " + validity.getText() + " // null? " + validity.getText().length());

                if (validity.getText().length() == 0 || description.getText().length() == 0 || nbPeopleNeeded.getText().length() == 0) {
                    Log.d(MainActivity.LOGTAG, "At least one field is NULL");
                    Toast.makeText(AddNeedActivity.this, "Incorrect input. Don't let anything blank !", Toast.LENGTH_LONG).show();
                    return;
                }


                String descr = description.getText().toString();
                int valid = 0;
                int nbPeople = 0;
                try {
                    valid = Integer.parseInt(validity.getText().toString());
                    nbPeople = Integer.parseInt(nbPeopleNeeded.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(AddNeedActivity.this, "The validity and the number of people needed must be numbers", Toast.LENGTH_LONG).show();
                    return;
                }


                //Perform checks

                if (valid < MIN_VALIDITY || valid > MAX_VALIDITY || description.length() < MIN_DESCR_L) {

                    Toast.makeText(AddNeedActivity.this, "Incorrect input. Validity must be " + validityInterval + " and the description must be at least 10 characters long", Toast.LENGTH_LONG).show();


                } else if (nbPeople < MIN_NB_PEOPLE || nbPeople > MAX_NB_PEOPLE) {

                    Toast.makeText(AddNeedActivity.this, "Incorrect input. The number of people needed must be " + peopleInterval, Toast.LENGTH_LONG).show();

                } else {  //try to do something for the concurrency bug

                    LatLng currPos;
                    if (setLocation != null) {
                        Log.d("Tag_sl", "Setting user set location");
                        currPos = setLocation;
                    } else {
                        currPos = currLoc.getLastLocation();
                    }

                    Log.d(MainActivity.LOGTAG, "position is null " + (currPos == null));

                    writeNewNeed(descr, (long) (valid * MILLS_IN_MINUTES) + System.currentTimeMillis(), currPos, nbPeople);


                    finish();
                }
            }
        });
    }
        
        private void bindChooseLocationButton(){
        chooseLocation_btn = findViewById(R.id.choose_loc_btn);
        chooseLocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddNeedActivity.this, ChooseLocationActivity.class), REQUEST_LOCATION);
            }
        });
    }



    //method used to write in the DB

    private void writeNewNeed(String descr, long ttl, LatLng pos, int nbPeopleNeeded) {

        Need newNeed = new Need(Database.getDBauth.getCurrentUser().getEmail(), descr, ttl, pos.latitude, pos.longitude, category, nbPeopleNeeded,"");
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
         if (requestCode == REQUEST_LOCATION && data != null) {
            lat = data.getDoubleExtra("lat_code", 0.0);
            lng = data.getDoubleExtra("lng_code", 0.0);
            setLocation = new LatLng(lat, lng);
            if (resultCode == RESULT_OK) {
                Toast.makeText(AddNeedActivity.this, "Got the location!", Toast.LENGTH_SHORT).show();
            }
        } else {
            currentLocation.onActivityResult(requestCode, resultCode, data);
        }
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
