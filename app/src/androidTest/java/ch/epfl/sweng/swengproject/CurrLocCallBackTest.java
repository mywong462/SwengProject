package ch.epfl.sweng.swengproject;


import android.Manifest;
import android.app.Activity;
import android.arch.core.util.Function;
import android.location.Location;
import android.os.Looper;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

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
import static ch.epfl.sweng.swengproject.MyApplication.LOGTAG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class CurrLocCallBackTest {

    @Rule
    public ActivityTestRule<MapsActivity> mActivity = new ActivityTestRule<>(MapsActivity.class);

    private UiDevice mDevice = UiDevice.getInstance(getInstrumentation());

    private final int REQUEST_CHECK_SETTINGS = 555;

    private static final String PACKAGE
            = "ch.epfl.sweng.swengproject";


    private Location loc = mock(Location.class);

    @Before
    public void before() throws InterruptedException{

        try{
            clickAllow();
            clickOKLocation();
            Thread.sleep(2000);
        }catch (UiObjectNotFoundException e){
            e.printStackTrace();
        }

        when(loc.getLatitude()).thenReturn(40.0);
        when(loc.getLongitude()).thenReturn(7.0);
    }

    @After
    public void after(){
        try{
            clickAllow();
            clickOKLocation();
        }catch (UiObjectNotFoundException e){
            e.printStackTrace();
        }
    }


    @Test
    public void testgetLocationNonNull() {

        ArrayList<Location> locList = new ArrayList<>();
        locList.add(loc);

        MyApplication.currentLocation.setFunction(null);

        LocationResult lr = LocationResult.create(locList);
        MyApplication.currentLocation.getCallBack().onLocationResult(lr);

        LatLng res = MyApplication.currentLocation.getLastLocation();

        assertEquals(new LatLng(40.0, 7.0), res);

    }

    @Test
    public void testCallbackNull(){
        MyApplication.currentLocation.getCallBack().onLocationResult(null);
    }

    @Test
    public void permissionTest(){
        assertTrue(MyApplication.currentLocation.getLocationPermissionStatus());
    }

    @Test
    public void completeFlowTest() throws UiObjectNotFoundException{
        ArrayList<Location> locList = new ArrayList<>();
        locList.add(loc);
        LocationResult lr = LocationResult.create(locList);
        MyApplication.currentLocation.setTestMode(true);
        MyApplication.currentLocation.injectMockLocationResult(lr);
        clickOKLocation();
        MyApplication.currentLocation.onActivityResult(REQUEST_CHECK_SETTINGS, Activity.RESULT_OK, null);

        Log.d(LOGTAG, "AAAAAAAAAAAAAAAAAAAAAAAH");
        MyApplication.currentLocation.setTestMode(false);

        assertEquals(new LatLng(40.0, 7.0), MyApplication.currentLocation.getLastLocation());
    }

    @Ignore
    public void testDialogButton() throws UiObjectNotFoundException, InterruptedException{
        getInstrumentation().getUiAutomation().executeShellCommand("pm revoke " + PACKAGE + " " + Manifest.permission.ACCESS_FINE_LOCATION);
        UiObject settingsBtn = mDevice.findObject(new UiSelector().text("SETTINGS"));
        settingsBtn.clickAndWaitForNewWindow();
        UiObject appInfoTextField = mDevice.findObject(new UiSelector().text("App info"));
        assertTrue(appInfoTextField.exists());
        mDevice.pressBack();
        UiObject doneBtn = mDevice.findObject(new UiSelector().text("DONE !"));
        getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + PACKAGE + " " + Manifest.permission.ACCESS_FINE_LOCATION);
        Thread.sleep(2000);
        doneBtn.click();
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

    private void clickDeny() throws UiObjectNotFoundException {
        UiObject allowBtn = mDevice.findObject(new UiSelector()
                .text("DENY")
                .className("android.widget.Button"));

        allowBtn.waitForExists(500);
        allowBtn.click();
    }

    private void disableLocation() {
        getInstrumentation().getUiAutomation().executeShellCommand("settings put secure location_providers_allowed -gps");
        getInstrumentation().getUiAutomation().executeShellCommand("settings put secure location_providers_allowed -network");
    }

}
