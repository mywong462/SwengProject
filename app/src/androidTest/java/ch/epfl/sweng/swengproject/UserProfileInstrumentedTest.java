package ch.epfl.sweng.swengproject;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.support.test.rule.ActivityTestRule;

import junit.framework.AssertionFailedError;

import java.util.List;

import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.storage.db.UserDao;
import ch.epfl.sweng.swengproject.util.UserTestUtil;

// Test is useless. Will wait for mock class to then mock sharedPreferences since
// the file can't be accessed from androidTest
@RunWith(AndroidJUnit4.class)
public class UserProfileInstrumentedTest {

    @Rule
    public final ActivityTestRule<UserProfileActivity> mActivityRule =
            new ActivityTestRule<>(UserProfileActivity.class);


    @Test
    public void testUserProfilePromt() {

        // On first login, user should be prompted to add its user info

        onView(withId(R.id.userName1)).perform(typeText("username")).perform(closeSoftKeyboard());
        onView(withId(R.id.firstName1)).perform(typeText("name")).perform(closeSoftKeyboard());
        onView(withId(R.id.lastName1)).perform(typeText("last name")).perform(closeSoftKeyboard());
        onView(withId(R.id.save_btn1)).perform(click());


        onView(withId(R.id.map));

    }

    @Test(expected = NoMatchingViewException.class)
    public void wrongInput(){

        //direcly click on the button without entering any informations
        onView(withId(R.id.save_btn1)).perform(click());

        //then try to click on a button that is on the map activity => should launch an exception
        onView(withId(R.id.create_need_btn)).perform(click());
    }

}
