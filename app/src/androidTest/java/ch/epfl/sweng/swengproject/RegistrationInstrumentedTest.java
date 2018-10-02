package ch.epfl.sweng.swengproject;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;

import android.support.test.rule.ActivityTestRule;


@RunWith(AndroidJUnit4.class)
public class RegistrationInstrumentedTest {



    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void testRandomRegisteredUser() throws InterruptedException {

        String mail = "exemple"+Math.random()+"@hotmail.com";
        String pswd = "exemple"+Math.random();

        onView(withId(R.id.welcome_scr)).perform(click());
        onView(withId(R.id.password2)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.email1)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.register_btn2)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.login_btn2)).perform(click());
        onView(withId(R.id.email1)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.password2)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.register_btn2)).perform(click());

        //will exit because email is not registered

    }

    @Test
    public void testWrongEmailInput(){

        String mail = "exemple"+Math.random();
        String pswd = "exemple"+Math.random();

        onView(withId(R.id.welcome_scr)).perform(click());
        onView(withId(R.id.register_btn2)).perform(click());
        onView(withId(R.id.password2)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.email1)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.register_btn2)).perform(click());

        boolean passed = false;

        try { //the activity should not have changed => login_btn isn't on the view and should return an error
            onView(withId(R.id.login_btn2)).perform(click());
        }
        catch (NoMatchingViewException e) {

            passed = true;
        }

        assertEquals(true,passed);

    }

    @Test
    public void testSmallPasswordInput(){

        String mail = "exemple"+Math.random();
        String pswd = "12345";

        onView(withId(R.id.welcome_scr)).perform(click());
        onView(withId(R.id.register_btn2)).perform(click());
        onView(withId(R.id.password2)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.email1)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.register_btn2)).perform(click());

        boolean passed = false;

        try { //the activity should not have changed => login_btn isn't on the view and should return an error
            onView(withId(R.id.login_btn2)).perform(click());
        }
        catch (NoMatchingViewException e) {

            passed = true;
        }

        assertEquals(true,passed);

    }

}