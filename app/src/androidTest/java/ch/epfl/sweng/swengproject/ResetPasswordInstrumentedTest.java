package ch.epfl.sweng.swengproject;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.Executor;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResetPasswordInstrumentedTest {


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
