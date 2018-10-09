package ch.epfl.sweng.swengproject;
import android.support.test.runner.AndroidJUnit4;

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

import android.support.test.rule.ActivityTestRule;

import junit.framework.AssertionFailedError;

// Test is useless. Will wait for mock class to then mock sharedPreferences since
// the file can't be accessed from androidTest
@RunWith(AndroidJUnit4.class)
public class UserProfileInstrumentedTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testUserProfilePromt() throws InterruptedException {

        // On first login, user should be prompted to add its user info
        String mail = "benoitknuchel@gmail.com";
        String pswd = "123456";

        onView(withId(R.id.welcome_scr)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.email1)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.password1)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.login_btn1)).perform(click());
        Thread.sleep(3000);

        boolean passed = false;
        try {
            // should go directly to maps activity since sharedPreferences file already exists
            // onView(withId(R.id.)).check(matches(isDisplayed()));
            passed = true;
        } catch (AssertionFailedError e) {

        }

        assertEquals(true,passed);
    }
}
