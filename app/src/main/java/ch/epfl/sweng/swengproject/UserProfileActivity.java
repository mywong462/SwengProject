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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userName = findViewById(R.id.userName1);
        firstName = findViewById(R.id.firstName1);
        lastName = findViewById(R.id.lastName1);
        btnSave = findViewById(R.id.save_btn1);

        final String fn = firstName.getText().toString();
        final String ln = lastName.getText().toString();
        final String un = userName.getText().toString();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create instance of SharedPreferences
                SharedPreferences up = getSharedPreferences(USER_PROFILE, 0);

                // Get the editor
                SharedPreferences.Editor edit = up.edit();
                edit.putString("userName", un);
                edit.putString("firstName", fn);
                edit.putString("lastName", ln);

                // Save the Editor value
                edit.commit();
                Toast.makeText(getApplicationContext(), "Saved changes to user profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserProfileActivity.this, MapsActivity.class));
                finish();

            }
        });




    }
}
