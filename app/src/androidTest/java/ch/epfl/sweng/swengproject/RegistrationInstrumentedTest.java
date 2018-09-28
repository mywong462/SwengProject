package ch.epfl.sweng.swengproject;

import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;

import android.support.test.rule.ActivityTestRule;


@RunWith(AndroidJUnit4.class)
public class RegistrationInstrumentedTest {



    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void testUserCanRegister() throws InterruptedException {

        String mail = "exemple"+Math.random()+"@hotmail.com";
        String pswd = "exemple"+Math.random();

        onView(withId(R.id.register_btn)).perform(click());
        onView(withId(R.id.password)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());

        FirebaseAuth testAuth = FirebaseAuth.getInstance();

       /* testAuth.signInWithEmailAndPassword(mail, pswd)
                .addOnCompleteListener(mActivityRule.getActivity(), new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        assertEquals(task.isSuccessful(),true);

                    }
                });
*/

     



    }



}
