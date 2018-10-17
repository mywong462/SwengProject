package ch.epfl.sweng.swengproject;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserSettingsTest {

    List<Categories> cList = new ArrayList<>();

    @Test(expected =  NullPointerException.class)
    public void testCreateWithCategoriesNull(){
        UserSettings us = new UserSettings(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidRange(){
        cList.add(Categories.HELP);
        UserSettings us = new UserSettings(cList, 0);
    }

    @Test
    public void testGetRange(){
        cList.add(Categories.HELP);
        UserSettings us = new UserSettings(cList, 1);
        int range = us.getRange();
        assertEquals(range, 1);
    }

    @Test
    public void testGetCategories(){
        List<Categories> listCompare = new ArrayList<>();
        listCompare.add(Categories.NEED);

        UserSettings us = new UserSettings(listCompare, 1);

        ArrayList<Categories> list = us.getCategories();
        assertTrue(list.equals(listCompare));
    }

    @Test(expected = NullPointerException.class)
    public void setCategoriesNull(){
        cList.add(Categories.NEED);
        UserSettings us = new UserSettings(cList, 1);

        us.setCategories(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setRangeInvalid(){
        cList.add(Categories.NEED);
        UserSettings us = new UserSettings(cList, 1);

        us.setRange(-1);
    }

    @Test
    public void setCategoriesWork(){
        cList.add(Categories.NEED);
        UserSettings us = new UserSettings(cList, 1);

        cList.add(Categories.HELP);
        us.setCategories(cList);

        assertTrue(cList.equals(us.getCategories()));
    }

    @Test
    public void setRangeWork(){
        cList.add(Categories.NEED);
        UserSettings us = new UserSettings(cList, 1);
        us.setRange(5);

        assertEquals(us.getRange(), 5);
    }
}
