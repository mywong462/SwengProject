package ch.epfl.sweng.swengproject.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
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


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth = Database.getDBauth;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User me = AppDatabase.getInMemoryDatabase(this).userDao().fetchMyOwnProfile();

        if(me == null){
            //the device do not store a profile for a user
            startActivity(new Intent(this, InscriptionActivity.class));
        }else{
            //the device indeed store a profile for a user

            auth.signInWithEmailAndPassword(me.email(), me.password())
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>(){

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                if(!user.isEmailVerified()){
                                    Toast.makeText(getApplicationContext(), "Email not verified", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                }else{
                                    startActivity(new Intent(MainActivity.this, MapsActivity.class));
                                }

                            } else {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        }
                    });


        }

    }
}
