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

    /** Variable to keep track of the last need created by the user and allowing him to create only one
     * It is initialized at 0L, so I think we should delete the needs of a user that closes the app
     * This makes sense in an app where needs have very short lifespan
     */
    private static long user_need_ttl;
    public long getUser_need_ttl() {
        return user_need_ttl;
    }
    public void setUser_need_ttl(Long user_need_ttl) {
        this.user_need_ttl = user_need_ttl;
    }
    public void setUser_need_ttl_OnStart(Long user_need_ttl) {
        this.user_need_ttl = user_need_ttl;
    }

    private static int user_need_ppl;
    public int getUser_need_ppl() {
        return user_need_ppl;
    }
    public void setUser_need_ppl(int user_need_ppl) {
        this.user_need_ppl = user_need_ppl;
    }
    public void setUser_need_ppl_OnStart(int user_need_ppl) { this.user_need_ppl = user_need_ppl; }

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
