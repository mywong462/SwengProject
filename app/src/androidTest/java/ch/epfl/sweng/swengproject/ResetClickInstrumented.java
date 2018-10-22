package ch.epfl.sweng.swengproject;

import android.support.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.mock;

public class ResetClickInstrumented {



    @Rule
    public final ActivityTestRule<ResetPasswordActivity> mActivityRule = new ActivityTestRule<>(ResetPasswordActivity.class);




    @Test
    public void onClickWork(){


        String email = "";

        FirebaseAuth mockF = mock(FirebaseAuth.class);

        mActivityRule.getActivity().setAuth(mockF);

        onView(withId(R.id.email3)).perform(typeText(email));

        onView(withId(R.id.resetPassword_btn3)).perform(click());

    }

}
