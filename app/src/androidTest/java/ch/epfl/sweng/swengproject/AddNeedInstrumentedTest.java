package ch.epfl.sweng.swengproject;


import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.concurrent.Executor;

import ch.epfl.sweng.swengproject.util.FakeLocation;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.action.ViewActions.typeText;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class AddNeedInstrumentedTest {

    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public final ActivityTestRule<AddNeedActivity> mActivityRule =
            new ActivityTestRule<>(AddNeedActivity.class,false,false);

    private UiDevice mDevice;

    @Before
    public void create(){

        mDevice = UiDevice.getInstance(getInstrumentation());

        LocationServer ls = new FakeLocation();

        //inject the mocked object in the activity
        mActivityRule.launchActivity(new Intent().putExtra("loc",ls));

    }

    @After
    public void after(){
        clickOKLocationIfAsked();
    }

    @Test
    public void invalidValidity1(){


        int failNumber = (int)Math.floor(Math.random()*(AddNeedActivity.MIN_VALIDITY));


        StringBuilder sb = new StringBuilder();

        for(int i = 1; i < AddNeedActivity.MIN_DESCR_L; ++i){

            sb.append('a');

        }


        onView(withId(R.id.validity_txt)).perform(typeText(""+failNumber)).perform(closeSoftKeyboard());

        onView(withId(R.id.descr_txt)).perform(typeText(sb.toString())).perform(closeSoftKeyboard());

        onView(withId(R.id.create_btn)).perform(click());


        boolean passed = false;

        try{
            onView(withId(R.id.create_need_btn)).perform(click());
        }
        catch(NoMatchingViewException e){

            passed = true;
        }

        assertEquals(true,passed);

    }



    @Test
    public void invalidValidity2(){


        int failNumber = (int)Math.ceil(Math.random()+AddNeedActivity.MAX_VALIDITY);



        StringBuilder sb = new StringBuilder();

        for(int i = 1; i < AddNeedActivity.MIN_DESCR_L; ++i){

            sb.append('a');

        }

        onView(withId(R.id.validity_txt)).perform(typeText(""+failNumber)).perform(closeSoftKeyboard());
        onView(withId(R.id.descr_txt)).perform(typeText(sb.toString())).perform(closeSoftKeyboard());

        onView(withId(R.id.create_btn)).perform(click());

        boolean passed = false;

        try{
            onView(withId(R.id.create_need_btn)).perform(click());
        }
        catch(NoMatchingViewException e){

            passed = true;
        }

        assertEquals(true,passed);

    }

    @Test
    public void userCanOnlyAddOneNeed(){

        String description = "01234567890";

        onView(withId(R.id.validity_txt)).perform(typeText("1"));
        onView(withId(R.id.descr_txt)).perform(typeText(description)).perform(closeSoftKeyboard());
        onView(withId(R.id.nbPeople_txt)).perform(typeText("1")).perform(closeSoftKeyboard());

        onView(withId(R.id.create_btn)).perform(click());

        // We should be on the mapsActivity layout
        onView(withId(R.id.create_need_btn)).perform(click());

        //Since a need is already created we should stay on the mapsActivity layout
        boolean passed = false;

        try{
            onView(withId(R.id.create_btn)).perform(click());
        }
        catch(NoMatchingViewException e){
            passed = true;
        }

        assertEquals(true,passed);
    }

    @Test
    public void invalidDescription(){


        StringBuilder sb = new StringBuilder();

        for(int i = 1; i < AddNeedActivity.MIN_DESCR_L; ++i){

            sb.append('a');

        }

        onView(withId(R.id.validity_txt)).perform(typeText(((AddNeedActivity.MAX_VALIDITY + AddNeedActivity.MIN_VALIDITY)/2)+"")).perform(closeSoftKeyboard());
        onView(withId(R.id.descr_txt)).perform(typeText(sb.toString())).perform(closeSoftKeyboard());

        onView(withId(R.id.create_btn)).perform(click());

        boolean passed = false;

        try{
            onView(withId(R.id.create_need_btn)).perform(click());
        }
        catch(NoMatchingViewException e){

            passed = true;
        }

        assertEquals(true,passed);

    }

    @Test
    public void canAccessActivityAndClickOnMap() {

        onView(withId(R.id.choose_loc_btn)).perform(click());

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
