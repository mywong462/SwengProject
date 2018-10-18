package ch.epfl.sweng.swengproject.storage;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import ch.epfl.sweng.swengproject.MyApplication;
import ch.epfl.sweng.swengproject.controllers.MainActivity;
import ch.epfl.sweng.swengproject.storage.db.AppDatabase;
import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.storage.db.UserDao;

public class StorageHelper {
    private StorageHelper(){}

    /**
     * Delete all data that might be stored in the disk!
     */
    public static void deleteAllDataStoredLocally(){
        UserDao userDao = AppDatabase.getDatabase().userDao();
        userDao.deleteAll();
        System.out.println("Normally everything must have been delete from the local storage of the phone now! ");
    }

    /**
     * If a the user profile is stored in the device, it is sent to the server.
     * If no user profile is stored in the device, it is downloaded from the server.
     */
    public static void getOrSendMyProfileToServer(){

        new AsyncTask<Void, Void, Void>() {

            final UserDao userDao = AppDatabase.getDatabase().userDao();
            User me = null;

            @Override
            protected Void doInBackground( final Void ... params ) {
                me = userDao.getMyOwnProfile();
                return null;
            }

            @Override
            protected void onPostExecute(final Void result ) {
               if(me == null){
                   getMyProfileFromServer();
               }else{
                   sendMyProfileToTheServer(me);
               }
            }
        }.execute();
    }

    /**
     * Update the information of a this user in the server: store there the first name and last name
     * as well as his picture
     */
    public static void sendMyProfileToTheServer(){
        new AsyncTask<Void, Void, Void>() {

            final UserDao userDao = AppDatabase.getDatabase().userDao();
            User me = null;

            @Override
            protected Void doInBackground( final Void ... params ) {
                me = userDao.getMyOwnProfile();
                return null;
            }

            @Override
            protected void onPostExecute(final Void result ) {
                if(me != null) {
                    sendMyProfileToTheServer(me);
                }
            }
        }.execute();
    }

    /**
     * Update the information of a user in the server: store there the first name and last name
     * as well as his picture
     */
    public static void sendMyProfileToTheServer(User u){
       //TODO: Implement me
        //must be asyncrone !!!!!
        System.out.println("Normally we must send now the profile to the server");
    }

    /**
     * Retrive all info that is on the server about myself. (My picture, name, and firstname)
     */
    public static void getMyProfileFromServer(){
        //TODO: Implement me
        //must be asyncrone !!!!!
        System.out.println("Normally we must get now the profile from the server");
    }
}
