package ch.epfl.sweng.swengproject;


import android.app.Application;
import android.content.Context;

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

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
