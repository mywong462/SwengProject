package ch.epfl.sweng.swengproject.controllers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
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

import ch.epfl.sweng.swengproject.Database;
import ch.epfl.sweng.swengproject.MapsActivity;
import ch.epfl.sweng.swengproject.R;
import ch.epfl.sweng.swengproject.ResetPasswordActivity;
import ch.epfl.sweng.swengproject.helpers.alertdialog.LoginADListener;
import ch.epfl.sweng.swengproject.helpers.alertdialog.LoginAlertDialog;

// TO DO: import the method checkInfo ect from Registration activity to call them here
// (code repetition)

public class LoginActivity extends AppCompatActivity implements LoginADListener {

    private final FirebaseAuth auth = Database.getDBauth;
    private EditText inputEmail, inputPassword;
    private Button btnLogin, btnRegister, btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //connect the widgets
        inputEmail = findViewById(R.id.email1);
        inputPassword = findViewById(R.id.password1);
        btnLogin = findViewById(R.id.login_btn1);
        btnRegister = findViewById(R.id.register_btn1);
        btnResetPassword = findViewById(R.id.resetPassword_btn1);

        String emailFromInscription  = getIntent().getStringExtra("email_to_propose");
        if(emailFromInscription != null){
            inputEmail.setText(emailFromInscription, TextView.BufferType.EDITABLE);
        }
        addListeners();
    }

    private void addListeners(){

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginActivity.this, InscriptionActivity.class));
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class)
                        .putExtra("email_to_propose",  inputEmail.getText().toString()));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = checkInput(inputEmail.getText().toString());
                String password = checkInput(inputPassword.getText().toString());

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>(){

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful() && auth.getCurrentUser().isEmailVerified()) {
                                    //TODO: MAY WANT TO SENT THE PROFILE IN THE SERVER HERE
                                    finish();
                                    startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                                } else if(task.isSuccessful() && !auth.getCurrentUser().isEmailVerified()){
                                    DialogFragment df = new LoginAlertDialog();
                                    df.show(getSupportFragmentManager(), "validate_email");
                                }else {
                                    Toast fail = Toast.makeText(LoginActivity.this, "Login Failed",Toast.LENGTH_LONG);
                                    fail.show();
                                    Exception exception = task.getException();
                                    System.out.println("The login failed because of : " + exception.toString());
                                }
                            }
                        });
            }
        });

    }

    @Override
    public void onLoginDialogPositivClick(DialogFragment dialog) {
        auth.getCurrentUser().sendEmailVerification();
    }
    private String checkInput(String in){
        if(in.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
        return in;
    }

}
