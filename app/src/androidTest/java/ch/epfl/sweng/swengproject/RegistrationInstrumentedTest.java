package ch.epfl.sweng.swengproject;


import android.support.test.espresso.NoMatchingViewException;
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
import static junit.framework.TestCase.assertEquals;
import android.support.test.rule.ActivityTestRule;

import ch.epfl.sweng.swengproject.controllers.MainActivity;


@RunWith(AndroidJUnit4.class)
public class RegistrationInstrumentedTest {

    private final String mailName = "example";
    private final String mailDomain = "@hotmail.com";

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testRandomRegisteredUser() throws InterruptedException {

        String mail = mailName +Math.random()+mailDomain;
        String pswd = mailName +Math.random();

        onView(withId(R.id.welcome_scr)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.register_btn1)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.email2)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.password2)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.register_btn2)).perform(click());

        //will exit because email is not registered
        Thread.sleep(3000);
    }

    @Test(expected = NoMatchingViewException.class)
    public void testWrongEmailInput() throws InterruptedException {

        String mail = mailName +Math.random();  //the mail doesn't contains @something.domain
        String pswd = mailName +Math.random();

        onView(withId(R.id.welcome_scr)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.register_btn1)).perform(click());
        onView(withId(R.id.password2)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.email2)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.register_btn2)).perform(click());


        //the activity should not have changed => we shouldn't be on the welcome screen
            onView(withId(R.id.welcome_scr)).perform(click());

        Thread.sleep(1000);
    }

    @Test(expected = NoMatchingViewException.class)
    public void testSmallPasswordInput() throws InterruptedException {

        String mail = mailName +Math.random();
        String pswd = "12345";

        onView(withId(R.id.welcome_scr)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.register_btn1)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.password2)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.email2)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.register_btn2)).perform(click());


        //the activity should not have changed => we shouldn't be on the welcome screen
            onView(withId(R.id.welcome_scr)).perform(click());

        Thread.sleep(1000);
    }

    @Ignore
    public void testSameEmailAddress() throws InterruptedException {

        String mail = mailName +Math.random()+mailDomain;
        String pswd = "asdf"+Math.random();

        //first register an email address

        onView(withId(R.id.welcome_scr)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.register_btn1)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.password2)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.email2)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.register_btn2)).perform(click());
        Thread.sleep(3000);

        //Then try to register with the same email

        onView(withId(R.id.welcome_scr)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.register_btn1)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.password2)).perform(typeText("AnyPassword")).perform(closeSoftKeyboard());
        onView(withId(R.id.email2)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.register_btn2)).perform(click());


        boolean passed = false;

        try { //the activity should not have changed => login_btn isn't on the view and should return an error
            onView(withId(R.id.welcome_scr)).perform(click());

        }
        catch (NoMatchingViewException e) {

            passed = true;
        }

        assertEquals(true,passed);
        Thread.sleep(1000);
    }

}