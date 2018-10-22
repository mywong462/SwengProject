package ch.epfl.sweng.swengproject;

import android.support.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.mock;

public class LoginClickInstrumented {



    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);


    @Test
    public  void canClick(){

        FirebaseAuth mock = mock(FirebaseAuth.class);
        mActivityRule.getActivity().setAuth(mock);
        onView(withId(R.id.login_btn1)).perform(click());
    }


}
