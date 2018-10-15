package ch.epfl.sweng.swengproject;

import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class MapsInstrumentedTest {

    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


    @Rule
    public final ActivityTestRule<MapsActivity> mActivityRule =
            new ActivityTestRule<>(MapsActivity.class);


    @Test
    public void dummyTest(){

        return;
    }

    @Test
    public void canSwitchActivity() {

        onView(withId(R.id.create_need_btn)).perform(click());
    }

}