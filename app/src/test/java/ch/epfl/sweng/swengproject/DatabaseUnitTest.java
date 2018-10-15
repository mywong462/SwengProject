package ch.epfl.sweng.swengproject;


import android.location.Location;
import android.widget.ArrayAdapter;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DatabaseUnitTest {


    @Test(expected = NullPointerException.class)
    public void checkInputNull(){

        DBTools.checkInput(null,13, null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void checkInputRange(){
        ArrayList<Categories> l = new ArrayList<>();
        l.add(Categories.HELP);

        DBTools.checkInput(new GeoPoint(13, 42),-13, l);

    }


    @Test
    public void distanceIsCorrect(){
        GeoPoint g1 = new GeoPoint(50, 5);
        GeoPoint g2 = new GeoPoint(51, 6);

        assertEquals(131.78, DBTools.distanceBetween(g1, g2), 1);
    }


    @Test
    public void correctlyFilterCategories(){
        ArrayList<Categories> l = new ArrayList<>();
        l.add(Categories.HELP);
        ArrayList<Need> listNeeds = new ArrayList<>();

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12));

        ArrayList<Need> needList = DBTools.filterNeeds(new GeoPoint(12,34), 200, l,listNeeds);

        assertEquals(0,needList.size());
    }

    @Test
    public void correctlyFilterRange(){
        ArrayList<Categories> l = new ArrayList<>();
        l.add(Categories.HELP);
        ArrayList<Need> listNeeds = new ArrayList<>();

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12));

        ArrayList<Need> needList = DBTools.filterNeeds(new GeoPoint(30,60), 2, l,listNeeds);

        assertEquals(0,needList.size());
    }

    @Test
    public void correctlyFilterTime(){
        ArrayList<Categories> l = new ArrayList<>();
        l.add(Categories.HELP);
        ArrayList<Need> listNeeds = new ArrayList<>();

        long timeValid =  System.currentTimeMillis() - 10000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12));

        ArrayList<Need> needList = DBTools.filterNeeds(new GeoPoint(12,34), 200, l,listNeeds);

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
        ArrayList<Need> needList = DBTools.filterNeeds(new GeoPoint(12,34), 200, l,listNeeds);

        assertEquals(2,needList.size());
    }

    @Test(expected = NullPointerException.class)
    public void checkBadGeoPoints(){

        DBTools.checkPoints(null,new GeoPoint(23,45));

    }



}
