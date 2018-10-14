package ch.epfl.sweng.swengproject;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
public class CurrentLocationInstrumentedTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void setCurrentLocationParametersValidTest(){
        mActivityRule.getActivity().currentLocation.setCurrentLocationParameters(mActivityRule.getActivity(), mActivityRule.getActivity());
    }

    @Test
    public void setCurrentLocationParametersInvalidTest1() {
        try {
            mActivityRule.getActivity().currentLocation.setCurrentLocationParameters(null, mActivityRule.getActivity());
        }catch(NullPointerException e){
            return;
        }
        fail();
    }

    @Test
    public void setCurrentLocationParametersInvalidTest2() {
        try {
            mActivityRule.getActivity().currentLocation.setCurrentLocationParameters(mActivityRule.getActivity(), null);
        }catch(NullPointerException e){
            return;
        }
        fail();
    }
}
