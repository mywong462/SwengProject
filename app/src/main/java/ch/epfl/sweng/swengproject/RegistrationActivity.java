package ch.epfl.sweng.swengproject;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.swengproject.controllers.MainActivity;


public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText inputEmail, inputPassword;
    private Button btnLogin, btnRegister, btnResetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = Database.getDBauth;

        inputEmail = findViewById(R.id.email2);
        inputPassword = findViewById(R.id.password2);
        btnLogin = findViewById(R.id.login_btn2);
        btnRegister = findViewById(R.id.register_btn2);
        btnResetPassword = findViewById(R.id.resetPassword_btn2);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start new resetPasswordActivity
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                //additional password requirements can be added
                passwordStrengthCheck(password);


                infoCheck(email, password);


                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>(){

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    auth.getCurrentUser().sendEmailVerification();
                                    

                                    //return to main activity for login

                                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));

                                } else {
                                    Toast fail = Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT);
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
    public void passwordStrengthCheck(String password){
        if(password.length() < 6){

            Toast fail = Toast.makeText(RegistrationActivity.this, "Password should be at least 6 characters long",Toast.LENGTH_LONG);
            fail.show();
        }
    }


    /**
     * @brief Sheck email and password fields for emptiness
     * @param email
     * @param password
     */

    public void infoCheck(String email, String password){
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_SHORT).show();
            return;
        }
    }


}
