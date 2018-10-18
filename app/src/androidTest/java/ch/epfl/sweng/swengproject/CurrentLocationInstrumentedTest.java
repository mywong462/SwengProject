package ch.epfl.sweng.swengproject;

import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
public class CurrentLocationInstrumentedTest {


    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void setCurrentLocationParametersValidTest(){
        MyApplication.currentLocation.setCurrentLocationParameters(mActivityRule.getActivity(), mActivityRule.getActivity());
    }

    @Test
    public void setCurrentLocationParametersInvalidTest1() {
        try {
            MyApplication.currentLocation.setCurrentLocationParameters(null, mActivityRule.getActivity());
        }catch(NullPointerException e){
            return;
        }
        fail();
    }

    @Test
    public void setCurrentLocationParametersInvalidTest2() {
        try {
            MyApplication.currentLocation.setCurrentLocationParameters(mActivityRule.getActivity(), null);
        }catch(NullPointerException e){
            return;
        }
        fail();
    }

}