package ch.epfl.sweng.swengproject.storage;

import ch.epfl.sweng.swengproject.MyApplication;
import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.UserDao;

public class StorageHelper {
    private StorageHelper(){}

    /**
     * Delete all data that might be stored in the disk!
     */
    public static void deleteAllDataStoredLocally(){
        UserDao userDao = AppDatabase.getDatabase(MyApplication.getAppContext()).userDao();
        userDao.deleteAll();
    }
}
