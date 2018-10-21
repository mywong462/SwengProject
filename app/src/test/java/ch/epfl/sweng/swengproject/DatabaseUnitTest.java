package ch.epfl.sweng.swengproject;


import android.location.Location;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
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


    @Test
    public void canCheckIfParticipating(){


        ArrayList<Need> listNeeds = new ArrayList<>();
        String participants = "specialuser@gmail.com";

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12,participants ));
        listNeeds.add(new Need("specialuser@gmail.com","random description",timeValid, 12, 34,Categories.MEET, 12,participants ));

        assertTrue(DBTools.participateToNeed(listNeeds,participants ));

        assertEquals(2,DBTools.numberOfParticipation(listNeeds,participants));

    }

    @Test(expected = NullPointerException.class)
    public void wrongInput(){

        DBTools.participateToNeed(null,"");

    }

    @Test(expected = NullPointerException.class)
    public void wrongInput2(){

        DBTools.numberOfParticipation(null,"");

    }


    @Test
    public void canComputeProportionOfCat(){



        ArrayList<Need> listNeeds = new ArrayList<>();
        String participants = "specialuser@gmail.com";

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12,participants ));
        listNeeds.add(new Need("specialuser@gmail.com","random description",timeValid, 12, 34,Categories.NEED, 12,participants ));

        int[] prop = DBTools.proportionOfCategories(listNeeds);

        assertEquals(1,prop[CategoriesInfo.convert(Categories.MEET)]);
        assertEquals(1,prop[CategoriesInfo.convert(Categories.NEED)]);
        assertEquals(0,prop[CategoriesInfo.convert(Categories.HELP)]);
    }

    @Test(expected = NullPointerException.class)
    public void wrongInput3(){

        DBTools.proportionOfCategories(null);
    }

    @Test
    public void canComputeTotalPart(){

        ArrayList<Need> listNeeds = new ArrayList<>();
        String participants = "specialuser@gmail.com,simon@epfl.ch,noreply@hotmail.com";

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 12, 34,Categories.MEET, 12,participants ));
        listNeeds.add(new Need("specialuser@gmail.com","random description",timeValid, 12, 34,Categories.NEED, 12,participants ));

        assertEquals(6, DBTools.totalNbrParticipants(listNeeds));
        assertEquals(0, DBTools.totalNbrParticipants(new ArrayList<Need>()));
    }

    @Test(expected = NullPointerException.class)
    public void wrongInput4(){

        DBTools.totalNbrParticipants(null);
    }

    @Test
    public void canFindClosest(){


        ArrayList<Need> listNeeds = new ArrayList<>();
        String participants = "specialuser@gmail.com,simon@epfl.ch,noreply@hotmail.com";

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 0, 0,Categories.MEET, 12,participants ));
        listNeeds.add(new Need("specialuser@gmail.com","random description",timeValid, 12, 34,Categories.NEED, 12,participants ));

        Need closest = DBTools.findClosest(listNeeds,new GeoPoint(11, 30));

        assertEquals(12,closest.getLatitude(),0.0001);

        closest = DBTools.findClosest(listNeeds,new GeoPoint(1, 4));

        assertEquals(0,closest.getLatitude(),0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongInput5(){
        DBTools.findClosest(new ArrayList<Need>(),new GeoPoint(0,0));
    }

    @Test(expected = NullPointerException.class)
    public void wrongInput6(){
        DBTools.findClosest(null,new GeoPoint(0,0));
    }

    @Test
    public void canComputeAverage(){

        ArrayList<Need> listNeeds = new ArrayList<>();
        String participants = "specialuser@gmail.com,simon@epfl.ch,noreply@hotmail.com";

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid, 0, 0,Categories.MEET, 12,participants ));
        listNeeds.add(new Need("specialuser@gmail.com","random description",timeValid, 12, 34,Categories.NEED, 12,participants ));


        assertEquals(3,DBTools.averageNbrParticipants(listNeeds),0.00001);

    }

    @Test(expected = NullPointerException.class)
    public void wrongInput7(){
        DBTools.averageNbrParticipants(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongInput8(){
        DBTools.averageNbrParticipants(new ArrayList<Need>());
    }



    @Test
    public void canSortNeedWithTime(){

        ArrayList<Need> listNeeds = new ArrayList<>();
        String participants = "specialuser@gmail.com,simon@epfl.ch,noreply@hotmail.com";

        long timeValid =  System.currentTimeMillis() + 100000;
        listNeeds.add(new Need("email@hotmail.ch","random description",timeValid -1, 0, 0,Categories.MEET, 12,participants ));
        listNeeds.add(new Need("specialuser@gmail.com","random description",timeValid, 12, 34,Categories.NEED, 12,participants ));
        listNeeds.add(new Need("specialuser@gmail.com","random description",timeValid+1, 12, 34,Categories.NEED, 12,participants ));
        listNeeds.add(new Need("specialuser@gmail.com","random description",timeValid + 20, 12, 34,Categories.NEED, 12,participants ));


        Comparator<Need> c = new Comparator<Need>() {
            @Override
            public int compare(Need o1, Need o2) {
                if(o1.getTimeToLive() > o2.getTimeToLive()){
                    return 1;
                }
                else if(o1.getTimeToLive() == o2.getTimeToLive()){
                    return 0;
                }
                else{
                    return -1;
                }
            }
        };

        List<Need> res = DBTools.sort(listNeeds,c);

        assertTrue(res.get(0).getTimeToLive() < res.get(1).getTimeToLive());
        assertTrue(res.get(1).getTimeToLive() < res.get(3).getTimeToLive());

    }

}
