package ch.epfl.sweng.swengproject.storage.db;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.content.SharedPreferences;


import java.util.List;


import ch.epfl.sweng.swengproject.MyApplication;

import static android.content.Context.MODE_PRIVATE;

@Dao
public abstract class UserDao {

    @Query("SELECT * FROM user")
    public abstract List<User> getAll();//tested

    public  void insertUsers(User... users){//tested
        insertUsersInDB(users);
        for (User user: users){
            user.savePicture();
        }
    }
    @Insert
    abstract void insertUsersInDB(User... users);//tested from insertUsers

    public  void deleteAll(){//tested
        deleteAllInDB();
        User.deletePictureFolder();
    }

    @Query("DELETE FROM User")
    abstract void deleteAllInDB();//tested from deleteAll()

    @Query("SELECT * FROM user WHERE email == :email")
    public abstract User getUserByEmail(String email);//tested

    @Query("SELECT * FROM user WHERE email IN (:emails)")
    public abstract List<User> selectByEmails(String[] emails);//tested



    @Query("SELECT * FROM user WHERE firstName LIKE :first AND "
            + "lastName LIKE :last LIMIT 1")
    public abstract User findByName(String first, String last);//tested



    public void updateUsers(User... users){//tested
        updateUsersInDB(users);
        for (User user: users){
            user.savePicture();
        }
    }

    @Update
    abstract  void updateUsersInDB(User... users);//tested from updateUsers

    public  void delete(User user){ //tested
        deleteInDB(user);
        user.deletePicture();
    }
    @Delete
    abstract void deleteInDB(User user);//tested from delete



    /**
     * Store the profile of the client that own the end device in his(this) device
     * @param me the user that represent the profile of the client
     */
    public  void storeMyOwnProfile(User me){//tested
        deleteMyOwnProfile();
        SharedPreferences preferences = MyApplication.getAppContext().getSharedPreferences("user_email", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("my_email", me.email());
        editor.apply();
        insertUsers(me);
        System.out.println("My own profile was saved in the Hard Disk");
    }

    /**
     *
     * @return the client profile if it exist in his device, else null
     */
    public User getMyOwnProfile(){//tested
        String myEmail = MyApplication.getAppContext().
                getSharedPreferences("user_email", MODE_PRIVATE).getString("my_email", null);
        if(myEmail == null){
            return null;
        }else{
            return getUserByEmail(myEmail);
        }
    }

    private void deleteMyOwnProfile(){//tested
        User meToDel = getMyOwnProfile();
        if(meToDel != null){
            delete(meToDel);
        }
    }
}
