package ch.epfl.sweng.swengproject;

import android.app.Activity;
import android.arch.core.util.Function;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.swengproject.util.FakeLocation;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;


import static android.support.test.InstrumentationRegistry.getInstrumentation;


public class AddNeedInstrumentedTest2 {

    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public final ActivityTestRule<AddNeedActivity> mActivityRule =
            new ActivityTestRule<>(AddNeedActivity.class,false,false);


    private UiDevice mDevice;

    @Before
    public void create(){
        LocationServer locServ = new FakeLocation();

        mDevice = UiDevice.getInstance(getInstrumentation());

        mActivityRule.launchActivity(new Intent());
        mActivityRule.getActivity().setAddNeedActivity(true, locServ);
    }

    @After
    public void after(){
        clickOKLocationIfAsked();
    }


    @Test
    public void createWithOneFieldNull(){
        StringBuilder sb = new StringBuilder();
        sb.append("");

        onView(withId(R.id.descr_txt)).perform(typeText(sb.toString())).perform(closeSoftKeyboard());
        onView(withId(R.id.validity_txt)).perform(typeText("1")).perform(closeSoftKeyboard());
        onView(withId(R.id.nbPeople_txt)).perform(typeText("1")).perform(closeSoftKeyboard());

        onView(withId(R.id.create_btn)).perform(click());

    }


    @Test
    public void createWithInvalidValidity(){
        Looper.prepare();

        onView(withId(R.id.descr_txt)).perform(typeText("abcdefghijkl")).perform(closeSoftKeyboard());
        onView(withId(R.id.validity_txt)).perform(typeText("0")).perform(closeSoftKeyboard());
        onView(withId(R.id.nbPeople_txt)).perform(typeText("1")).perform(closeSoftKeyboard());

        onView(withId(R.id.create_btn)).perform(click());
    }

    @Test
    public void createWithInvalidNbPeople(){

        onView(withId(R.id.descr_txt)).perform(typeText("abcdefghijkl")).perform(closeSoftKeyboard());
        onView(withId(R.id.validity_txt)).perform(typeText("1")).perform(closeSoftKeyboard());
        onView(withId(R.id.nbPeople_txt)).perform(typeText("0")).perform(closeSoftKeyboard());

        onView(withId(R.id.create_btn)).perform(click());
    }


    @Test
    public void createWithOk(){
        onView(withId(R.id.descr_txt)).perform(typeText("abcdefghijkl")).perform(closeSoftKeyboard());
        onView(withId(R.id.validity_txt)).perform(typeText("1")).perform(closeSoftKeyboard());
        onView(withId(R.id.nbPeople_txt)).perform(typeText("1")).perform(closeSoftKeyboard());

        onView(withId(R.id.create_btn)).perform(click());
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
