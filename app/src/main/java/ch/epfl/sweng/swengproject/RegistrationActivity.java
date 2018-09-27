package ch.epfl.sweng.swengproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.firebase.auth.FirebaseAuth;


public class RegistrationActivity extends AppCompatActivity {


    private EditText inputEmail, inputPassword;
    private Button btnRegister;
    private FirebaseAuth auth;  //firebase instance


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        inputEmail = findViewById(R.id.email);      //get the two texts
        inputPassword = findViewById(R.id.password);

        btnRegister = findViewById(R.id.button);    //get the button

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();         //recover the informations from the text fields
                String password = inputPassword.getText().toString();

                //check entered informations...

               // auth = FirebaseAuth.getInstance();
               // auth.createUserWithEmailAndPassword(email, password);

            }
        });



    }



}
