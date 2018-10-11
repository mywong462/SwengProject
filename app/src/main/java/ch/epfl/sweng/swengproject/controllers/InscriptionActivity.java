package ch.epfl.sweng.swengproject.controllers;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.InputStream;

import ch.epfl.sweng.swengproject.Database;
import ch.epfl.sweng.swengproject.MyApplication;
import ch.epfl.sweng.swengproject.R;
import ch.epfl.sweng.swengproject.RegistrationActivity;
import ch.epfl.sweng.swengproject.helpers.inputvalidation.InscriptionValidator;
import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.storage.db.UserDao;

public class InscriptionActivity extends AppCompatActivity {

    //widgets:
    private ImageButton profilePictureButton;
    private EditText emailEditText;
    private EditText pswEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private Button registerButton;
    private Button goToLogginButton;

    //Firebase auth
    private FirebaseAuth auth = Database.getDBauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        profilePictureButton = findViewById(R.id.activity_main_profile_image_input);
        emailEditText = findViewById(R.id.inscription_activity_email);
        pswEditText = findViewById(R.id.inscription_activity_password);
        firstNameEditText = findViewById(R.id.inscription_activity_first_name);
        lastNameEditText = findViewById(R.id.inscription_activity_last_name);
        registerButton = findViewById(R.id.inscription_activity_register_button);
        goToLogginButton = findViewById(R.id.inscription_activity_go_to_login_activity);

        profilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBeforeRegister();
            }
        });

        goToLogginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogginActivity();
            }
        });

        String email = "monEmailTest"+(int)(Math.random()*100000)+"@gmail.com";
        emailEditText.setText(email,TextView.BufferType.EDITABLE);
        pswEditText.setText("Top secret haha",TextView.BufferType.EDITABLE);
    }

    private void pickImage(){
        //https://www.youtube.com/watch?v=Mm1dMWZWQ6w
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == 1){
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

    private void goToLogginActivity(){
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    private void checkBeforeRegister() {
        if (InscriptionValidator.fieldsAreValid(emailEditText.getText().toString(), pswEditText.getText().toString(),
                firstNameEditText.getText().toString(), lastNameEditText.getText().toString())) {
            register();
        }
    }

    private void register(){

        auth.createUserWithEmailAndPassword(emailEditText.getText().toString(), pswEditText.getText().toString()
        )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            auth.getCurrentUser().sendEmailVerification();
                            // Sign in success, update UI with the signed-in user's information
                           /* Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);*/
                            Toast.makeText(MyApplication.getAppContext(), "Okay", Toast.LENGTH_LONG).show();

                        } else {

                            ConnectivityManager conMan = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                            NetworkInfo info = conMan.getActiveNetworkInfo();

                            if(info == null ||!info.isConnected()){ //check if the error was caused by network connectivity problems
                                Toast.makeText(MyApplication.getAppContext(), "Your inscription failed! Please make sure that you are connected to a network",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(MyApplication.getAppContext(), "Registration Failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

        /*https://developer.android.com/reference/android/os/AsyncTask
        AsyncTask is designed to be a helper class around Thread
         and Handler and does not constitute a generic threading framework.
          AsyncTasks should ideally be used for short operations
          (a few seconds at the most.) [...]

          An asynchronous task is defined by a computation that
          runs on a background thread and whose result is published
          on the UI thread. An asynchronous task is defined by 3 generic types,
           called Params, Progress and Result,
           and 4 steps, called onPreExecute, doInBackground, onProgressUpdate and onPostExecute*/

        /*User me = new User();
        me.setEmail(emailEditText.toString());
        me.setPassword(pswEditText.toString());
        me.setFirstName(firstNameEditText.toString());
        me.setLastName(lastNameEditText.toString());
        Bitmap bm = ((BitmapDrawable)profilePictureButton.getDrawable()).getBitmap();
        me.setPicture(bm);*/

        //UserDao userDao = AppDatabase.getInMemoryDatabase(this).userDao();
        //userDao.storeMyOwnProfile(me);

        /*User meButFetched = userDao.fetchMyOwnProfile();
        emailEditText.setText(meButFetched.email(),TextView.BufferType.EDITABLE);
        pswEditText.setText(meButFetched.password(),TextView.BufferType.EDITABLE);
        firstNameEditText.setText(meButFetched.firstName(),TextView.BufferType.EDITABLE);
        lastNameEditText.setText(meButFetched.lastName(),TextView.BufferType.EDITABLE);
        profilePictureButton.setImageBitmap(meButFetched.picture());*/

         /*new  AsyncTask<User, Void, Void>() {
            @Override
            protected Void doInBackground(User... users) {
                User u = users[0];
                UserDao userDao = AppDatabase.getInMemoryDatabase(MyApplication.getAppContext()).userDao();
                userDao.storeMyOwnProfile(u);
                return null;
            }
        }.execute(me);*/


    }

}
