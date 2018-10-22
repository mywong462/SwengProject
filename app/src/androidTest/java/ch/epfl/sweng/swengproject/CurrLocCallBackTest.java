package ch.epfl.sweng.swengproject;


import android.arch.core.util.Function;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;


import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class CurrLocCallBackTest {

    @Rule
    public ActivityTestRule<MapsActivity> mActivity = new ActivityTestRule<>(MapsActivity.class);

    private UiDevice mDevice = UiDevice.getInstance(getInstrumentation());

    private boolean test = false;

    public Function<Void, Void> function = new Function<Void, Void>() {
        @Override
        public Void apply(Void input) {
            test = true;
            return null;
        }
    };

    @Before
    public void before() throws InterruptedException{
        try{
            clickAllow();
            clickOKLocation();
            Thread.sleep(2000);
        }catch (UiObjectNotFoundException e){}
    }

    @After
    public void after(){
        try{
            clickAllow();
            clickOKLocation();
        }catch (UiObjectNotFoundException e){}
    }


    @Test
    public void testgetLocationNonNull() {

        Location loc = mock(Location.class);
        when(loc.getLatitude()).thenReturn(40.0);
        when(loc.getLongitude()).thenReturn(7.0);

        ArrayList<Location> locList = new ArrayList<>();
        locList.add(loc);

        MyApplication.currentLocation.setFunction(null);

        LocationResult lr = LocationResult.create(locList);
        MyApplication.currentLocation.getCallBack().onLocationResult(lr);

        LatLng res = MyApplication.currentLocation.getLastLocation();

        assertEquals(new LatLng(40.0, 7.0), res);

    }

    @Test
    public void testVerifyFunction(){
        Location loc = mock(Location.class);
        when(loc.getLatitude()).thenReturn(40.0);
        when(loc.getLongitude()).thenReturn(7.0);

        ArrayList<Location> locList = new ArrayList<>();
        locList.add(loc);

        MyApplication.currentLocation.setFunction(function);

        LocationResult lr = LocationResult.create(locList);
        MyApplication.currentLocation.getCallBack().onLocationResult(lr);

        assertTrue(test);
    }


    @Test
    public void testCallbackNull(){
        MyApplication.currentLocation.getCallBack().onLocationResult(null);
    }



    private void clickOKLocation() throws UiObjectNotFoundException {
        UiObject OKBtn = mDevice.findObject(new UiSelector()
                .text("OK")
                .className("android.widget.Button"));
        OKBtn.waitForExists(500);
        if (OKBtn.exists()) {
            OKBtn.click();
        }

        clickAgreeImproveLocationAccuracy();
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

    private void clickAllow() throws UiObjectNotFoundException {
        UiObject allowBtn = mDevice.findObject(new UiSelector()
                .text("ALLOW")
                .className("android.widget.Button"));

        allowBtn.waitForExists(500);
        allowBtn.click();
    }

    private void disableLocation() {
        getInstrumentation().getUiAutomation().executeShellCommand("settings put secure location_providers_allowed -gps");
        getInstrumentation().getUiAutomation().executeShellCommand("settings put secure location_providers_allowed -network");
    }

}
