package ch.epfl.sweng.swengproject.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ch.epfl.sweng.swengproject.Database;
import ch.epfl.sweng.swengproject.LoginActivity;
import ch.epfl.sweng.swengproject.MapsActivity;
import ch.epfl.sweng.swengproject.MyApplication;
import ch.epfl.sweng.swengproject.R;
import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.storage.db.UserDao;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth = Database.getDBauth;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User me = new User();
        me.setEmail("monEmail");
        me.setLastName("monNom");
        me.setFirstName("monPrénom");

        UserDao userDao = AppDatabase.getInMemoryDatabase(MyApplication.getAppContext()).userDao();


        User us[] = {me};
        userDao.insertUsers(me);
        new AsyncTask<Void, Void, Void>() {
            User me = null;
            @Override
            protected Void doInBackground(Void... params) {
                UserDao userDao = AppDatabase.getInMemoryDatabase(MyApplication.getAppContext()).userDao();

                    return null;
            }
        }
















/*
        new AsyncTask<Void, Void, Void>() {
            User me = null;
            @Override
            protected Void doInBackground(Void... params) {
                UserDao userDao = AppDatabase.getInMemoryDatabase(MyApplication.getAppContext()).userDao();*/
                /*me = userDao.fetchMyOwnProfile();
                if(me == null){
                    System.out.println("!!!!!!!!!!!!!!!!");
                }else{
                    System.out.println("my profile is not null in fact");
                }*/

/*

                 me = userDao.getUserByEmail("mon_email_test");
                if(me == null){
                    System.out.println("!!!!!!!!!!!!!!!!");
                }else {
                    System.out.println("my profile is not null in fact");
                }

                    return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(me == null){
                    System.out.println("From main activity: no profile user found in the disk");
                    startActivity(new Intent(MainActivity.this, InscriptionActivity.class));
                }else{
                    System.out.println("From main activity: a profile user was found in the disk with email "+me.email());
                    auth.signInWithEmailAndPassword(me.email(), me.password())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>(){

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        System.out.println("Correctly log in to an account ...");

                                        FirebaseUser user = auth.getCurrentUser();
                                        if(!user.isEmailVerified()){
                                            System.out.println("...but the email was not verified");
                                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                        }else{
                                            System.out.println( "..and the email was verified, all is ok!");
                                            startActivity(new Intent(MainActivity.this, MapsActivity.class));
                                        }

                                    } else {
                                        System.out.println("We found a profile, but automatic login failed");
                                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    }
                                }
                            });
                }
            }


        }.execute();
      */
    }
}
