package ch.epfl.sweng.swengproject;

import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swengproject.util.FakeLocation;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.action.ViewActions.typeText;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
public class ChooseLocationInstrumentedTest {

    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public final ActivityTestRule<ChooseLocationActivity> aActivityRule =
            new ActivityTestRule<>(ChooseLocationActivity.class,false,false);

    @Rule
    public final ActivityTestRule<AddNeedActivity> bActivityRule =
            new ActivityTestRule<>(AddNeedActivity.class,false,false);

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void create(){
        //final CurrentLocation currentLocation = new CurrentLocation();
        bActivityRule.launchActivity(new Intent());
    }

    @Test
    public void activityFlowTest() {
        onView(withId(R.id.choose_loc_btn)).perform(click());
        onView(withId(R.id.map_ch_loc)).check(matches(isDisplayed()));
    }

    // without dragging the map, the default location should be close enough and we should
    // go back to the AddNeedActivity
    @Test
    public void defaultLocationTest() {
        onView(withId(R.id.choose_loc_btn)).perform(click());
        onView(withId(R.id.set_loc_btn)).perform(click());
        onView(withId(R.id.choose_loc_btn)).perform(click());

    }
}
