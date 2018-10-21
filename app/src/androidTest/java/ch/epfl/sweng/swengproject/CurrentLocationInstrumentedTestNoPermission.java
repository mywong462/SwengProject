package ch.epfl.sweng.swengproject;

import android.Manifest;
import android.arch.core.util.Function;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static ch.epfl.sweng.swengproject.MyApplication.LOGTAG;

@RunWith(AndroidJUnit4.class)
public class CurrentLocationInstrumentedTestNoPermission {
    @Rule
    public ActivityTestRule<MapsActivity> mActivity = new ActivityTestRule<>(MapsActivity.class);

    private static final String PACKAGE
            = "ch.epfl.sweng.swengproject";

    private static final String androidBtn = "android.widget.Button";

    private UiDevice mDevice = UiDevice.getInstance(getInstrumentation());
    private Context context;


    @Before
    public void before() {

        context = InstrumentationRegistry.getContext();

        //Disable location
        disableLocation();
    }


    @Test
    public void okLocationTest() throws UiObjectNotFoundException{
        clickOKLocation();
    }

    @Test
    public void refuseTwiceTest() throws UiObjectNotFoundException{
        clickNoThanksLocation();
        clickNoThanksLocation();
        clickOKLocation();

    }

    @Test
    public void locationOkThenDisableTest() throws UiObjectNotFoundException{
        clickOKLocation();
        disableLocation();
        clickOKLocation();
    }


    private void clickAllow() throws UiObjectNotFoundException {
        UiObject allowBtn = mDevice.findObject(new UiSelector()
                .text("ALLOW")
                .className(androidBtn));

        allowBtn.waitForExists(500);
        allowBtn.click();
    }

    private void clickDeny() throws UiObjectNotFoundException {
        UiObject allowBtn = mDevice.findObject(new UiSelector()
                .text("DENY")
                .className(androidBtn));

        allowBtn.waitForExists(500);
        allowBtn.click();
    }

    private void clickOKLocation() throws UiObjectNotFoundException {
        UiObject OKBtn = mDevice.findObject(new UiSelector()
                .text("OK")
                .className(androidBtn));
        OKBtn.waitForExists(500);
        if (OKBtn.exists()) {
            OKBtn.click();
        }

        clickAgreeImproveLocationAccuracy();
    }

    private void clickNoThanksLocation() throws UiObjectNotFoundException {
        UiObject noThxBtn = mDevice.findObject(new UiSelector()
                .text("OK")
                .className(androidBtn));
        noThxBtn.waitForExists(500);
        if (noThxBtn.exists()) {
            noThxBtn.click();
        }
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

    /**
     * Can only be used once, otherwise it crashes...WHY??
     */
    private boolean revokePermission() {
        getInstrumentation().getUiAutomation().executeShellCommand("pm revoke " + PACKAGE + " " + Manifest.permission.ACCESS_FINE_LOCATION);

        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    private boolean disableLocation() {
        getInstrumentation().getUiAutomation().executeShellCommand("settings put secure location_providers_allowed -gps");
        getInstrumentation().getUiAutomation().executeShellCommand("settings put secure location_providers_allowed -network");

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled;
        boolean network_enabled;

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return !(gps_enabled && network_enabled);

    }

    private void grantPermission() throws InterruptedException {
        getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + PACKAGE + " " + Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void enableLocation() throws InterruptedException {
        getInstrumentation().getUiAutomation().executeShellCommand("settings put secure location_providers_allowed +gps");
        getInstrumentation().getUiAutomation().executeShellCommand("settings put secure location_providers_allowed +network");
    }

    private void goBackToSwengProjectApp() throws RemoteException, UiObjectNotFoundException {
        //Be sure no other app is running
        mDevice.pressRecentApps();

        UiObject app = mDevice.findObject(new UiSelector()
                .clickable(true)
                .className("android.widget.FrameLayout"));

        app.click();
    }

    private boolean closeApp() throws UiObjectNotFoundException, RemoteException, InterruptedException {
        mDevice.pressRecentApps();

        boolean res;

        UiObject close = mDevice.findObject(new UiSelector().resourceId("com.android.systemui:id/dismiss_task"));
        res = close.click();
        Thread.sleep(100);

        Log.d(LOGTAG, "res = " + res);

        mDevice.pressHome();

        return res;
    }
}
