package ch.epfl.sweng.swengproject.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.InputStream;

import ch.epfl.sweng.swengproject.MapsActivity;
import ch.epfl.sweng.swengproject.MyApplication;
import ch.epfl.sweng.swengproject.R;
import ch.epfl.sweng.swengproject.helpers.alertdialog.AlertDialogGenericListener;
import ch.epfl.sweng.swengproject.helpers.alertdialog.GenericAlertDialog;
import ch.epfl.sweng.swengproject.helpers.inputvalidation.InscriptionValidator;
import ch.epfl.sweng.swengproject.storage.StorageHelper;
import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.storage.db.UserDao;

public class RegistrationActivity extends AppCompatActivity implements AlertDialogGenericListener, View.OnClickListener {

    //widgets:
    private ImageButton profilePictureButton;
    private EditText emailEditText;
    private EditText pswEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private  Button registerButton;

    //set to false once the user pressed the register button
    private boolean userCanInteract = true;

    //our profile that will be sent to the server if no error occurred
    private User meToSend = null;

    //Firebase auth
    private final FirebaseAuth auth = MyApplication.getFirebaseAuth();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //blocks the event to reach the buttons when the alert dialogs are displayed
        return userCanInteract && super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        System.out.println("---WELCOME IN REGISTRATION ACTIVITY---");

        //connect the widgets to the code
        profilePictureButton = findViewById(R.id.activity_main_profile_image_input);
        emailEditText = findViewById(R.id.registration_activity_email);
        pswEditText = findViewById(R.id.registration_activity_password);
        firstNameEditText = findViewById(R.id.registration_activity_first_name);
        lastNameEditText = findViewById(R.id.registration_activity_last_name);
        registerButton = findViewById(R.id.registration_activity_register_button);
        Button goToLogginButton = findViewById(R.id.registration_activity_go_to_login_activity);

        profilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCanInteract = false;
                checkBeforeRegister();
            }
        });

        goToLogginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginActivity();
            }
        });

        String emailFromRegistration  = getIntent().getStringExtra("email_to_propose");
        if(emailFromRegistration != null){
            emailEditText.setText(emailFromRegistration, TextView.BufferType.EDITABLE);
        }
    }

    private void pickImage() {
        //https://www.youtube.com/watch?v=Mm1dMWZWQ6w
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                try {
                    InputStream stream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    profilePictureButton.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void goToLoginActivity() {
        String emailToPropose = emailEditText.getText().toString();
        finish();
        startActivity(new Intent(this, LoginActivity.class)
                .putExtra("email_to_propose", emailToPropose));
    }

    private void checkBeforeRegister() {
        if (InscriptionValidator.fieldsAreValid(emailEditText.getText().toString(), pswEditText.getText().toString(),
                firstNameEditText.getText().toString(), lastNameEditText.getText().toString())) {
            register();
        }else{
            userCanInteract = true;
        }
    }

    private void register() {

        //TODO: change this useless test!
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo info = conMan.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) { //check if the error was caused by network connectivity problems
            Toast.makeText(MyApplication.getAppContext(), "Your inscription failed! Please make sure that you are connected to a network", Toast.LENGTH_LONG).show();

            userCanInteract = true;
            return;
        }

        auth.createUserWithEmailAndPassword(emailEditText.getText().toString(), pswEditText.getText().toString()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    auth.getCurrentUser().sendEmailVerification();

                    meToSend = new User();
                    meToSend.setEmail(emailEditText.getText().toString());
                    meToSend.setPassword(pswEditText.getText().toString());
                    meToSend.setFirstName(firstNameEditText.getText().toString());
                    meToSend.setLastName(lastNameEditText.getText().toString());
                    //TODO: should not save the default empty picture in the user. Check it before
                    Bitmap bm = ((BitmapDrawable) profilePictureButton.getDrawable()).getBitmap();
                    meToSend.setPicture(bm);

                    new StoreMyProfileAsyncTask().execute(meToSend);

                    showValidateEmailAlertDialog();

                } else {
                    Exception exception = task.getException();
                    if(exception instanceof com.google.firebase.auth.FirebaseAuthUserCollisionException){
                        showEmailExistAlertDialog();
                    }else{
                        // Toast.makeText(MyApplication.getAppContext(), "Your inscription failed ! We are sorry for that, please try later", Toast.LENGTH_LONG).show();
                        Toast.makeText(MyApplication.getAppContext(), "Hello", Toast.LENGTH_LONG).show();
                        userCanInteract = true;
                        System.out.println("The inscription failed because : " + exception.toString());
                    }
                }
            }
        });
    }



    private static class StoreMyProfileAsyncTask extends AsyncTask<User, Void, Void> {

        UserDao userDao = AppDatabase.getInstance().userDao();

        @Override
        protected Void doInBackground(User... users) {

            StorageHelper.deleteAllDataStoredLocally();

            User me = users[0];
            UserDao userDao = AppDatabase.getInstance().userDao();
            userDao.storeMyOwnProfile(me);
            return null;
        }
    }

    private static class DeleteAllOnDisk extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StorageHelper.deleteAllDataStoredLocally();
            return null;
        }
    }

    private void showValidateEmailAlertDialog(){
        DialogFragment df = new GenericAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", "Please certify your email");
        bundle.putString("message",
                "Before continuing to use this application, we must ensure that you provided your real email account. For this, please check your mailbox and click on the link you received from us. \nNo email received ? Maybe you made an error when typing your email...");
        bundle.putString("positiveButton","That's done !");
        bundle.putString("neutralButton","Check my email");
        bundle.putBoolean("cancelable", false);
        bundle.putInt("dialogID",1);

        df.setArguments(bundle);
        df.show(getSupportFragmentManager(), "validate_email");
    }

    private void showEmailExistAlertDialog(){
        DialogFragment df = new GenericAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", "This email already exist in our database");
        bundle.putString("message",
                "If you want to use an other email address, please click the \"Change Email\" button below. If you want to use this address, please login with it");

        bundle.putString("positiveButton","Login with this email");
        bundle.putString("neutralButton","Change Email");
        bundle.putBoolean("cancelable", false);
        bundle.putInt("dialogID", 2);
        df.setArguments(bundle);
        df.show(getSupportFragmentManager(), "email_already_exist");
    }

    @Override
    public void onPositiveClick(int id) {

        if(id == 1){//validate email alert dialog
            //the user pretend he verified his email
            auth.getCurrentUser()
                    .reload()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseUser user = auth.getCurrentUser();

                            if(user.isEmailVerified()){
                                StorageHelper.sendMyProfileToTheServer();
                                finish();
                                startActivity(new Intent(RegistrationActivity.this, MapsActivity.class));
                            }else{
                                showValidateEmailAlertDialog();
                            }
                        }
                    });
        }else if(id == 2) {//email exist alert dialog
            //the user want to log with this email instead of register
            finish();
            startActivity(new Intent(this, LoginActivity.class)
                    .putExtra("email_to_propose", emailEditText.getText().toString()));
        }

    }

    @Override
    public void onNeutralClick(int id) {
        if(id == 1) {//validate email alert dialog
            //the user believes he mistyped his email
            new DeleteAllOnDisk().execute();
            auth.getCurrentUser().delete();
            userCanInteract = true;
        }else if(id == 2) {//email exist alert dialog
            //the user want to log with this email instead of register
            userCanInteract = true;
        }
    }
    @Override
    public void onNegativeClick(int id) {

    }
}

