package ch.epfl.sweng.swengproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static ch.epfl.sweng.swengproject.MyApplication.LOGTAG;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class CurrentLocationInstrumentedTestNoPermission {

    @Rule
    public ActivityTestRule<MainActivity> mActivity = new ActivityTestRule<>(MainActivity.class);

    private static final String PACKAGE
            = "ch.epfl.sweng.swengproject";
    private static final int LAUNCH_TIMEOUT = 5000;

    private static final String androidBtn = "android.widget.Button";


    private UiDevice mDevice;
    private Context context;
    private boolean disabled;
    private boolean revoked;
    private boolean closed;


    @Before
    public void before() throws UiObjectNotFoundException, InterruptedException, RemoteException {
        //Initialize UiDevice
        mDevice = UiDevice.getInstance(getInstrumentation());

        //Get Context
        context = InstrumentationRegistry.getContext();

        //Disable location
        Log.d(LOGTAG, "Disable location");
        disabled = disableLocation();
        Thread.sleep(1000);
        closed = closeApp();
        Thread.sleep(1000);

        revoked = revokePermission();
        Thread.sleep(500);
        Log.d(LOGTAG, "perm = " + revoked);

        mDevice.pressHome();

        //Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);


        //Start from home
        mDevice.pressHome();

        //Launch the app
        final Intent intent = new Intent(mActivity.getActivity(), MapsActivity.class);

        //Clear Previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        //Wait for app to appear
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void okLocationTest() {

        try {
            clickAllow();
            clickOKLocation();
        } catch (UiObjectNotFoundException e) {
            fail();
        }
    }

    @Test
    public void refuseTwiceTest() {
        try {
            clickAllow();
            clickNoThanksLocation();
            clickNoThanksLocation();
            clickOKLocation();
        } catch (UiObjectNotFoundException e) {
            fail();
        }
    }

    @Test
    public void locationOkThenDisableTest() throws InterruptedException, RemoteException{
        try {
            clickAllow();
            clickOKLocation();
            disableLocation();
            closeApp();
            Thread.sleep(500);
            goBackToSwengProjectApp();
            Thread.sleep(500);
            clickOKLocation();
        } catch (UiObjectNotFoundException e) {
            fail();
        }
    }

    @Test
    public void denyPermissionThenManuallyAllow() throws InterruptedException{
        try {
            clickDeny();
            //follow settings to the graaaaahl
            UiObject settingsBtn = mDevice.findObject(new UiSelector().text("SETTINGS"));
            settingsBtn.waitForExists(500);
            settingsBtn.clickAndWaitForNewWindow(500);

            UiObject permissionsBtn = mDevice.findObject(new UiSelector()
                    .className("android.widget.LinearLayout").childSelector(new UiSelector().text("Permissions")));
            permissionsBtn.click();

            UiObject locationSwitch = mDevice.findObject(new UiSelector().checkable(true).text("OFF"));
            locationSwitch.click();

            mDevice.pressBack();
            Thread.sleep(200);
            mDevice.pressBack();
            Thread.sleep(200);

            //Finally click done
            UiObject doneBtn = mDevice.findObject(new UiSelector().text("DONE !"));
            doneBtn.waitForExists(200);
            doneBtn.click();

            clickOKLocation();

        } catch (UiObjectNotFoundException e) {
            fail();
        }
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

    private boolean revokePermission() {
        getInstrumentation().getUiAutomation().executeShellCommand("pm revoke " + PACKAGE + " " + Manifest.permission.ACCESS_FINE_LOCATION);

        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    private boolean disableLocation() throws UiObjectNotFoundException, InterruptedException {
        final Intent disableLocIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        disableLocIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(disableLocIntent);
        UiObject disableLoc = mDevice.findObject(new UiSelector().className(android.widget.Switch.class.getName()).instance(1));
        Thread.sleep(500);
        if (disableLoc.exists() && disableLoc.isChecked()) {
            return disableLoc.click();
        }
        return false;
    }

    private boolean grantPermission() throws UiObjectNotFoundException, InterruptedException {
        final Intent revokePermIntent = new Intent();
        revokePermIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        revokePermIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", PACKAGE, null);
        revokePermIntent.setData(uri);
        context.startActivity(revokePermIntent);
        UiObject permissions = mDevice.findObject(new UiSelector().text("Permissions"));
        permissions.clickAndWaitForNewWindow();
        UiObject revokePerm = mDevice.findObject(new UiSelector().className(android.widget.Switch.class.getName()).instance(0));

        Thread.sleep(500);

        if (revokePerm.exists() && !revokePerm.isChecked()) {
            return revokePerm.click();
        }
        return false;
    }

    private boolean enableLocation() throws UiObjectNotFoundException, InterruptedException {
        final Intent disableLocIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        disableLocIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(disableLocIntent);
        UiObject disableLoc = mDevice.findObject(new UiSelector().className(android.widget.Switch.class.getName()).instance(1));
        Thread.sleep(500);
        if (disableLoc.exists() && !disableLoc.isChecked()) {
            return disableLoc.click();
        }
        return false;
    }

    private void goBackToSwengProjectApp() throws RemoteException, UiObjectNotFoundException{
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
