package ch.epfl.sweng.swengproject;


import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DatabaseUnitTest {


    @Test(expected = NullPointerException.class)
    public void badCategory(){
        Database.getNeeds(new GeoPoint(12,34),200,null);
    }



    @Test(expected = IllegalArgumentException.class)
    public void badRange(){
        List<Categories> l = new ArrayList<>();
        l.add(Categories.HELP);

        Database.getNeeds(new GeoPoint(12,34),-200, l);
    }


    @Test
    public void correctlyFilterCategories(){
        ArrayList<Categories> l = new ArrayList<>();
        l.add(Categories.HELP);
        ArrayList<Need> listNeeds = new ArrayList<>();

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12));

        ArrayList<Need> needList = Database.filterNeeds(new GeoPoint(12,34), 200, l,listNeeds);

        assertEquals(0,needList.size());
    }

    @Test
    public void correctlyFilterRange(){
        ArrayList<Categories> l = new ArrayList<>();
        l.add(Categories.HELP);
        ArrayList<Need> listNeeds = new ArrayList<>();

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12));

        ArrayList<Need> needList = Database.filterNeeds(new GeoPoint(30,60), 2, l,listNeeds);

        assertEquals(0,needList.size());
    }

    @Test
    public void correctlyFilterTime(){
        ArrayList<Categories> l = new ArrayList<>();
        l.add(Categories.HELP);
        ArrayList<Need> listNeeds = new ArrayList<>();

        long timeValid =  System.currentTimeMillis() - 10000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12));

        ArrayList<Need> needList = Database.filterNeeds(new GeoPoint(12,34), 200, l,listNeeds);

        assertEquals(0,needList.size());
    }

    @Test
    public void correctlyFilterCategoriesList(){
        ArrayList<Categories> l = new ArrayList<>();
        l.add(Categories.HELP);
        l.add(Categories.NEED);
        ArrayList<Need> listNeeds = new ArrayList<>();

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12));
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.NEED, 12));
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.HELP, 12));
        ArrayList<Need> needList = Database.filterNeeds(new GeoPoint(12,34), 200, l,listNeeds);

        assertEquals(2,needList.size());
    }

}
