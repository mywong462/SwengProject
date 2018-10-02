package ch.epfl.sweng.swengproject;

import android.content.Intent;
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


public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText inputEmail, inputPassword;
    private Button btnLogin, btnRegister, btnResetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login_btn);
        btnRegister = findViewById(R.id.register_btn);
        btnResetPassword = findViewById(R.id.resetPassword_btn);

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

                infoCheck(email, password);
                passwordStrengthCheck(password);

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>(){

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    auth.getCurrentUser().sendEmailVerification()
                                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener() {

                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()) {
                                                        Toast success = Toast.makeText(RegistrationActivity.this, "Activation email sent to " + auth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT);
                                                        success.show();
                                                    } else {
                                                        Toast fail = Toast.makeText(RegistrationActivity.this, "Failed to send activation email", Toast.LENGTH_SHORT);
                                                        fail.show();
                                                    }
                                                }
                                            });
                                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                } else {
                                    Toast fail = Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT);
                                    fail.show();
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
