package ch.epfl.sweng.swengproject;


import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class NeedUnitTest {

    private String mail = "test@epfl.ch";
    private String description = "a description that makes sense";
    int ttl = 25;
    double lat = 46;
    double lo = 37.2;
    Categories category = Categories.HELP;
    int nbPeopleNeeded = 2;


    @Test
    public void canCreateNeed(){

        Need test = new Need(mail,description,ttl,lat,lo, category, nbPeopleNeeded, new ArrayList<String>());
        assertEquals(mail,test.getEmitter());
        assertEquals(description,test.getDescription());
        assertEquals(lat,test.getLatitude(),0.001);
        assertEquals(lo,test.getLongitude(),0.001);
        assertEquals(ttl,test.getTimeToLive());
        assertEquals(0, test.getParticipants().size());

    }

    @Test
    public void canSetNeeds(){

        Need test = new Need();

        test.setDescription(description);
        test.setEmitter(mail);
        test.setLatitude(lat);
        test.setLongitude(lo);
        test.setTimeToLive(ttl);
        test.setCategory(category);
        test.setNbPeopleNeeded(nbPeopleNeeded);
        test.setParticipants(new ArrayList<String>());

        assertEquals(mail,test.getEmitter());
        assertEquals(description,test.getDescription());
        assertEquals(lat,test.getLatitude(),0.001);
        assertEquals(lo,test.getLongitude(),0.001);
        assertEquals(ttl,test.getTimeToLive());
        assertEquals(category, test.getCategory());
        assertEquals(nbPeopleNeeded, test.getNbPeopleNeeded());
        assertEquals(0, test.getParticipants().size());
    }

    @Test
    public void canGetPosition(){

        Need test = new Need();

        test.setDescription(description);
        test.setEmitter(mail);
        test.setLatitude(lat);
        test.setLongitude(lo);
        test.setTimeToLive(ttl);
        test.setCategory(category);
        test.setNbPeopleNeeded(nbPeopleNeeded);

        GeoPoint gp = test.getPos();

        assertEquals(gp.getLatitude(),test.getLatitude(),0.001);
        assertEquals(gp.getLongitude(),test.getLongitude(),0.001);


    }

}
