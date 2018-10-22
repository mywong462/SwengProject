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
import static ch.epfl.sweng.swengproject.DBTools.*;
import static ch.epfl.sweng.swengproject.Database.*;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.EnumSet;

import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class AddNeedActivity extends AppCompatActivity {

    // Get the cloud database and its reference
    private static final FirebaseFirestore needsDB = FirebaseFirestore.getInstance();
    private static CollectionReference needsRef = needsDB.collection("needs");

    //constant used for input checks
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

    // Variables to prevent a user creating multiple needs
    static final String LOGTAG_nn = "Tag_nn";
    private long ttl;

    // Variables to allow user to set his own location for his need
    private int REQUEST_LOCATION = 133;
    private Button chooseLocation_btn;
    private LatLng setLocation;
    private Double lat;
    private Double lng;

    private LocationServer currLoc;

    //All the categories in the array listCategory
    private ArrayList<Categories> listCategory = new ArrayList<>(EnumSet.allOf(Categories.class));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_need);

        // To set the location for the need
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




    //method used to write in the DB
    private void writeNewNeed( String descr, long ttl, LatLng pos, int nbPeopleNeeded) {
        if (canAddNewNeed(((MyApplication) this.getApplication()).getUser_need_ttl())) {
            Need newNeed = new Need(Database.getDBauth.getCurrentUser().getEmail(), descr, ttl, pos.latitude, pos.longitude, category, nbPeopleNeeded, "");
            //set_fcm_InstanceId(newNeed);
            final long ttlCopy = new Long(ttl);
            final int pplCopy = new Integer(nbPeopleNeeded);

            Database.saveNeed(newNeed).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        updateTtl(ttlCopy);
                        updatePpl(pplCopy);
                        Toast.makeText(AddNeedActivity.this, "Need Successfully added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddNeedActivity.this, "Error : Please verify your connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    /** To retrieve the current registration token of the client app
    public void set_fcm_InstanceId(final Need user_need) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d(LOGTAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        user_need.setToken(token);
                        Log.d(LOGTAG, "success getting new InstanceId: " + token);
                    }
                });
    }*/

    private void updateTtl(long ttl) {
        ((MyApplication) this.getApplication()).setUser_need_ttl(ttl);
    }

    private void updatePpl(int ppl) {
        ((MyApplication) this.getApplication()).setUser_need_ppl(ppl);
    }

    /** This method uses the global variable accross the application state user_need_ttl
     * Variable to keep track of the last need created by the user and allowing him to create only one
     * If (user_need_ttl-System.currentTimeMillis()) is negative, it means the need created by the user has expired
     * It is initialized at 0L, so the first time a need is created, the above will always evaluate to true
     * However, for this to be true, a user created need needs to be deleted when he closes the app
     * This makes sense for shortlived needs, especially since the user would not be notified
     * when another accepts its invitation if the app is closed
     */
    private boolean canAddNewNeed(long user_need_ttl) {
        ttl = user_need_ttl-System.currentTimeMillis();
        Log.d(LOGTAG_nn, "Time alive left = "+ttl);
        if (ttl > 0) {
            Toast.makeText(AddNeedActivity.this, "You can't add another need while you have one alive!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        currentLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // To set the location for the need
    private void bindChooseLocationButton(){
        chooseLocation_btn = findViewById(R.id.choose_loc_btn);
        chooseLocation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddNeedActivity.this, ChooseLocationActivity.class), REQUEST_LOCATION);
            }
        });
    }

    // Call back when ChooseLocationActivity is finished
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
