package ch.epfl.sweng.swengproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class CurrentLocationInstrumentedTestNoPermission {

    private static final String PACKAGE
            = "ch.epfl.sweng.swengproject";
    private static final int LAUNCH_TIMEOUT = 5000;

    private static final String androidBtn = "android.widget.Button";


    private UiDevice mDevice;
    private Context context;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void before() throws UiObjectNotFoundException, InterruptedException, RemoteException {
        //Initialize UiDevice
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        //Get Context
        context = InstrumentationRegistry.getContext();

        //Disable location
        disableLocation();

        //Revoke location permission
        revokePermission();


        mDevice.pressHome();

        //close all the apps
        mDevice.pressRecentApps();
        List<UiObject2> apps = mDevice.findObjects(By.clazz("android.widget.ImageView").clickable(true));
        apps.forEach(new Consumer<UiObject2>() {
            @Override
            public void accept(UiObject2 uiObject2) {
                uiObject2.click();
            }
        });


        //Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);


        //Start from home
        mDevice.pressHome();

        //Launch the app
        final Intent mapsIntent = new Intent(mActivityRule.getActivity(), MapsActivity.class);

        //Clear Previous instances
        mapsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(mapsIntent);

        //Wait for app to appear
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void allowTest(){
        try {
            clickAllow();
            clickOKLocation();
        }catch (UiObjectNotFoundException e){fail();}
    }

    @Test
    public void denyTest(){
        try {
            clickDeny();
        }catch (UiObjectNotFoundException e){fail();}
    }

    private void clickAllow() throws UiObjectNotFoundException{
        UiObject allowBtn = mDevice.findObject(new UiSelector()
        .text("ALLOW")
        .className(androidBtn));

        allowBtn.waitForExists(2000);
        allowBtn.click();
    }

    private void clickDeny() throws UiObjectNotFoundException{
        UiObject allowBtn = mDevice.findObject(new UiSelector()
                .text("DENY")
                .className(androidBtn));

        allowBtn.waitForExists(2000);
        allowBtn.click();
    }

    private void clickOKLocation() throws UiObjectNotFoundException{
        UiObject OKBtn = mDevice.findObject(new UiSelector()
                .text("OK")
                .className(androidBtn));
        OKBtn.waitForExists(2000);
        if(OKBtn.exists()) {
            OKBtn.click();
        }
    }

    private void clickNoThanksLocation() throws UiObjectNotFoundException{
        UiObject noThxBtn = mDevice.findObject(new UiSelector()
                .text("OK")
                .className(androidBtn));
        noThxBtn.waitForExists(2000);
        if(noThxBtn.exists()) {
            noThxBtn.click();
        }
    }


    private void revokePermission() throws UiObjectNotFoundException, InterruptedException{
        final Intent revokePermIntent = new Intent();
        revokePermIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        revokePermIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Uri uri = Uri.fromParts("package", PACKAGE, null);
        revokePermIntent.setData(uri);
        context.startActivity(revokePermIntent);
        UiObject permissions = mDevice.findObject(new UiSelector().text("Permissions"));
        permissions.clickAndWaitForNewWindow();
        UiObject revokePerm = mDevice.findObject(new UiSelector().className(android.widget.Switch.class.getName()).instance(0));
        if(revokePerm.exists() && revokePerm.isChecked()){
            revokePerm.click();
        }
        Thread.sleep(1000);
    }

    private void disableLocation() throws UiObjectNotFoundException, InterruptedException{
        final Intent disableLocIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(disableLocIntent);
        UiObject disableLoc = mDevice.findObject(new UiSelector().className(android.widget.Switch.class.getName()).instance(1));
        if(disableLoc.exists() && disableLoc.isChecked()){
            disableLoc.click();
        }
        Thread.sleep(1000);
    }
}
