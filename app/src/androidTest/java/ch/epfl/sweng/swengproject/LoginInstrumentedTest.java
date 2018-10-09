package ch.epfl.sweng.swengproject;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.equalTo;

import android.support.test.rule.ActivityTestRule;

import junit.framework.AssertionFailedError;

@RunWith(AndroidJUnit4.class)
public class LoginInstrumentedTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Ignore
    public void testWrongPassword() throws InterruptedException {

        String mail = "claire.capelo@epfl.ch";
        String pswd = "notMyPassword";

        onView(withId(R.id.welcome_scr)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.email1)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.password1)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.login_btn1)).perform(click());
        Thread.sleep(3000);

        boolean passed = false;
        try {
            // should stay on login page because login failed
            onView(withId(R.id.login_scr)).check(matches(isDisplayed()));
            passed = true;
        } catch (AssertionFailedError e) {

        }

        assertEquals(true,passed);
    }

    @Test
    public void resetPassword() throws InterruptedException {

        String mail = "claire.capelo@epfl.ch";
        boolean passed = false;

        onView(withId(R.id.welcome_scr)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.resetPassword_btn1)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.email3)).perform(typeText(mail)).perform(closeSoftKeyboard());
        Thread.sleep(3000);

        try {
            // should stay on page
            onView(withId(R.id.reset_scr)).check(matches(isDisplayed()));
            passed = true;
        } catch (AssertionFailedError e) {

        }

        assertEquals(true, passed);
    }



}
