package ch.epfl.sweng.swengproject;


import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.concurrent.Executor;


@RunWith(AndroidJUnit4.class)
public class RegistrationInstrumentedTest {

    private final String mailName = "example";
    private final String mailDomain = "@hotmail.com";

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test(expected = PerformException.class)
    public void testRandomRegisteredUser() throws InterruptedException {

        String mail = mailName +Math.random()+mailDomain;
        String pswd = mailName +Math.random();

        onView(withId(R.id.welcome_scr)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.register_btn1)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.email2)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.password2)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.register_btn2)).perform(click());

        //will exit bcause email is not registered
        Thread.sleep(3000);
    }

    @Test(expected = PerformException.class)
    public void testWrongEmailInput() throws InterruptedException {

        String mail = mailName +Math.random();  //the mail doesn't contains @something.domain
        String pswd = mailName +Math.random();

        onView(withId(R.id.welcome_scr)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.register_btn1)).perform(click());
        onView(withId(R.id.password2)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.email2)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.register_btn2)).perform(click());


        //the activity should not have changed => we shouldn't be on the welcome screen
            onView(withId(R.id.welcome_scr)).perform(click());

        Thread.sleep(1000);
    }

    @Test(expected = PerformException.class)
    public void testSmallPasswordInput() throws InterruptedException {

        String mail = mailName +Math.random();
        String pswd = "12345";

        onView(withId(R.id.welcome_scr)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.register_btn1)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.password2)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.email2)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.register_btn2)).perform(click());


        //the activity should not have changed => we shouldn't be on the welcome screen
            onView(withId(R.id.welcome_scr)).perform(click());

        Thread.sleep(1000);
    }

    @Ignore
    public void testSameEmailAddress() throws InterruptedException {

        String mail = mailName +Math.random()+mailDomain;
        String pswd = "asdf"+Math.random();

        //first register an email address

        onView(withId(R.id.welcome_scr)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.register_btn1)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.password2)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.email2)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.register_btn2)).perform(click());
        Thread.sleep(3000);

        //Then try to register with the same email

        onView(withId(R.id.welcome_scr)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.register_btn1)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.password2)).perform(typeText("AnyPassword")).perform(closeSoftKeyboard());
        onView(withId(R.id.email2)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.register_btn2)).perform(click());


        boolean passed = false;

        try { //the activity should not have changed => login_btn isn't on the view and should return an error
            onView(withId(R.id.welcome_scr)).perform(click());

        }
        catch (NoMatchingViewException e) {

            passed = true;
        }

        assertEquals(true,passed);
        Thread.sleep(1000);
    }



    //Mocking the database part
    @Mock
    private Task<AuthResult> taskAuthNotSucc = new Task<AuthResult>() {
        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Nullable
        @Override
        public AuthResult getResult() {
            return null;
        }

        @Nullable
        @Override
        public <X extends Throwable> AuthResult getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnSuccessListener(@NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };

    @Mock
    private ConnectivityManager connMan = mock(ConnectivityManager.class);

    @Mock
    private NetworkInfo netInfo = mock(NetworkInfo.class);

    @Test
    public void testCreateUserNotSuccTaskNotFromNetwork(){
        RegistrationActivity regAct = new RegistrationActivity();
        regAct.setTest(true);

        when(connMan.getActiveNetworkInfo()).thenReturn(netInfo);
        when(netInfo.isConnected()).thenReturn(false);

        regAct.createUserEmailPass(taskAuthNotSucc);
    }


    @Test
    public void testCreateUserNotSuccTaskFromNetwork(){
        RegistrationActivity regAct = new RegistrationActivity();
        regAct.setTest(true);

        when(connMan.getActiveNetworkInfo()).thenReturn(netInfo);
        when(netInfo.isConnected()).thenReturn(true);

        regAct.createUserEmailPass(taskAuthNotSucc);
    }

}