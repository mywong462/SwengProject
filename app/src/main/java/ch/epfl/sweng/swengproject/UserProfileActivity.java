package ch.epfl.sweng.swengproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserProfileActivity extends AppCompatActivity {

    public static final String USER_PROFILE = "UserProfile";
    private EditText firstName, lastName, userName;
    private Button btnSave;
    private String fn, ln, un;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //link private variables with UI elements
        getUIElements();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFieldsContent();

                if(checkInput(fn,ln,un)){
                    // Create instance of SharedPreferences
                    SharedPreferences up = getSharedPreferences(USER_PROFILE, 0);

                    // Get the editor
                    SharedPreferences.Editor edit = createAndSetEditor(up, fn, un, ln);

                    // Save the Editor value
                    edit.commit();
                    Toast.makeText(getApplicationContext(), "Saved changes to user profile", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserProfileActivity.this, MapsActivity.class));
                    finish();
                }
            }
        });

    }

    private SharedPreferences.Editor createAndSetEditor(SharedPreferences up, String fn, String un, String ln){
        SharedPreferences.Editor edit = up.edit();
        edit.putString("userName", un);
        edit.putString("firstName", fn);
        edit.putString("lastName", ln);

        return edit;
    }

    private void getUIElements(){
        userName = findViewById(R.id.userName1);
        firstName = findViewById(R.id.firstName1);
        lastName = findViewById(R.id.lastName1);
        btnSave = findViewById(R.id.save_btn1);
    }

    private boolean checkInput(String fn, String ln, String un){

        if(fn.isEmpty() || ln.isEmpty() || un.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    private void getFieldsContent(){
        fn = firstName.getText().toString();
        ln = lastName.getText().toString();
        un = userName.getText().toString();
    }
}
