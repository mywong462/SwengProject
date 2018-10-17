package ch.epfl.sweng.swengproject;

import org.junit.Test;

public class UserSettingsTest {


    @Test(expected =  NullPointerException.class)
    public void testCreateWithCategoriesNull(){
        UserSettings us = new UserSettings(null, 0);
    }
}
