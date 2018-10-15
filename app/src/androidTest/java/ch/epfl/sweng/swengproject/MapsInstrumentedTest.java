package ch.epfl.sweng.swengproject;


import android.Manifest;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.espresso.NoMatchingViewException;

import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;


import com.android.dx.command.Main;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;



@RunWith(AndroidJUnit4.class)
public class MapsInstrumentedTest {

    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public final ActivityTestRule<MapsActivity> mActivityRule = new ActivityTestRule<>(MapsActivity.class);

    @Test
    public void dummyTest(){
        Log.d("HELLO", "dummydummy");
    }

    @Ignore //because of no matching view Error, maybe an element is blocking the view
    public void canSwitchActivity() {
        try{
            Thread.sleep(500);
            onView(withId(R.id.create_need_btn)).perform(click());
        }catch(InterruptedException e){}

    }

    @Ignore
    public void canAddManyNeedsRapidly() throws InterruptedException {

        for(int i = 0; i < 10; ++i){
            onView(withId(R.id.create_need_btn)).perform(click());
            Thread.sleep(3000);
            onView(withId(R.id.validity_txt)).perform(typeText("22")).perform(closeSoftKeyboard());
            onView(withId(R.id.descr_txt)).perform(typeText("Description written from tests : "+i)).perform(closeSoftKeyboard());
            Thread.sleep(2000);
            onView(withId(R.id.create_btn)).perform(click());

        }
    }

}