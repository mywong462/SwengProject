package ch.epfl.sweng.swengproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.core.util.Function;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static ch.epfl.sweng.swengproject.MyApplication.LOGTAG;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
public class CurrentLocationInstrumentedTest {


    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void setCurrentLocationParametersValidTest(){
        MyApplication.currentLocation.setCurrentLocationParameters(mActivityRule.getActivity(), mActivityRule.getActivity());
    }

    @Test
    public void setCurrentLocationParametersInvalidTest1() {
        try {
            MyApplication.currentLocation.setCurrentLocationParameters(null, mActivityRule.getActivity());
        }catch(NullPointerException e){
            return;
        }
        fail();
    }

    @Test
    public void setCurrentLocationParametersInvalidTest2() {
        try {
            MyApplication.currentLocation.setCurrentLocationParameters(mActivityRule.getActivity(), null);
        }catch(NullPointerException e){
            return;
        }
        fail();
    }

    private boolean test = false;
    private boolean finished = false;

    private Function<Void, Void> f = new Function<Void, Void>() {
        @Override
        public Void apply(Void input) {
            change();
            return null;
        }
    };

    private void change(){
        synchronized (lock) {
            test = true;
            lock.notifyAll();
        }
    }

    private final Object lock = new Object();

    @Test
    public void showDialogTest(){

        synchronized (lock) {
            if(!finished) {
                mActivityRule.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog dialog = MyApplication.showCustomAlert2Buttons("title", "message", "test1", "test2", f, f, mActivityRule.getActivity());
                        dialog.show();
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
                        finished = true;
                    }
                });
            }
        }


        synchronized (lock) {
            while(!finished){
                try{
                    lock.wait();
                }catch (InterruptedException e){}
            }
            assertTrue(test);
        }
    }
}