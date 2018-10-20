package ch.epfl.sweng.swengproject;

import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swengproject.util.FakeLocation;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.action.ViewActions.typeText;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class ChooseLocationInstrumentedTest {

    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public final ActivityTestRule<ChooseLocationActivity> aActivityRule =
            new ActivityTestRule<>(ChooseLocationActivity.class,false,false);


    @Before
    public void setMock(){

        LocationServer ls = new FakeLocation();

        //inject the mocked object in the activity
        aActivityRule.launchActivity(new Intent().putExtra("loc",ls));
    }



    @Test
    public void canAccessAndReturnFromActivity() {

        onView(withId(R.id.map_ch_loc)).perform(click());


    }


    @Test
    public void NotTooFar(){


       assertFalse(aActivityRule.getActivity().locationTooFar());

    }

    @Test
    public void canClick(){

        onView(withId(R.id.set_loc_btn)).perform(click());
    }

    @Test
    public void canSetDefaultLoc(){

        aActivityRule.getActivity().setDefaultLocation();

    }


    /*
    @Before
    public void create(){

        //LocationServer ls = new FakeLocation();

        //inject the mocked object in the activity
        //bActivityRule.launchActivity(new Intent().putExtra("loc",ls));
        //bActivityRule.launchActivity(new Intent());
    }*/


    @Ignore
    public void activityFlowTest() {
        try{
            onView(withId(R.id.choose_loc_btn)).perform(click());
            Thread.sleep(2000);
            onView(withId(R.id.map_ch_loc)).check(matches(isDisplayed()));
        }catch(InterruptedException e){
            fail();
        }

    }

    // without dragging the map, the default location should be close enough and we should
    // go back to the AddNeedActivity
    @Ignore
    public void defaultLocationTest() {
        onView(withId(R.id.choose_loc_btn)).perform(click());
        onView(withId(R.id.set_loc_btn)).perform(click());
        onView(withId(R.id.choose_loc_btn)).perform(click());

    }
}
