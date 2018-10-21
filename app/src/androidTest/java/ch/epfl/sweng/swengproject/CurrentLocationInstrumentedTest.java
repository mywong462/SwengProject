package ch.epfl.sweng.swengproject;

import android.app.AlertDialog;
import android.arch.core.util.Function;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
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


@RunWith(AndroidJUnit4.class)
public class CurrentLocationInstrumentedTest {

    //These tests have all the permissions
    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    private UiDevice mDevice;

    @Before
    public void before(){
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @After
    public void after(){
        allowIfNeeded();
        clickOKLocationIfAsked();
    }

    @Rule
    public final ActivityTestRule<MapsActivity> mActivityRule =
            new ActivityTestRule<>(MapsActivity.class);

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

    @Test
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


    private void allowIfNeeded(){
        try {
            UiObject allowBtn = mDevice.findObject(new UiSelector()
                    .text("ALLOW")
                    .className("android.widget.Button"));

            allowBtn.waitForExists(500);
            allowBtn.click();
        }catch (UiObjectNotFoundException e){}
    }

    private void clickOKLocationIfAsked(){
        try {
            UiObject OKBtn = mDevice.findObject(new UiSelector()
                    .text("OK")
                    .className("android.widget.Button"));
            OKBtn.waitForExists(500);
            if (OKBtn.exists()) {
                OKBtn.click();
            }
            clickAgreeImproveLocationAccuracy();
        }catch(UiObjectNotFoundException e){}
    }

    private void clickAgreeImproveLocationAccuracy() throws UiObjectNotFoundException {
        UiObject agreeImprove = mDevice.findObject(new UiSelector()
                .text("AGREE")
                .index(1)
                .resourceId("android:id/button1")
                .className("android.widget.Button")
                .clickable(true)
                .packageName("com.google.android.gms"));
        agreeImprove.waitForExists(500);
        if (agreeImprove.exists()) {
            agreeImprove.click();
        }
    }
}