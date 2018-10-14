package ch.epfl.sweng.swengproject;

import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CurrentLocationInstrumentedTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    private UiDevice mDevice;

    @Before
    public void before(){
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        assertNotNull(mDevice);
    }

    @Ignore
    public void setCurrentLocationParametersValidTest(){
        mActivityRule.getActivity().currentLocation.setCurrentLocationParameters(mActivityRule.getActivity(), mActivityRule.getActivity());
    }

    @Ignore
    public void setCurrentLocationParametersInvalidTest1() {
        try {
            mActivityRule.getActivity().currentLocation.setCurrentLocationParameters(null, mActivityRule.getActivity());
        }catch(NullPointerException e){
            return;
        }
        fail();
    }

    @Ignore
    public void setCurrentLocationParametersInvalidTest2() {
        try {
            mActivityRule.getActivity().currentLocation.setCurrentLocationParameters(mActivityRule.getActivity(), null);
        }catch(NullPointerException e){
            return;
        }
        fail();
    }
}
