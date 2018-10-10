package ch.epfl.sweng.swengproject;


import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import static org.junit.Assert.*;

public class NeedUnitTest {

    private String mail = "test@epfl.ch";
    private String description = "a description that makes sense";
    int ttl = 25;
    double lat = 46;
    double lo = 37.2;


    @Test
    public void canCreateNeed(){

        Need test = new Need(mail,description,ttl,lat,lo);
        assertEquals(mail,test.getEmitter());
        assertEquals(description,test.getDescription());
        assertEquals(lat,test.getLatitude(),0.001);
        assertEquals(lo,test.getLongitude(),0.001);
        assertEquals(ttl,test.getTimeToLive());

    }

    @Test
    public void canSetNeeds(){

        Need test = new Need();

        test.setDescription(description);
        test.setEmitter(mail);
        test.setLatitude(lat);
        test.setLongitude(lo);
        test.setTimeToLive(ttl);

        assertEquals(mail,test.getEmitter());
        assertEquals(description,test.getDescription());
        assertEquals(lat,test.getLatitude(),0.001);
        assertEquals(lo,test.getLongitude(),0.001);
        assertEquals(ttl,test.getTimeToLive());
    }

    @Test
    public void canGetPosition(){

        Need test = new Need();

        test.setDescription(description);
        test.setEmitter(mail);
        test.setLatitude(lat);
        test.setLongitude(lo);
        test.setTimeToLive(ttl);

        GeoPoint gp = test.getPos();

        assertEquals(gp.getLatitude(),test.getLatitude(),0.001);
        assertEquals(gp.getLongitude(),test.getLongitude(),0.001);


    }

}
