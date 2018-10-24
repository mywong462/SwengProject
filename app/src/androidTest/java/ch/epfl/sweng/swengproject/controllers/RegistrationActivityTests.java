package ch.epfl.sweng.swengproject.controllers;

import android.app.Activity;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.epfl.sweng.swengproject.MyApplication;
import ch.epfl.sweng.swengproject.R;
import ch.epfl.sweng.swengproject.storage.StorageHelper;
import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.UserDao;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;



@RunWith(AndroidJUnit4.class)
public class RegistrationActivityTests {

    //TODO: CATCH THE TOAST WITH A TOAST MATCHER
    //TODO: AVOID THE PROBLEM WHEN GOOGLE PROPOSE TO SAVE THE EMAIL
    //TODO: TEST THE IMAGE PICKER

    private static UserDao userDao;

    @Rule
    public final ActivityTestRule<RegistrationActivity> activityTestRule =
            new ActivityTestRule<>(RegistrationActivity.class, false, false);

    @Mock
    private FirebaseAuth mockFirebaseAuth;

    @Mock
    private Task<AuthResult> mockAuthResultTask;

    @Mock
    private Task<Void> mockReloadTask;

    @Captor
    private ArgumentCaptor<OnCompleteListener> testOnCompleteListener;

    @Captor
    private ArgumentCaptor<OnSuccessListener> testOnSuccessListener;

    @Mock
    private FirebaseUser mockFirebaseUser;

    @BeforeClass
    public static void doBeforeAll(){
        MyApplication.setUnderTest(true);
        userDao = AppDatabase.getInstance().userDao();
    }

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);

        MyApplication.setFirebaseAuthMock(mockFirebaseAuth);

        testOnCompleteListener = ArgumentCaptor.forClass(OnCompleteListener.class);

        when(mockAuthResultTask.addOnCompleteListener(any(Activity.class), testOnCompleteListener.capture())).thenReturn(mockAuthResultTask);

        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);

        when(mockFirebaseUser.reload()).thenReturn(mockReloadTask);

        when(mockReloadTask.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(mockReloadTask);

    }

    @Test
    public void transitionToLoginActivityCorrect(){
        StorageHelper.deleteAllDataStoredLocally();

        String email = "monEmail@mondomaine.com";

        activityTestRule.launchActivity(new Intent());
        onView(withId(R.id.registration_activity_email)).perform(typeText(email)).perform(closeSoftKeyboard());
        onView(withId(R.id.registration_activity_go_to_login_activity)).perform(click());

        //check that inscription activity is displayed by clicking on it
        onView(withId(R.id.login_activity_email_edit_text)).check(matches(withText(email)));
    }

    @Test
    public void userWillGoToLoginBecauseEmailExist(){
        StorageHelper.deleteAllDataStoredLocally();

        String email = "cetEmailExistDeja@mondomaine.com";
        String pwd = "123456";

        when(mockFirebaseAuth.createUserWithEmailAndPassword(email, pwd)).thenReturn(mockAuthResultTask);
        when(mockAuthResultTask.isSuccessful()).thenReturn(false);
        //return mail exception
        activityTestRule.launchActivity(new Intent());
        onView(withId(R.id.registration_activity_email)).perform(typeText(email)).perform(closeSoftKeyboard());
        onView(withId(R.id.registration_activity_password)).perform(typeText(pwd)).perform(closeSoftKeyboard());
        onView(withId(R.id.registration_activity_register_button)).perform(click());

        when(mockAuthResultTask.getException()).thenReturn(new FirebaseAuthUserCollisionException("tadata","tadada"));
        testOnCompleteListener.getValue().onComplete(mockAuthResultTask);


        onView(withText("This email already exist in our database")).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.login_scr)).check(matches(isDisplayed()));
    }

    @Test
    public void userWillChangeHisEmailBecauseItExist(){
        StorageHelper.deleteAllDataStoredLocally();

        String email = "cetEmailExistDeja@mondomaine.com";
        String pwd = "123456";

        when(mockFirebaseAuth.createUserWithEmailAndPassword(email, pwd)).thenReturn(mockAuthResultTask);
        when(mockAuthResultTask.isSuccessful()).thenReturn(false);
        //return mail exception
        activityTestRule.launchActivity(new Intent());
        onView(withId(R.id.registration_activity_email)).perform(typeText(email)).perform(closeSoftKeyboard());
        onView(withId(R.id.registration_activity_password)).perform(typeText(pwd)).perform(closeSoftKeyboard());
        onView(withId(R.id.registration_activity_register_button)).perform(click());

        when(mockAuthResultTask.getException()).thenReturn(new FirebaseAuthUserCollisionException("tadata", "tadada"));
        testOnCompleteListener.getValue().onComplete(mockAuthResultTask);


        onView(withText("This email already exist in our database")).check(matches(isDisplayed()));

        onView(withId(android.R.id.button3)).perform(click());
        onView(withId(R.id.registration_activity_email)).perform(clearText())
                .perform(typeText("ohThatsMyEmail@email.com")).perform(closeSoftKeyboard());
    }

    //TODO: MAKE THIS TEST PASS! how to catch the toast ??
//    @Test
//    public void inscriptionFailedWithUnknownError() throws Throwable {
//        StorageHelper.deleteAllDataStoredLocally();
//
//        String email = "cetEmailExistDeja@mondomaine.com";
//        String pwd = "123456";
//
//        when(mockFirebaseAuth.createUserWithEmailAndPassword(email, pwd)).thenReturn(mockAuthResultTask);
//        when(mockAuthResultTask.isSuccessful()).thenReturn(false);
//        when(mockAuthResultTask.getException()).thenReturn(new UITestException());
//
//        //return mail exception
//        activityTestRule.launchActivity(new Intent());
//        onView(withId(R.id.registration_activity_email)).perform(typeText(email)).perform(closeSoftKeyboard());
//        onView(withId(R.id.registration_activity_password)).perform(typeText(pwd)).perform(closeSoftKeyboard());
//        onView(withId(R.id.registration_activity_register_button)).perform(click());
//
//        runOnUiThread(new Runnable() {
//            public void run() {
//                testOnCompleteListener.getValue().onComplete(mockAuthResultTask);
//
//            }
//        });
//        onView(withText("Helslo")).inRoot(new ToastMatcher())
//                .check(matches(isDisplayed()));
//    }

    @Test
    public void successfullInscriptionButUserChangeEmail(){
        StorageHelper.deleteAllDataStoredLocally();

        String email = "correctNouveauEmail@mondomaine.com";
        String pwd = "123456";

        when(mockFirebaseAuth.createUserWithEmailAndPassword(email, pwd)).thenReturn(mockAuthResultTask);
        when(mockAuthResultTask.isSuccessful()).thenReturn(true);
        //return mail exception
        activityTestRule.launchActivity(new Intent());
        onView(withId(R.id.registration_activity_email)).perform(typeText(email)).perform(closeSoftKeyboard());
        onView(withId(R.id.registration_activity_password)).perform(typeText(pwd)).perform(closeSoftKeyboard());
        onView(withId(R.id.registration_activity_register_button)).perform(click());

        testOnCompleteListener.getValue().onComplete(mockAuthResultTask);


        onView(withText("Please certify your email")).check(matches(isDisplayed()));

        onView(withId(android.R.id.button3)).perform(click());

        onView(withId(R.id.registration_activity_email)).perform(clearText())
                .perform(typeText("ohThatsMyEmail@email.com")).perform(closeSoftKeyboard());
    }

    @Test
    public void userDoNotVerifyHisEmail(){
        StorageHelper.deleteAllDataStoredLocally();

        String email = "correctNouveauEmail@mondomaine.com";
        String pwd = "123456";

        when(mockFirebaseAuth.createUserWithEmailAndPassword(email, pwd)).thenReturn(mockAuthResultTask);
        when(mockAuthResultTask.isSuccessful()).thenReturn(true);
        //return mail exception
        activityTestRule.launchActivity(new Intent());
        onView(withId(R.id.registration_activity_email)).perform(typeText(email)).perform(closeSoftKeyboard());
        onView(withId(R.id.registration_activity_password)).perform(typeText(pwd)).perform(closeSoftKeyboard());
        onView(withId(R.id.registration_activity_register_button)).perform(click());

        testOnCompleteListener.getValue().onComplete(mockAuthResultTask);


        onView(withText("Please certify your email")).check(matches(isDisplayed()));
        when(mockReloadTask.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(mockReloadTask);

        onView(withId(android.R.id.button1)).perform(click());

        testOnSuccessListener.getValue().onSuccess(null);

        //assert that the alertDialog pop uped again!
        onView(withText("Please certify your email")).check(matches(isDisplayed()));
    }

    @Test
    public void successFullInscriptionAndUserVerifyEmail(){
        StorageHelper.deleteAllDataStoredLocally();

        String email = "correctNouveauEmail@mondomaine.com";
        String pwd = "123456";

        when(mockFirebaseAuth.createUserWithEmailAndPassword(email, pwd)).thenReturn(mockAuthResultTask);
        when(mockAuthResultTask.isSuccessful()).thenReturn(true);
        //return mail exception
        activityTestRule.launchActivity(new Intent());
        onView(withId(R.id.registration_activity_email)).perform(typeText(email)).perform(closeSoftKeyboard());
        onView(withId(R.id.registration_activity_password)).perform(typeText(pwd)).perform(closeSoftKeyboard());
        onView(withId(R.id.registration_activity_register_button)).perform(click());

        testOnCompleteListener.getValue().onComplete(mockAuthResultTask);


        onView(withText("Please certify your email")).check(matches(isDisplayed()));
        when(mockReloadTask.addOnSuccessListener(testOnSuccessListener.capture())).thenReturn(mockReloadTask);

        onView(withId(android.R.id.button1)).perform(click());

        when(mockFirebaseUser.isEmailVerified()).thenReturn(true);

        testOnSuccessListener.getValue().onSuccess(null);

        //assert that we are in the mapView
        onView(withId(R.id.map_activity_main_view)).check(matches(isDisplayed()));
    }
}
