package ch.epfl.sweng.swengproject;

import android.app.AlertDialog;
import android.arch.core.util.Function;
import android.location.Location;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;


import static ch.epfl.sweng.swengproject.MyApplication.LOGTAG;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class CurrentLocationInstrumentedTest {


    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Ignore
    public void setCurrentLocationParametersValidTest(){
        MyApplication.currentLocation.setCurrentLocationParameters(mActivityRule.getActivity(), mActivityRule.getActivity());
    }

    @Ignore
    public void setCurrentLocationParametersInvalidTest1() {
        try {
            MyApplication.currentLocation.setCurrentLocationParameters(null, mActivityRule.getActivity());
        }catch(NullPointerException e){
            return;
        }
        fail();
    }

    @Ignore
    public void setCurrentLocationParametersInvalidTest2() {
        try {
            MyApplication.currentLocation.setCurrentLocationParameters(mActivityRule.getActivity(), null);
        }catch(NullPointerException e){
            return;
        }
        fail();
    }

    private boolean test = false;
    private boolean finished1 = false;

    private Function<Void, Void> f1 = new Function<Void, Void>() {
        @Override
        public Void apply(Void input) {
            change();
            return null;
        }
    };

    private void change(){
        synchronized (lock1) {
            test = true;
            lock1.notifyAll();
        }
    }

    private final Object lock1 = new Object();

    @Ignore
    public void showDialogTest(){

        synchronized (lock1) {
            if(!finished1) {
                mActivityRule.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog dialog = MyApplication.showCustomAlert2Buttons("title", "message", "test1", "test2", f1, f1, mActivityRule.getActivity());
                        dialog.show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
                        finished1 = true;
                    }
                });
            }
        }


        synchronized (lock1) {
            while(!finished1){
                try{
                    lock1.wait();
                }catch (InterruptedException e){}
            }
            assertTrue(test);
        }
    }



    private final Object lock2 = new Object();
    private LatLng currLoc;

    private Function<Void, Void> checkLocFunc = new Function<Void, Void>() {
        @Override
        public Void apply(Void input) {
            Log.d(LOGTAG, "checkLocFunc");
            locationNotNull();
            return null;
        }
    };

    private void locationNotNull(){
        synchronized (lock2) {
            currLoc = MyApplication.currentLocation.getLastLocation();
            finished2 = true;
            lock2.notifyAll();
        }
    }

    private boolean finished2 = false;

    @Mock
    private Location mockLocation = mock(android.location.Location.class);

    @Ignore
    public void checkLocationTest(){


        Log.d(LOGTAG, "coucou");
        MyApplication.currentLocation.setCurrentLocationParameters(mActivityRule.getActivity(), mActivityRule.getActivity(), checkLocFunc);
        Log.d(LOGTAG, "hey");
        MyApplication.currentLocation.callerActivityReady();
        Log.d(LOGTAG, "how");
        //MyApplication.currentLocation.injectionForTest(mockLocation);
        Log.d(LOGTAG, "jhhgjhgh");



        synchronized (lock2){
            Log.d(LOGTAG, "deadlock");
            while (!finished2){
                try {
                    lock2.wait();
                } catch (InterruptedException e){}
            }

            assertEquals(currLoc.latitude, 22.0);
            assertEquals(currLoc.longitude, 80.0);
        }


    }
}