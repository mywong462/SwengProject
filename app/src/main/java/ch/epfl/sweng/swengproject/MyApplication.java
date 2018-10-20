package ch.epfl.sweng.swengproject;


import android.app.Application;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

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
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
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

    /**
     *
     * @return the global context of this application
     */
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
    protected static final String LOGTAG = "HELLO";
}
