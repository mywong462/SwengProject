package ch.epfl.sweng.swengproject;

import android.graphics.Bitmap;

import org.junit.Test;

import ch.epfl.sweng.swengproject.storage.db.User;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class UserUnitTest {


    @Test(expected = NullPointerException.class)
    public void wrongPictureSet(){

        User u = new User();

        u.setPicture(null);

    }

    @Test(expected = NullPointerException.class)
    public void saveWrongPicture(){

        User u = new User();

        u.setPicture(null);

        assertFalse(u.savePicture());

    }

    @Test(expected = NullPointerException.class)
    public void getWrongPicture(){

        User u = new User();

        u.setPicture(null);
        u.picture();

    }

}
