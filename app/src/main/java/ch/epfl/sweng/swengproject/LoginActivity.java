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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private Task usertask;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.email);      //get the two texts
        inputPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.button);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Need to reload the session after the email is verified
                usertask = auth.getCurrentUser().reload();
                usertask.addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {

                        String email = inputEmail.getText().toString();
                        String password = inputPassword.getText().toString();

                        if (TextUtils.isEmpty(email)) {
                            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (TextUtils.isEmpty(password)) {
                            Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!auth.getCurrentUser().isEmailVerified()) {
                            Toast.makeText(getApplicationContext(), "Email not verified", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>(){

                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            //change UI or activity !!!!!
                                            startActivity(new Intent(LoginActivity.this, MapActivity.class));


                                        } else {
                                            // If sign in fails, display a message to the user.

                                            Toast fail = Toast.makeText(LoginActivity.this, "Login Failed",Toast.LENGTH_LONG);
                                            fail.show();

                                        }



                                    }
                                });

                    }
                });
            }
        });
    }
}
