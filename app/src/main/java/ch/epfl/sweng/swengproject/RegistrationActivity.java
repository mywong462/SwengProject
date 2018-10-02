package ch.epfl.sweng.swengproject;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegistrationActivity extends AppCompatActivity {


    private EditText inputEmail, inputPassword;
    private Button btnRegister;
    private FirebaseAuth auth;  //firebase instance


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.email);      //get the two texts
        inputPassword = findViewById(R.id.password);

        btnRegister = findViewById(R.id.button);    //get the button

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();         //recover the informations from the text fields
                String password = inputPassword.getText().toString();

                //additional password requirements can be added
                passwordStrengthCheck(password);

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>(){

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {


                                    auth.getCurrentUser().sendEmailVerification();    //send a verification email

                                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));   //go back to login page


                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast fail = Toast.makeText(RegistrationActivity.this, "Registration Failed",Toast.LENGTH_SHORT);
                                    fail.show();

                                    ConnectivityManager conMan = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                                    NetworkInfo info = conMan.getActiveNetworkInfo();

                                    if(info == null ||!info.isConnected()){ //check if the error was caused by network connectivity problems

                                        Toast failConnection = Toast.makeText(RegistrationActivity.this, "Please verify you are connected to a network",Toast.LENGTH_LONG);
                                        failConnection.show();
                                    }
                                }
                            }
                        });

            }
        });
    }


    /**
     * @brief Display an error message if the password is less than 6 characters long
     * @param password The password to be checked
     */
    private void passwordStrengthCheck(String password){

        if(password.length() < 6){

            Toast fail = Toast.makeText(RegistrationActivity.this, "Password should be at least 6 characters long",Toast.LENGTH_LONG);
            fail.show();
        }
    }


}
