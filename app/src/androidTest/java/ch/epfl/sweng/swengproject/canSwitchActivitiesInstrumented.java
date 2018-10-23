package ch.epfl.sweng.swengproject;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class canSwitchActivitiesInstrumented {


    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void goFromOneActToAnother(){


        onView(withId(R.id.welcome_scr)).perform(click());

        onView(withId(R.id.register_btn1)).perform(click());

        onView(withId(R.id.login_btn2)).perform(click());

        onView(withId(R.id.register_btn1)).perform(click());

        onView(withId(R.id.resetPassword_btn2)).perform(click());

        onView(withId(R.id.goBack_btn)).perform(click());

    }


}
