package ch.epfl.sweng.swengproject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.arch.core.util.Function;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import ch.epfl.sweng.swengproject.storage.db.AppDatabase;

public class MyApplication extends Application {
    /**
     * Base class for maintaining global application state. You can provide
     * your own implementation by creating a subclass and specifying the
     * fully-qualified name of this subclass as the "android:name"
     * attribute in your AndroidManifest.xml's &lt;application&gt; tag.
     * The Application class, or your subclass of the Application class,
     * is instantiated before any other class when the process for your
     * application/package is created.
     * https://developer.android.com/reference/android/app/Application
     */

    private static boolean isUnderTest = false;
    private static Context context = null;
    private static FirebaseAuth firebaseAuth = null;
    private static  CurrentLocation currentLocation = null;
    public static final String LOGTAG = "HELLO";

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        firebaseAuth = FirebaseAuth.getInstance();

        currentLocation = new CurrentLocation();
    }

    /**
     *
     * @param isUnderTest set it to true to specify that the application is under tests
     */
    public static void setUnderTest(boolean isUnderTest){
        //broadcast this event to all required classes of the application
        MyApplication.isUnderTest = isUnderTest;
        AppDatabase.setUnderTest(isUnderTest);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    /**
     *
     * @return the object used to authenticate user with Firebase
     */
    public static FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }


    /**
     * This method is used only from UITests when we want to mock FirebaseAuth
     * @param firebaseAuth the object used to authenticate user with Firebase that the App will use
     */
    public static void setFirebaseAuthMock(FirebaseAuth firebaseAuth) {
        MyApplication.firebaseAuth = firebaseAuth;
    }


    //--------------------some mystic stuff :)--------------------
    public static AlertDialog showCustomAlert2Buttons(String title, String message, String neutralButtonText, String positiveButtonText, final Function<Void, Void> callOnNeutralClick, final Function<Void, Void> callOnPositiveClick, Activity activity){
        return new AlertDialog.Builder(activity)
                .setTitle(Objects.requireNonNull(title))
                .setMessage(Objects.requireNonNull(message))
                .setNeutralButton(Objects.requireNonNull(neutralButtonText), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callOnNeutralClick.apply(null);
                    }
                })
                .setPositiveButton(Objects.requireNonNull(positiveButtonText), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callOnPositiveClick.apply(null);
                    }
                })
                .create();
    }


    public static CurrentLocation getCurrentLocation(){
        return currentLocation;
    }

}
