package ch.epfl.sweng.swengproject;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RegistrationInstrumentedTest {


    @Rule
    public final ActivityTestRule<RegistrationActivity> mActivityRule = new ActivityTestRule<>(RegistrationActivity.class);

    @Test
    public void testCanGreetUsers() {
        onView(withId(R.id.register_btn)).perform(click());
        onView(withId(R.id.password)).perform(typeText("exemplepassword")).perform(closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText("exemple@epfl.ch")).perform(closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());


    }



}
