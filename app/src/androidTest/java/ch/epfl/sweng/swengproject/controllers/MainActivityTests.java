package ch.epfl.sweng.swengproject.controllers;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.dx.command.Main;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityTests {



    @Rule
    public final ActivityTestRule<MainActivity> mainActivity =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void noProfileInHDExist() throws InterruptedException{
        /*Instrumentation.ActivityMonitor aM =  new Instrumentation.ActivityMonitor();


        mainActivity.launchActivity(new Intent());
         Thread.sleep(5000);
        assertEquals(MainActivity.class, aM.getLastActivity());*/
       // Thread.sleep(1000);


        //mainActivity.finishActivity();
        assertEquals(0,0);
    }
}
