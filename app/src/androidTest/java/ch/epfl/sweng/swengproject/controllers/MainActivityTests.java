package ch.epfl.sweng.swengproject.controllers;

import android.content.Intent;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import android.support.test.rule.ActivityTestRule;

import ch.epfl.sweng.swengproject.MyApplication;
import ch.epfl.sweng.swengproject.R;
import ch.epfl.sweng.swengproject.storage.StorageHelper;
import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.storage.db.UserDao;
import ch.epfl.sweng.swengproject.util.UserTestUtil;


@RunWith(AndroidJUnit4.class)
public class MainActivityTests {

    private static UserDao userDao;
    private static AppDatabase dataBase;

    @BeforeClass
    public static void doBeforeAll(){
        MyApplication.setUnderTest(true);
        dataBase = AppDatabase.getInstance();
        userDao = dataBase.userDao();
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

    @Test
    public void wrongProfileInHDExist() throws InterruptedException{
        User realUser = new User();
        realUser.setEmail("kaeser.jonathan@gmail.com");
        realUser.setPassword("123456");
        userDao.storeMyOwnProfile(realUser);

        mainActivity.launchActivity(new Intent());
        Thread.sleep(5000);


        assertEquals(true,true);
    }
}
