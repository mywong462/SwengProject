
package ch.epfl.sweng.swengproject;

import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import android.widget.SeekBar;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swengproject.util.FakeLocation;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


@RunWith(AndroidJUnit4.class)
public class MapFilterInstrumentedTest {

    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public final ActivityTestRule<MapsActivity> mActivityRule =
            new ActivityTestRule<>(MapsActivity.class,false,false);

    @Before
    public void create(){

        LocationServer ls = new FakeLocation();

        //inject the mocked object in the activity
        mActivityRule.launchActivity(new Intent().putExtra("loc",ls));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }


    @Test
    public void openFilterMenu(){

        onView(withId(R.id.map_filter_btn)).perform(click());

    }
    /*
    @Test
    public void checkRangeSliderWork() {
        onView(withId(R.id.rangeBar)).perform(setProgress(100));
    }

    @Test
    public void checkCatButtonWork(){

        onView(withId(R.id.map_filter_btn)).perform(click());
        onView(withId(R.id.catSpinner)).perform(click());
        //onData(childAtPosition(allOf(withId(R.id.dropDownItems),withParent(withClassName(is("android.widget.DropDownListView")))),0));
        //onData(childAtPosition(allOf(withId(R.id.checkbox),withParent(childAtPosition(allOf(withId(R.id.dropDownItems),withParent(withClassName(is("android.widget.DropDownListView")))),0))),0)).perform(click());
        //onView(withTagValue(is((Object)"ch.epfl.sweng.swengproject.SpinnerAdapter$ViewHolder@6cfacff"))).perform(click());
        /*
        onData(withId(R.id.checkbox))
                .inAdapterView(childAtPosition(allOf(withId(R.id.dropDownItems),withParent(withClassName(is("android.widget.DropDownListView")))),1))
                .perform(click());

        ViewInteraction checkBox = onView(
                allOf(withId(R.id.checkbox),
                        childAtPosition(
                                allOf(withId(R.id.dropDownItems),
                                        withParent(withClassName(is("android.widget.DropDownListView")))),
                                0),
                        isDisplayed()));
        checkBox.perform(click());

        ViewInteraction checkBox2 = onView(
                allOf(withId(R.id.checkbox),
                        childAtPosition(
                                allOf(withId(R.id.dropDownItems),
                                        withParent(withClassName(is("android.widget.DropDownListView")))),
                                0),
                        isDisplayed()));
        checkBox2.perform(click());

        ViewInteraction checkBox3 = onView(
                allOf(withId(R.id.checkbox),
                        childAtPosition(
                                allOf(withId(R.id.dropDownItems),
                                        withParent(withClassName(is("android.widget.DropDownListView")))),
                                0),
                        isDisplayed()));
        checkBox3.perform(click());
        */

/*




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

        //onView(withId(R.id.map_ch_loc)).perform(click());

    }

*/

}


