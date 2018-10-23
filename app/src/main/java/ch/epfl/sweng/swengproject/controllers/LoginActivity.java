package ch.epfl.sweng.swengproject.controllers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.swengproject.MapsActivity;
import ch.epfl.sweng.swengproject.MyApplication;
import ch.epfl.sweng.swengproject.R;
import ch.epfl.sweng.swengproject.ResetPasswordActivity;
import ch.epfl.sweng.swengproject.helpers.alertdialog.AlertDialogGenericListener;
import ch.epfl.sweng.swengproject.helpers.alertdialog.GenericAlertDialog;
import ch.epfl.sweng.swengproject.storage.StorageHelper;



public class LoginActivity extends AppCompatActivity implements AlertDialogGenericListener {

    private final FirebaseAuth auth = MyApplication.getFirebaseAuth();
    private EditText inputEmail, inputPassword;
    private Button btnLogin, btnRegister, btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //connect the widgets
        inputEmail = findViewById(R.id.login_activity_email_edit_text);
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
                String emailToPropose = inputEmail.getText().toString();
                finish();
                startActivity(new Intent(LoginActivity.this, InscriptionActivity.class)
                        .putExtra("email_to_propose", emailToPropose));
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
                btnLogin.setEnabled(false);

                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                if(!inputAreCorrect(email, password)){
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>(){

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful() && auth.getCurrentUser().isEmailVerified()) {
                                    StorageHelper.getOrSendMyProfileToServer();
                                    finish();
                                    startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                                } else if(task.isSuccessful() && !auth.getCurrentUser().isEmailVerified()){
                                    showConfirmEmailAlertDialog();
                                    btnLogin.setEnabled(true);
                                }else {
                                    Toast fail = Toast.makeText(LoginActivity.this, "Login Failed",Toast.LENGTH_LONG);
                                    fail.show();
                                    btnLogin.setEnabled(true);
                                    Exception exception = task.getException();
                                    System.out.println("The login failed because of : " + exception.toString());
                                }
                            }
                        });
            }
        });

    }

    private void showConfirmEmailAlertDialog(){
        DialogFragment df = new GenericAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Please certify your email");
        bundle.putString("message",
                "You cannot log in to this app with an email that has not been validate. Please open your mailbox and click the link you received from us.");
        bundle.putString("positive", "Send me an email again");
        bundle.putString("neutral","Ok");
        bundle.putInt("dialogID",1);
        df.setArguments(bundle);
        df.show(getSupportFragmentManager(), "validate_email_in_login");
    }

    @Override
    public void onPositiveClick(int id) {
        auth.getCurrentUser().sendEmailVerification();
    }

    @Override
    public void onNeutralClick(int id) {

    }

    @Override
    public void onNegativeClick(int id) {

    }

    private boolean inputAreCorrect(String email, String password){

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



}
