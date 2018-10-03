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


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText inputEmail, inputPassword;
    private Button btnLogin, btnRegister, btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.email1);
        inputPassword = findViewById(R.id.password1);
        btnLogin = findViewById(R.id.login_btn1);
        btnRegister = findViewById(R.id.register_btn1);
        btnResetPassword = findViewById(R.id.resetPassword_btn1);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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


                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>(){

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!auth.getCurrentUser().isEmailVerified()) {
                                    Toast.makeText(getApplicationContext(), "Email not verified", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(LoginActivity.this, MapActivity.class));
                                    finish();
                                } else {
                                    Toast fail = Toast.makeText(LoginActivity.this, "Login Failed",Toast.LENGTH_LONG);
                                    fail.show();
                                }
                            }
                        });
            }
        });
    }
}
