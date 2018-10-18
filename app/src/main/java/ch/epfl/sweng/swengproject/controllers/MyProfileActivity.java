package ch.epfl.sweng.swengproject.controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;

import ch.epfl.sweng.swengproject.MapsActivity;
import ch.epfl.sweng.swengproject.R;
import ch.epfl.sweng.swengproject.helpers.alertdialog.AlertDialogGenericListener;
import ch.epfl.sweng.swengproject.helpers.alertdialog.LogOutAlertDialog;
import ch.epfl.sweng.swengproject.helpers.alertdialog.LoginAlertDialog;
import ch.epfl.sweng.swengproject.storage.StorageHelper;
import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.storage.db.UserDao;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MyProfileActivity extends AppCompatActivity implements AlertDialogGenericListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Button logOutButton = findViewById(R.id.activity_my_profile_logout_button);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutButtonPressed();
            }
        });

    }


    private void logOutButtonPressed(){
        DialogFragment df = new LogOutAlertDialog();
        df.show(getSupportFragmentManager(), "want_logout");
    }

    @Override
    public void onPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onNeutralClick(DialogFragment dialog) {

    }

    @Override
    public void onNegativeClick(DialogFragment dialog) {
        new LogOutTask(this).execute();
    }

    private void leave(){
        Intent i = new Intent(MyProfileActivity.this, MainActivity.class);
        //https://tips.androidhive.info/2013/10/how-to-clear-all-activity-stack-in-android/
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private static class LogOutTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<MyProfileActivity> activityReference;

        // only retain a weak reference to the activity
        LogOutTask(MyProfileActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            StorageHelper.deleteAllDataStoredLocally();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            MyProfileActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.leave();
        }
    }
}
