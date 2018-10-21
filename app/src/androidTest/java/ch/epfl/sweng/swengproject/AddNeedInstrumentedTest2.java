package ch.epfl.sweng.swengproject;

import android.os.Looper;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class AddNeedInstrumentedTest2 {

    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public final ActivityTestRule<AddNeedActivity> mActivityRule =
            new ActivityTestRule<>(AddNeedActivity.class,false,true);


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
        //AddNeedActivity addNeedActivity = new AddNeedActivity(true);

        onView(withId(R.id.descr_txt)).perform(typeText("abcdefghijkl")).perform(closeSoftKeyboard());
        onView(withId(R.id.validity_txt)).perform(typeText("0")).perform(closeSoftKeyboard());
        onView(withId(R.id.nbPeople_txt)).perform(typeText("1")).perform(closeSoftKeyboard());

        onView(withId(R.id.create_btn)).perform(click());
    }

    @Test
    public void createWithInvalidValidity2(){
        //Looper.prepare();
        //AddNeedActivity addNeedActivity = new AddNeedActivity(true);

        onView(withId(R.id.descr_txt)).perform(typeText("abcdefghijkl")).perform(closeSoftKeyboard());
        onView(withId(R.id.validity_txt)).perform(typeText("a")).perform(closeSoftKeyboard());
        onView(withId(R.id.nbPeople_txt)).perform(typeText("1")).perform(closeSoftKeyboard());

        onView(withId(R.id.create_btn)).perform(click());
    }
}
