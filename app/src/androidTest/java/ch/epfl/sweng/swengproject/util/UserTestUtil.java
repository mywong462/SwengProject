package ch.epfl.sweng.swengproject.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import ch.epfl.sweng.swengproject.MyApplication;
import ch.epfl.sweng.swengproject.R;
import ch.epfl.sweng.swengproject.storage.db.User;


public class UserTestUtil {
    private UserTestUtil(){}

    private static final String[] firstNames = {"Jean", "Peter", "Harsh", "Jojo", "Ginette", "Mark", "Antoine",
            "Aurélie", "Manon", "Candice", "Axelle", "Axel"};

    private static String[] lastNames = {"Dupont", "Desvaux", "Sancord", "Lepreu", "Du Jardin", "Beau", "Müller",
            "Vendron", "De la rue", "Cestcuit", "Kessler", "Berger"};

    private static String[] emails = {"Dupont@gmail.com", "Desvaux@hotmail.com", "Sancord@hotmail.com", "Lepreu@gmail.com", "Du Jardin@yahoo.fr", "Beau@yahoo.fr", "Müller@yahoo.fr",
            "Vendron@gmail.com", "De la rue@hotmail.com", "Cestcuit@gmail.com", "Kessler@hotmail.com", "Berger@hotmail.com"};

    private static Date randomDate(){
        Random rnd = new Random();
        long    ms= -946771200000L + (Math.abs(rnd.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
        return new Date(ms);
    }

    private static Bitmap notRandomImage = BitmapFactory.decodeResource(MyApplication.getAppContext().getResources(), R.drawable.emptyprofile);

    /**
     *
     * @return a user that is make randommly to test purposes
     */
    public static User randomUser(){
        String rfn = firstNames[(int)(Math.random() * firstNames.length)];
        String rln = lastNames[(int)(Math.random() * lastNames.length)];
        String re = emails[(int)(Math.random() * emails.length)];
        User u = new User();
        u.setFirstName(rfn);
        u.setLastName(rln);
        u.setEmail(re+UUID.randomUUID());
        u.setInscriptionDate(randomDate());
        u.setPicture(notRandomImage);
        return u;
    }



    /**
     *
     * @return a random list of users  to test purposes
     */
    public static List<User> randomUsers(){
        List<User> users = new ArrayList<>();
        int listLength = (int) (Math.random() * 50) + 10;
        for(int i = 0; i< listLength; i++){
            User u = randomUser();
            users.add(u);
        }
        return users;
    }
}
