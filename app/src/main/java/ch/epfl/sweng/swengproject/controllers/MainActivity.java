package ch.epfl.sweng.swengproject.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.ref.WeakReference;

import ch.epfl.sweng.swengproject.Database;
import ch.epfl.sweng.swengproject.LoginActivity;
import ch.epfl.sweng.swengproject.MapsActivity;
import ch.epfl.sweng.swengproject.MyApplication;
import ch.epfl.sweng.swengproject.R;
import ch.epfl.sweng.swengproject.storage.StorageHelper;
import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.storage.db.UserDao;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth = Database.getDBauth;
    private User me = null;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*User u = new User();
        u.setFirstName("Jojo");
        u.setLastName("Kaes");
        u.setEmail("monEmail" + (int) (Math.random() * 1000000));
        u.setPassword("Top secret putain");
final User[] us = {u};

        new AsyncTask<Void, Void, Void>() {

            UserDao userDao = AppDatabase.getDatabase(MyApplication.getAppContext()).userDao();

            @Override
            protected Void doInBackground (Void... aV){
                User lastMe = userDao.getMyOwnProfile();
                if(lastMe == null){
                    System.out.println("Impossible to retrive my last profile on disk");
                }else{
                    System.out.println("My last profile retrived had as email " + lastMe.email());
                }
                System.out.println("Ready to insert the user with email " + us[0].email() + "in the database as me !");

                userDao.storeMyOwnProfile(us[0]);
                List<User> allUsers = userDao.getAll();
                System.out.println("The is currently " + allUsers.size() + " users in the database.");
                return null;
            }


        }.execute();*/

        new GetMyProfileTask(this).execute();
    }


    private static class GetMyProfileTask extends AsyncTask<Void, Void, Void> {

        UserDao userDao = AppDatabase.getDatabase(MyApplication.getAppContext()).userDao();
        User me = null;

        private WeakReference<MainActivity> activityReference;

        // only retain a weak reference to the activity
        GetMyProfileTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            me = userDao.getMyOwnProfile();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.takeFirstDecision(me);
        }
    }

    private void takeFirstDecision(User user) {
        me = user;

        if (me == null || me.password() == null || me.email() == null) {
            System.out.println("First decision taken, no profile found in HD (or founded but with email or password null), go to Login Activity");
            goToInscriptionActivity();
        } else {
            System.out.println("First decision taken, a profile found in HD, should try to login automatically");
            tryAutomaticLogin();
        }
    }


    private void tryAutomaticLogin() {
        String email = me.email();
        String password = me.password();
        System.out.println("Try to login automatically with email " + email + "and password " + password + " fetched from HD");

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            System.out.println("After fetching a profile from the HD, the automatic login failed, so we send the user to the Inscription Activity");
                            goToInscriptionActivity();
                        } else {
                            System.out.println("After fetching a profile from the HD, the automatic login succeeded, it's time to check if the user verified his email already");
                            checkUserEmailIsConfirmed();
                        }
                    }
                });
    }

    private void checkUserEmailIsConfirmed() {
        FirebaseUser user = auth.getCurrentUser();
        if (!user.isEmailVerified()) {
            System.out.println("The user cannot use this app if his email is verified. The user is sent to the logging activity");
            //TODO: the login activity must have the email and psw set already héhé.
            goToLoginActivity();
        } else {
            System.out.println("...and the email was verified, all is ok!");
            goToMapsActivity();
        }

    }

    private void goToInscriptionActivity() {
        new DeleteAllOnDisk().execute();
        finish();
        startActivity(new Intent(MainActivity.this, InscriptionActivity.class));
    }

    private void goToLoginActivity() {
        finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    private void goToMapsActivity() {
        finish();
        startActivity(new Intent(MainActivity.this, MapsActivity.class));
    }

    private static class DeleteAllOnDisk extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StorageHelper.deleteAllDataStoredLocally();
            return null;
        }
    }
}