package ch.epfl.sweng.swengproject;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.storage.db.UserDao;
import ch.epfl.sweng.swengproject.util.UserTestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)

public class UserTests {

    //TODO: Test how the system handle the pictures of the users !


    private static UserDao userDao;
    private static AppDatabase dataBase;

    @BeforeClass
    public static void doBeforeAll(){
        AppDatabase.setUnderTest(true);
        dataBase = AppDatabase.getInstance();
        userDao = dataBase.userDao();
    }

    @AfterClass
    public static void doAfterAll() {
        AppDatabase.setUnderTest(false);
        dataBase.close();
        AppDatabase.destroyInstance();
    }

    @Before
    public void createDb() {
        userDao.deleteAll();
    }


    @Test
    public void storeAndFetchAll() {
        List<User> usersToStore = UserTestUtil.randomUsers();
        userDao.insertUsers(usersToStore.toArray(new User[0]));
        List<User> fetchedUsers = userDao.getAll();
        assertEquals(usersToStore.size(), fetchedUsers.size());
    }

    @Test
    public void deleteAll() {
        List<User> usersToStore = UserTestUtil.randomUsers();
        userDao.insertUsers(usersToStore.toArray(new User[0]));
        userDao.deleteAll();
        List<User> fetchedUsers = userDao.getAll();
        assertEquals(0, fetchedUsers.size());
    }

    @Test
    public void getByEmail() {
        List<User> usersToStore = UserTestUtil.randomUsers();
        userDao.insertUsers(usersToStore.toArray(new User[0]));

        for (int i = 0; i < 10; i++) {
            User u = usersToStore.get(i);
            assertEquals(u.inscriptionDate(), userDao.getUserByEmail(u.email()).inscriptionDate());
        }
    }

    @Test
    public void loadByEmails() {
        List<User> usersToStore = UserTestUtil.randomUsers();
        userDao.insertUsers(usersToStore.toArray(new User[0]));

        for (int i = 0; i < 10; i++) {
            User u = usersToStore.get(i);
            String[] emails = {u.email()};
            assertEquals(u.email(), userDao.selectByEmails(emails).get(0).email());
        }

    }



    @Test
    public void findUserByName() {

        List<User> usersToStore = UserTestUtil.randomUsers();
        userDao.insertUsers(usersToStore.toArray(new User[0]));

        for (int i = 0; i < 10; i++) {
            User u = usersToStore.get((int) (Math.random() * usersToStore.size()));
            User found = userDao.findByName(u.firstName(), u.lastName());
            assertNotNull(found.picture());
        }
    }

    @Test
    public void deleteUsers() {

        List<User> usersToStore = UserTestUtil.randomUsers();
        userDao.insertUsers(usersToStore.toArray(new User[0]));

        for (int i = 0; i < 10; i++) {
            User u = usersToStore.get(i);
            userDao.delete(u);
        }
        List<User> allRemainingUsers = userDao.getAll();
        assertEquals(usersToStore.size() - 10, allRemainingUsers.size());
    }

    @Test
    public void updateUsers() {

        List<User> usersToStore = UserTestUtil.randomUsers();
        userDao.insertUsers(usersToStore.toArray(new User[0]));

        for (int i = 0; i < 10; i++) {
            User u = usersToStore.get(i);
            String newFirstName = UserTestUtil.randomUser().firstName();
            u.setFirstName(newFirstName);
            userDao.updateUsers(u);
            u = userDao.getUserByEmail(u.email());
            assertEquals(newFirstName, u.firstName());
        }
    }

    @Test
    public void myProfile() {
        User me = UserTestUtil.randomUser();
        userDao.storeMyOwnProfile(me);
        User me2 = userDao.getMyOwnProfile();
        assertEquals(me.email(), me2.email());
    }
}