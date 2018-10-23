package ch.epfl.sweng.swengproject.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import ch.epfl.sweng.swengproject.R;
import ch.epfl.sweng.swengproject.helpers.alertdialog.AlertDialogGenericListener;
import ch.epfl.sweng.swengproject.helpers.alertdialog.LogOutAlertDialog;
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
                pickImage();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonClicked();
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
                    hideOrShowUpdateButton();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
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

        if(meBeforeAnyChange == null){
            return;
        }
        if(!emailEditText.getText().toString().equals(meBeforeAnyChange.email())
                || !pswEditText.getText().toString().equals(meBeforeAnyChange.password())
                || !firstNameEditText.getText().toString().equals(meBeforeAnyChange.firstName())
                ||!lastNameEditText.getText().toString().equals(meBeforeAnyChange.lastName())
                || !((BitmapDrawable) profilePictureButton.getDrawable()).getBitmap().
                            sameAs(meBeforeAnyChange.picture())
                ){
            updateProfile.setVisibility(View.VISIBLE);
        }else{
            updateProfile.setVisibility(View.GONE);
        }
    }

    private void updateButtonClicked(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(meBeforeAnyChange.email(), meBeforeAnyChange.password());

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            changeProfileNow(user);
                        }else{
                            Toast.makeText(MyProfileActivity.this, "Sorry, service not available for the moment",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void changeProfileNow(FirebaseUser user){
        changeEmailNow(user);
    }

    private void changeEmailNow(FirebaseUser user){
        final FirebaseUser u = user;
        //update email
        if(!meBeforeAnyChange.email().equals(emailEditText.getText().toString())){
            user.updateEmail(emailEditText.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                changePswNow(u);
                            }else{
                                Toast.makeText(MyProfileActivity.this, "Unable to change your email, sorry",Toast.LENGTH_LONG).show();
                                System.out.println(task.getException().toString());
                            }
                        }
                    });
        }else{
            changePswNow(u);
        }
    }

    private void changePswNow(FirebaseUser user){
        if(!meBeforeAnyChange.password().equals(pswEditText.getText().toString())){
            user.updatePassword(pswEditText.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                updateRest();
                            }else{
                                Toast.makeText(MyProfileActivity.this, "Unable to change your password, sorry",Toast.LENGTH_LONG).show();
                                System.out.println(task.getException().toString());
                            }
                        }
                    });
        }else{
            updateRest();
        }
    }

    private void updateRest(){
        User meNew = new User();
        meNew.setEmail(emailEditText.getText().toString());
        meNew.setPassword(pswEditText.getText().toString());
        meNew.setFirstName(firstNameEditText.getText().toString());
        meNew.setLastName(lastNameEditText.getText().toString());
        meNew.setPicture(((BitmapDrawable) profilePictureButton.getDrawable()).getBitmap());
        StorageHelper.saveThisUserAsMe(meNew);
        StorageHelper.sendMyProfileToTheServer(meNew);
        meBeforeAnyChange = meNew;
        hideOrShowUpdateButton();
    }
}
