package ch.epfl.sweng.swengproject;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class RegistrationClickInstrumented {

    @Rule
    public final ActivityTestRule<RegistrationActivity> mActivityRule =
            new ActivityTestRule<>(RegistrationActivity.class);


    @Before

    public void init(){
        mActivityRule.getActivity().setTestMode();
    }


    @Test
    public void canClick(){

        onView(withId(R.id.register_btn2)).perform(click());

    }

}
