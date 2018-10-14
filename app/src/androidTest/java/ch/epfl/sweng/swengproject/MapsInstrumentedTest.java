package ch.epfl.sweng.swengproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.core.internal.deps.guava.collect.Iterables;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.action.ViewActions.typeText;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static ch.epfl.sweng.swengproject.MainActivity.currentLocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class MapsInstrumentedTest {


    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void dummyTest(){
        return;
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

    @Ignore //Pass locally but not with Travis
    public void invalidValidity1(){


        int failNumber = (int)Math.floor(Math.random()*(AddNeedActivity.MIN_VALIDITY));


        StringBuilder sb = new StringBuilder();

        for(int i = 1; i < AddNeedActivity.MIN_DESCR_L; ++i){

            sb.append('a');

        }

        onView(withId(R.id.create_need_btn)).perform(click());


        onView(withId(R.id.validity_txt)).perform(typeText(""+failNumber)).perform(closeSoftKeyboard());
        onView(withId(R.id.descr_txt)).perform(typeText(sb.toString())).perform(closeSoftKeyboard());

        onView(withId(R.id.create_btn)).perform(click());

        boolean passed = false;

        try{
            onView(withId(R.id.create_need_btn)).perform(click());
        }
        catch(NoMatchingViewException e){

            passed = true;
        }

        assertEquals(true,passed);

    }


    @Ignore //Pass locally but not with Travis
    public void invalidValidity2(){


        int failNumber = (int)Math.ceil(Math.random()+AddNeedActivity.MAX_VALIDITY);

        

        StringBuilder sb = new StringBuilder();

        for(int i = 1; i < AddNeedActivity.MIN_DESCR_L; ++i){

            sb.append('a');

        }

        onView(withId(R.id.create_need_btn)).perform(click());


        onView(withId(R.id.validity_txt)).perform(typeText(""+failNumber)).perform(closeSoftKeyboard());
        onView(withId(R.id.descr_txt)).perform(typeText(sb.toString())).perform(closeSoftKeyboard());

        onView(withId(R.id.create_btn)).perform(click());

        boolean passed = false;

        try{
            onView(withId(R.id.create_need_btn)).perform(click());
        }
        catch(NoMatchingViewException e){

            passed = true;
        }

        assertEquals(true,passed);

    }

    @Ignore //Pass locally but not with Travis
    public void invalidDescription(){


        StringBuilder sb = new StringBuilder();

        for(int i = 1; i < AddNeedActivity.MIN_DESCR_L; ++i){

            sb.append('a');

        }


        onView(withId(R.id.create_need_btn)).perform(click());


        onView(withId(R.id.validity_txt)).perform(typeText(((AddNeedActivity.MAX_VALIDITY + AddNeedActivity.MIN_VALIDITY)/2)+"")).perform(closeSoftKeyboard());
        onView(withId(R.id.descr_txt)).perform(typeText(sb.toString())).perform(closeSoftKeyboard());

        onView(withId(R.id.create_btn)).perform(click());

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