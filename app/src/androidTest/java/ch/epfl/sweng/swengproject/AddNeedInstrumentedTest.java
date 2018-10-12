package ch.epfl.sweng.swengproject;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.action.ViewActions.typeText;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;


@RunWith(AndroidJUnit4.class)
public class AddNeedInstrumentedTest {


    @Rule
    public final ActivityTestRule<AddNeedActivity> mActivityRule =
            new ActivityTestRule<>(AddNeedActivity.class,false,false);

    @Before
    public void create() throws Throwable {

        LocationServer ls = new FakeLocation();
        mActivityRule.launchActivity(new Intent().putExtra("loc",ls));

    }

    @Test //Pass locally but not with Travis
    public void invalidValidity1(){


        int failNumber = (int)Math.floor(Math.random()*(AddNeedActivity.MIN_VALIDITY));


        StringBuilder sb = new StringBuilder();

        for(int i = 1; i < AddNeedActivity.MIN_DESCR_L; ++i){

            sb.append('a');

        }

       // onView(withId(R.id.create_need_btn)).perform(click());
        //FloatingActionButton b = mActivityRule.getActivity().findViewById(R.id.create_need_btn);
        //assertTrue(b.performClick());

        onView(withId(R.id.validity_txt)).perform(typeText(""+failNumber)).perform(closeSoftKeyboard());
        //EditText t = mActivityRule.getActivity().findViewById(R.id.validity_txt);
        //t.setText(failNumber);
        onView(withId(R.id.descr_txt)).perform(typeText(sb.toString())).perform(closeSoftKeyboard());
        //EditText t2 = mActivityRule.getActivity().findViewById(R.id.descr_txt);
       // t2.setText(sb.toString());

         onView(withId(R.id.create_btn)).perform(click());
        //Button b2 = mActivityRule.getActivity().findViewById(R.id.create_btn);
       // assertTrue(b2.performClick());

        boolean passed = false;

        try{
            onView(withId(R.id.create_need_btn)).perform(click());
        }
        catch(NoMatchingViewException e){

            passed = true;
        }

        assertEquals(true,passed);

    }

}
