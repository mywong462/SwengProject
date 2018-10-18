package ch.epfl.sweng.swengproject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.arch.core.util.Function;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.util.Objects;

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
        Log.d(LOGTAG, "MyApplication created");
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

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

    public static final String LOGTAG = "HELLO";
    public static final CurrentLocation currentLocation = new CurrentLocation();

}
