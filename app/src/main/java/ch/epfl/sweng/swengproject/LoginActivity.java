package ch.epfl.sweng.swengproject;

import android.content.Intent;
import android.content.SharedPreferences;
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

// TO DO: import the method checkInfo ect from Registration activity to call them here
// (code repetition)

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth = Database.getDBauth;
    private EditText inputEmail, inputPassword;
    private Button btnLogin, btnRegister, btnResetPassword;
    private boolean test = false;


    public void setAuth(FirebaseAuth firebaseAuth){
        this.auth = firebaseAuth;
        this.test = true;
    }

    public OnCompleteListener listener = new OnCompleteListener<AuthResult>(){

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (!auth.getCurrentUser().isEmailVerified()) {
                Toast.makeText(getApplicationContext(), "Email not verified", Toast.LENGTH_SHORT).show();
                return;
            }
            if (task.isSuccessful()) {
                if (!LoginActivity.this.getSharedPreferences("UserProfile", 0).contains("userName")) {
                    startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
                } else {
                    startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                    finish();
                }
            } else {
                Toast fail = Toast.makeText(LoginActivity.this, "Login Failed",Toast.LENGTH_LONG);
                fail.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getViewElements();

        addListeners();
    }


    private void getViewElements(){
        inputEmail = findViewById(R.id.email1);
        inputPassword = findViewById(R.id.password1);
        btnLogin = findViewById(R.id.login_btn1);
        btnRegister = findViewById(R.id.register_btn1);
        btnResetPassword = findViewById(R.id.resetPassword_btn1);
    }

    private void addListeners(){

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

                String email = checkInput(inputEmail.getText().toString());
                String password = checkInput(inputPassword.getText().toString());

                if(!test) {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, listener);
                }
            }
        });

    }

    private String checkInput(String in){
        if(in.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
        return in;
    }

}
