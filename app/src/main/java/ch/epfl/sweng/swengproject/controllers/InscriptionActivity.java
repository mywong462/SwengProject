package ch.epfl.sweng.swengproject.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import ch.epfl.sweng.swengproject.R;
import ch.epfl.sweng.swengproject.RegistrationActivity;
import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.storage.db.UserDao;

public class InscriptionActivity extends Activity {

    private ImageButton profilePictureButton;
    private EditText emailEditText;
    private EditText pswEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private Button registerButton;
    private Button goToLogginButton;

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
                register();
            }
        });

        goToLogginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogginActivity();
            }
        });

    }

    private void pickImage(){
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

    private void register(){
        User me = new User();
        me.setEmail(emailEditText.toString());
        me.setPassword(pswEditText.toString());
        me.setFirstName(firstNameEditText.toString());
        me.setLastName(lastNameEditText.toString());
        Bitmap bm = ((BitmapDrawable)profilePictureButton.getDrawable()).getBitmap();
        me.setPicture(bm);
        UserDao userDao = AppDatabase.getInMemoryDatabase(this).userDao();
        userDao.storeMyOwnProfile(me);

        User meButFetched = userDao.fetchMyOwnProfile();
        emailEditText.setText(meButFetched.email(),TextView.BufferType.EDITABLE);
        pswEditText.setText(meButFetched.password(),TextView.BufferType.EDITABLE);
        firstNameEditText.setText(meButFetched.firstName(),TextView.BufferType.EDITABLE);
        lastNameEditText.setText(meButFetched.lastName(),TextView.BufferType.EDITABLE);
        profilePictureButton.setImageBitmap(meButFetched.picture());


    }

}
