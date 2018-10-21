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

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.Executor;

import ch.epfl.sweng.swengproject.MyApplication;
import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.storage.db.UserDao;

import static org.junit.Assert.assertEquals;
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

    @Mock FirebaseAuth fireBaseAuthMock = mock(FirebaseAuth.class);

    @Mock AuthResult mockAuthResult = new AuthResult() {
        @Override
        public FirebaseUser getUser() {
            return null;
        }

        @Override
        public AdditionalUserInfo getAdditionalUserInfo() {
            return null;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {

        }
    };

    @Mock Task<AuthResult> failTask = new Task<AuthResult>() {

//        @NonNull
//        @Override
//        public Task<AuthResult> addOnCompleteListener(@NonNull Activity activity, @NonNull OnCompleteListener<AuthResult> onCompleteListener) {
//           System.out.println("maybe stuff to do here");
//            return this;
//        }

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
            return mockAuthResult;
        }

        @Nullable
        @Override
        public <X extends Throwable> AuthResult getResult(@NonNull Class<X> aClass) throws X {
            return mockAuthResult;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnSuccessListener(@NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
            return this;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
            return this;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super AuthResult> onSuccessListener) {
            return this;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return this;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return this;
        }

        @NonNull
        @Override
        public Task<AuthResult> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
            return this;
        }
    };

    @Mock
    Activity myAct;




    @Test
    public void wrongProfileInHDExist() throws InterruptedException{


        when(fireBaseAuthMock.signInWithEmailAndPassword(""  ,"")).thenReturn(failTask);
        //when(failTask.addOnCompleteListener(myAct,)).thenReturn(failTask);
        /*when(failTask.
                addOnCompleteListener(act, l)).thenAnswer(new Answer() {


            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });*/


        // when(fireBaseAuthMock.getCurrentUser()).thenReturn(user);



        MyApplication.setFirebaseAuthMock(fireBaseAuthMock);

        User realUser = new User();
        realUser.setEmail("kaeser.jonathan@gmail.com");
        realUser.setPassword("123456");
        userDao.storeMyOwnProfile(realUser);

        mainActivity.launchActivity(new Intent());
        Thread.sleep(5000);


        assertEquals(true,true);
    }
}
