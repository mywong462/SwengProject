package ch.epfl.sweng.swengproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private EditText inputEmail;
    private Button btnResetPassword, btnGoBack;
    private boolean test = false;

    public OnCompleteListener listener = new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
                Toast.makeText(ResetPasswordActivity.this, "We've sent you an email with the instructions to reset your password.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void setAuth(FirebaseAuth fAuth){
        this.auth = fAuth;
        this.test = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        inputEmail = findViewById(R.id.email3);
        btnResetPassword = findViewById(R.id.resetPassword_btn3);
        btnGoBack = findViewById(R.id.goBack_btn);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getAndCheckEmail(inputEmail.getText().toString());

                if(!test){
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(listener);
                }
            }
        });

        String emailFromRegistration  = getIntent().getStringExtra("email_to_propose");
        if(emailFromRegistration != null){
            inputEmail.setText(emailFromRegistration, TextView.BufferType.EDITABLE);
        }
    }

    private String getAndCheckEmail(String email){

        if(email.isEmpty()){
            Toast.makeText(getApplication(), "Enter your email address", Toast.LENGTH_SHORT).show();
        }
        return email;
    }
}
