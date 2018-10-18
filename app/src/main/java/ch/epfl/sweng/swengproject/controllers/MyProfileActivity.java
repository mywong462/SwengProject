package ch.epfl.sweng.swengproject.controllers;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

    //widgets:
    private ImageButton profilePictureButton;
    private EditText emailEditText;
    private EditText pswEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private Button updateProfile;

    private User meBeforeAnyChange = null;

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

        //connecting the widgets
        profilePictureButton = findViewById(R.id.activity_my_profile_image_input);
        emailEditText = findViewById(R.id.activity_my_profile_email);
        pswEditText = findViewById(R.id.activity_my_profile_password);
        firstNameEditText = findViewById(R.id.activity_my_profile_first_name);
        lastNameEditText = findViewById(R.id.activity_my_profile_last_name);
        updateProfile = findViewById(R.id.activity_my_profile_update_button);
        updateProfile.setVisibility(View.GONE);

        new ShowMyProfileTask(this).execute();

        profilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updateProfile.getVisibility() == View.GONE){
                    updateProfile.setVisibility(View.VISIBLE);
                }else{
                    updateProfile.setVisibility(View.GONE);
                }
            }
        });
    }

    private void automaticallyFillTheInfos(User me){
        meBeforeAnyChange = me;
        profilePictureButton.setImageBitmap(me.picture());
        emailEditText.setText(me.email(), TextView.BufferType.EDITABLE);
        pswEditText.setText(me.password(), TextView.BufferType.EDITABLE);
        firstNameEditText.setText(me.firstName(), TextView.BufferType.EDITABLE);
        lastNameEditText.setText(me.lastName(), TextView.BufferType.EDITABLE);
        setWidgetsListeners();
    }

    private void setWidgetsListeners(){
        emailEditText.addTextChangedListener(new myTextWatcher());
        pswEditText.addTextChangedListener(new myTextWatcher());
        firstNameEditText.addTextChangedListener(new myTextWatcher());
        lastNameEditText.addTextChangedListener(new myTextWatcher());
    }


    private void logOutButtonPressed(){
        DialogFragment df = new LogOutAlertDialog();
        df.show(getSupportFragmentManager(), "want_logout");
    }



    @Override
    public void onPositiveClick(DialogFragment dialog) {
        //do nothing
    }

    @Override
    public void onNeutralClick(DialogFragment dialog) {
        //do nothing
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

    private static class ShowMyProfileTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<MyProfileActivity> activityReference;

        ShowMyProfileTask(MyProfileActivity context) {
            activityReference = new WeakReference<>(context);
        }

        User me = null;

        @Override
        protected Void doInBackground(Void... voids) {
            me = AppDatabase.getInstance().userDao().getMyOwnProfile();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            MyProfileActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            if(me != null){
                activity.automaticallyFillTheInfos(me);
            }
        }
    }

    private class myTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            hideOrShowUpdateButton();
        }
    }

    private void hideOrShowUpdateButton(){
        System.out.println("Actually checking");
        if(meBeforeAnyChange == null){
            return;
        }
        if(emailEditText.getText().toString() != meBeforeAnyChange.email()
                || pswEditText.getText().toString() != meBeforeAnyChange.password()
                || firstNameEditText.getText().toString() != meBeforeAnyChange.firstName()
                ||lastNameEditText.getText().toString() != meBeforeAnyChange.lastName()
                ){
            //|| !((BitmapDrawable) profilePictureButton.getDrawable()).getBitmap().
            //                sameAs(meBeforeAnyChange.picture())
            updateProfile.setVisibility(View.VISIBLE);
        }else{
            updateProfile.setVisibility(View.GONE);
        }
    }
};