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
    // Variable to keep track of the last need created by the user and allowing him to create only one
    // It is initialized at 0L, so I think we should delete the needs of a user that closes the app
    // This makes sense in an app where needs have very short lifespan
    private long user_need_ttl;
    public long getUser_need_ttl() {
        return user_need_ttl;
    }
    public void setUser_need_ttl(Long user_need_ttl) {
        this.user_need_ttl = user_need_ttl;
    }
    public void setUser_need_ttl_OnStart(Long user_need_ttl) {
        this.user_need_ttl = user_need_ttl;
    }

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
