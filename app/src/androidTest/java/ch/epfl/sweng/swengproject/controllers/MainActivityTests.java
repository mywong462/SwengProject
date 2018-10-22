package ch.epfl.sweng.swengproject.controllers;


import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeResult;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.Executor;

import ch.epfl.sweng.swengproject.MyApplication;
import ch.epfl.sweng.swengproject.R;
import ch.epfl.sweng.swengproject.storage.StorageHelper;
import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.storage.db.UserDao;
import ch.epfl.sweng.swengproject.util.UITestException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MainActivityTests {

    private static UserDao userDao;

    @BeforeClass
    public static void doBeforeAll(){
        MyApplication.setUnderTest(true);
        userDao = AppDatabase.getInstance().userDao();
    }

    @Rule
    public final ActivityTestRule<MainActivity> mainActivity =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Mock
    private FirebaseAuth mockFirebaseAuth;

    @Mock
    private Task<AuthResult> mockAuthResultTask;

    @Captor
    private ArgumentCaptor<OnCompleteListener> testOnCompleteListener;

    @Mock
    private FirebaseUser mockFirebaseUser;



    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);

        testOnCompleteListener = ArgumentCaptor.forClass(OnCompleteListener.class);

        when(mockFirebaseAuth.signInWithEmailAndPassword(any(String.class) , any(String.class)))
                .thenReturn(mockAuthResultTask);

        when(mockAuthResultTask.addOnCompleteListener(any(Activity.class), testOnCompleteListener.capture())).thenReturn(mockAuthResultTask);

        when(mockAuthResultTask.getException()).thenReturn(new UITestException());

        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
    }


    @Test
    public void noProfileInHDExist(){
        StorageHelper.deleteAllDataStoredLocally();

        mainActivity.launchActivity(new Intent());

        //check that inscription activity is displayed by clicking on it
        onView(withId(R.id.inscription_src)).perform(click());
    }


    @Test
    public void authDoesNotSucceed(){

        MyApplication.setFirebaseAuthMock(mockFirebaseAuth);

        User realUser = new User();
        realUser.setEmail("kaeser.jonathan@gmail.com");
        realUser.setPassword("123456");

        userDao.storeMyOwnProfile(realUser);

        mainActivity.launchActivity(new Intent());

        testOnCompleteListener.getValue().onComplete(mockAuthResultTask);

        //check that inscription activity is displayed by clicking on it
        onView(withId(R.id.inscription_src)).perform(click());
    }

    @Test
    public void authSucceedButUserNotVerified() throws InterruptedException{

        MyApplication.setFirebaseAuthMock(mockFirebaseAuth);

        User realUser = new User();
        realUser.setEmail("kaeser.jonathan@gmail.com");
        realUser.setPassword("123456");

        userDao.storeMyOwnProfile(realUser);

        mainActivity.launchActivity(new Intent());

        when(mockAuthResultTask.isSuccessful()).thenReturn(true);

        when(mockFirebaseUser.isEmailVerified()).thenReturn(false);

        when(mockFirebaseUser.getEmail()).thenReturn(realUser.email());

        testOnCompleteListener.getValue().onComplete(mockAuthResultTask);

        //assert that we are in the login activity and email edit text is automatically filled
        onView(withId(R.id.login_activity_email_edit_text)).check(matches(withText(realUser.email())));
    }

    @Test
    public void authSucceedAndUserVerified() throws InterruptedException{

        MyApplication.setFirebaseAuthMock(mockFirebaseAuth);

        User realUser = new User();
        realUser.setEmail("kaeser.jonathan@gmail.com");
        realUser.setPassword("123456");

        userDao.storeMyOwnProfile(realUser);

        mainActivity.launchActivity(new Intent());

        when(mockAuthResultTask.isSuccessful()).thenReturn(true);

        when(mockFirebaseUser.isEmailVerified()).thenReturn(true);

        when(mockFirebaseUser.getEmail()).thenReturn(realUser.email());

        testOnCompleteListener.getValue().onComplete(mockAuthResultTask);

        //assert equal we are in maps activity
        onView(withId(R.id.map_activity_main_view)).perform(click());
    }
}
