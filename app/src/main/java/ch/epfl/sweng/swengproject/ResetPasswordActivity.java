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

public class ResetPasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText inputEmail;
    private Button btnResetPassword, btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        auth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.email3);
        btnResetPassword = findViewById(R.id.resetPassword_btn3);
        btnGoBack = findViewById(R.id.goBack_btn);

        String emailFromInscription  = getIntent().getStringExtra("email_to_propose");
        if(emailFromInscription != null){
            inputEmail.setText(emailFromInscription, TextView.BufferType.EDITABLE);
        }

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


                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this, "We've sent you an email with the instructions to reset your password.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private String getAndCheckEmail(String email){

        if(email.isEmpty()){
            Toast.makeText(getApplication(), "Enter your email address", Toast.LENGTH_SHORT).show();
        }
        return email;
    }
}
