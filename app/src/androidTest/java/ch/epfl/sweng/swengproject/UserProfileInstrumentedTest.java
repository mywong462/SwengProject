package ch.epfl.sweng.swengproject;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
import static org.junit.Assert.assertNotNull;

import android.support.test.rule.ActivityTestRule;

import junit.framework.AssertionFailedError;

import java.util.List;

import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.storage.db.UserDao;
import ch.epfl.sweng.swengproject.util.UserTestUtil;

// Test is useless. Will wait for mock class to then mock sharedPreferences since
// the file can't be accessed from androidTest
@RunWith(AndroidJUnit4.class)
public class UserProfileInstrumentedTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    @Test
    public void EmptyTest(){
    
    }

    @Ignore
    public void testUserProfilePromt() throws InterruptedException {

        // On first login, user should be prompted to add its user info
        String mail = "benoitknuchel@gmail.com";
        String pswd = "123456";

        onView(withId(R.id.welcome_scr)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.email1)).perform(typeText(mail)).perform(closeSoftKeyboard());
        onView(withId(R.id.password1)).perform(typeText(pswd)).perform(closeSoftKeyboard());
        onView(withId(R.id.login_btn1)).perform(click());
        Thread.sleep(3000);

        boolean passed = false;
        try {
            // should go directly to maps activity since sharedPreferences file already exists
            // onView(withId(R.id.)).check(matches(isDisplayed()));
            passed = true;
        } catch (AssertionFailedError e) {

        }

        assertEquals(true,passed);
    }

    @RunWith(AndroidJUnit4.class)

    public static class UserTests {

        //TODO: Test how the system handle the pictures of the users !


        private UserDao userDao;
        private AppDatabase dataBase;

        @Before
        public void createDb() {
            Context context = InstrumentationRegistry.getTargetContext();
            dataBase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
            userDao = dataBase.userDao();
            userDao.deleteAll();
        }

        @After
        public void closeDb() {
            dataBase.close();
        }


        @Test
        public void storeAndFetchAll() {
            List<User> usersToStore = UserTestUtil.randomUsers();
            userDao.deleteAll();
            userDao.insertUsers(usersToStore.toArray(new User[0]));


            List<User> fetchedUsers = userDao.getAll();
            Assert.assertEquals(usersToStore.size(), fetchedUsers.size());
        }

        @Test
        public void deleteAll() {
            List<User> usersToStore = UserTestUtil.randomUsers();
            userDao.deleteAll();
            userDao.insertUsers(usersToStore.toArray(new User[0]));
            userDao.deleteAll();
            List<User> fetchedUsers = userDao.getAll();
            Assert.assertEquals(0, fetchedUsers.size());
        }

        @Test
        public void loadByEmails() {
            List<User> usersToStore = UserTestUtil.randomUsers();
            userDao.deleteAll();
            userDao.insertUsers(usersToStore.toArray(new User[0]));

            for (int i = 0; i < 10; i++) {
                User u = usersToStore.get(i);
                String[] emails = {u.email()};
                Assert.assertEquals(u.email(), userDao.selectByEmails(emails).get(0).email());
            }

        }

        @Test
        public void getByEmail() {
            List<User> usersToStore = UserTestUtil.randomUsers();
            userDao.deleteAll();
            userDao.insertUsers(usersToStore.toArray(new User[0]));

            for (int i = 0; i < 10; i++) {
                User u = usersToStore.get(i);
                Assert.assertEquals(u.inscriptionDate(), userDao.getUserByEmail(u.email()).inscriptionDate());
            }
        }

        @Test
        public void findUserByName() {

            List<User> usersToStore = UserTestUtil.randomUsers();
            userDao.deleteAll();
            userDao.insertUsers(usersToStore.toArray(new User[0]));

            for (int i = 0; i < 10; i++) {
                User u = usersToStore.get((int) (Math.random() * usersToStore.size()));
                User found = userDao.findByName(u.firstName(), u.lastName());
                assertNotNull(found);
            }
        }

        @Test
        public void deleteUsers() {

            List<User> usersToStore = UserTestUtil.randomUsers();
            userDao.deleteAll();
            userDao.insertUsers(usersToStore.toArray(new User[0]));

            for (int i = 0; i < 10; i++) {
                User u = usersToStore.get(i);
                userDao.delete(u);
            }
            List<User> allRemainingUsers = userDao.getAll();
            Assert.assertEquals(usersToStore.size() - 10, allRemainingUsers.size());
        }

        @Test
        public void updateUsers() {

            List<User> usersToStore = UserTestUtil.randomUsers();
            userDao.deleteAll();
            userDao.insertUsers(usersToStore.toArray(new User[0]));

            for (int i = 0; i < 10; i++) {
                User u = usersToStore.get(i);
                String newFirstName = UserTestUtil.randomUser().firstName();
                u.setFirstName(newFirstName);
                userDao.updateUsers(u);
                u = userDao.getUserByEmail(u.email());
                Assert.assertEquals(newFirstName, u.firstName());
            }
        }

    }
}
