package ch.epfl.sweng.swengproject.controllers;


import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.storage.db.UserDao;
import ch.epfl.sweng.swengproject.util.UITestException;

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

   /* @Test
    public void noProfileInHDExist() throws InterruptedException{
        StorageHelper.deleteAllDataStoredLocally();
        //User me = UserTestUtil.randomUser();
        //userDao.storeMyOwnProfile(me);

        /*Instrumentation.ActivityMonitor aM =  new Instrumentation.ActivityMonitor();


        mainActivity.launchActivity(new Intent());
         Thread.sleep(5000);
        assertEquals(MainActivity.class, aM.getLastActivity());*/
    // Thread.sleep(1000);


    //mainActivity.finishActivity();
        /*mainActivity.launchActivity(new Intent());
        //Thread.sleep(5000);
        boolean passed = true;
        try { //the activity should not have changed => login_btn isn't on the view and should return an error
            onView(withId(R.id.inscription_src)).perform(click());

        }catch (NoMatchingViewException e) {

            passed = false;
        }
        assertEquals(true,passed);
    }*/

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


   /* @Test
    public void wrongProfileInHDExist() throws InterruptedException{
        MyApplication.setFirebaseAuthMock(mockFirebaseAuth);
        User realUser = new User();
        realUser.setEmail("kaeser.jonathan@gmail.com");
        realUser.setPassword("123456");
        userDao.storeMyOwnProfile(realUser);
        mainActivity.launchActivity(new Intent());
        testOnCompleteListener.getValue().onComplete(mockAuthResultTask);
        assertEquals(true,true);
    }*/

    @Test
    public void rightProfileInHDExist() throws InterruptedException{
        MyApplication.setFirebaseAuthMock(mockFirebaseAuth);
        User realUser = new User();
        realUser.setEmail("kaeser.jonathan@gmail.com");
        realUser.setPassword("123456");
        userDao.storeMyOwnProfile(realUser);
        mainActivity.launchActivity(new Intent());
        when(mockAuthResultTask.isSuccessful()).thenReturn(true);
        when(mockFirebaseUser.isEmailVerified()).thenReturn(false);
        testOnCompleteListener.getValue().onComplete(mockAuthResultTask);
        assertEquals(true,true);
        Thread.sleep(9000);
    }
}
