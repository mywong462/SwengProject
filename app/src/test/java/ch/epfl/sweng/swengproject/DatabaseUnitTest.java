package ch.epfl.sweng.swengproject;


import android.location.Location;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.model.LatLng;
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
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12, ""));

        ArrayList<Need> needList = DBTools.filterNeeds(new GeoPoint(12,34), 200, l,listNeeds);

        assertEquals(0,needList.size());
    }

    @Test
    public void correctlyFilterRange(){
        ArrayList<Categories> l = new ArrayList<>();
        l.add(Categories.HELP);
        ArrayList<Need> listNeeds = new ArrayList<>();

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12, ""));

        ArrayList<Need> needList = DBTools.filterNeeds(new GeoPoint(30,60), 2, l,listNeeds);

        assertEquals(0,needList.size());
    }

    @Test
    public void correctlyFilterTime(){
        ArrayList<Categories> l = new ArrayList<>();
        l.add(Categories.HELP);
        ArrayList<Need> listNeeds = new ArrayList<>();

        long timeValid =  System.currentTimeMillis() - 10000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12,""));

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
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12,""));
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.NEED, 12,""));
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.HELP, 12,""));
        ArrayList<Need> needList = DBTools.filterNeeds(new GeoPoint(12,34), 200, l,listNeeds);

        assertEquals(2,needList.size());
    }

    @Test(expected = NullPointerException.class)
    public void checkBadGeoPoints(){

        DBTools.checkPoints(null,new GeoPoint(23,45));

    }


    @Test
    public void checkEmptyNeed(){

        ArrayList<Need> listNeeds = new ArrayList<>();

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12, ""));

        assertTrue(DBTools.isNotFull(listNeeds,new LatLng(12,34)));

    }

    @Test
    public void checkFullNeed(){

        ArrayList<Need> listNeeds = new ArrayList<>();
        String participants = "hedi.sassi96@gmail.com,jean-claude@epfl.ch";

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 2, participants));

        assertFalse(DBTools.isNotFull(listNeeds,new LatLng(12,34)));

    }

    @Test
    public void alreadyAccepted(){

        ArrayList<Need> listNeeds = new ArrayList<>();
        String participants = "email@hotmail.ch";

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12,participants ));

        assertFalse(DBTools.notAlreadyAccepted(listNeeds,new LatLng(12,34),"email@hotmail.ch"));


    }


    @Test
    public void notAlreadyAccepted(){

        ArrayList<Need> listNeeds = new ArrayList<>();
        String participants = "email@hotmail.ch";

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12,participants ));

        assertTrue(DBTools.notAlreadyAccepted(listNeeds,new LatLng(12,34),"Simon"));

    }


    @Test
    public void convertString(){

        String test = "email@truc,qwertz@machin,bidule@hotmail.com";

        ArrayList<String> result = DBTools.convertCsvToArray(test);

        assertEquals(result.get(0), "email@truc");
        assertEquals(result.get(1), "qwertz@machin");
        assertEquals(result.get(2), "bidule@hotmail.com");
    }

    @Test
    public void canComputeNbrParticipants(){

        String test = "email@truc,qwertz@machin,bidule@hotmail.com";

        assertEquals(3,DBTools.computeNumber(test));

    }

    @Test
    public void canConvertCat(){

        String s1 = "HELP";
        String s2 = "ALL";
        String s3 = "NEED";
        String s4 = "MEET";

        assertEquals(Categories.HELP,DBTools.convertStringToCat(s1));
        assertEquals(Categories.ALL,DBTools.convertStringToCat(s2));
        assertEquals(Categories.NEED,DBTools.convertStringToCat(s3));
        assertEquals(Categories.MEET,DBTools.convertStringToCat(s4));

    }





}
