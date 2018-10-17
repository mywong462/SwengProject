package ch.epfl.sweng.swengproject;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

public class DBTools {

    private static final double R = 6371;



    private static double haversine(GeoPoint g1, GeoPoint g2){

        checkPoints(g1,g2);
  
        return pow(sin((toRadians(g1.getLatitude()) - toRadians(g2.getLatitude()))/2), 2) +
                cos(toRadians(g1.getLatitude())) * cos(toRadians(g2.getLatitude())) * pow(sin((toRadians(g1.getLongitude()) - toRadians(g2.getLongitude()))/2), 2);
    }

    public static double distanceBetween(GeoPoint g1, GeoPoint g2){
        checkPoints(g1,g2);

        double a = haversine(g1, g2);

        double c = 2*atan2(sqrt(a), sqrt(1-a));

        return R * c;


    }

    public static ArrayList<Need> filterNeeds(GeoPoint here, int range, ArrayList<Categories> categories, List<Need> listNeeds){

        ArrayList<Need> availableNeeds = new ArrayList<>();

        for (Need need : listNeeds) {
            //If the need isn't in the desired range (range is in kilometer) and the need isn't outdated
            if (distanceBetween(here, need.getPos()) <= range && need.getTimeToLive() > System.currentTimeMillis()) {

                if (categories.contains(Categories.ALL) || categories.contains(need.getCategory())){
                    availableNeeds.add(need);
                }
            }


        }

        return availableNeeds;

    }


    public static void checkInput(GeoPoint mGeoPoint, int range, ArrayList<Categories> categories){

        if(categories == null || mGeoPoint == null){

            throw new NullPointerException();
        }
        else if(range < 0){
            throw new IllegalArgumentException();
        }

    }

    public static void checkPoints(GeoPoint g1, GeoPoint g2){
        if(g1 == null || g2 == null){
            throw new NullPointerException();
        }

    }

    /**
     * @Brief Check if there is a matching need with a number of participants less thant the max
     * @param needList the list of needs
     * @param pos the position of the current marker
     * @return true if the need is not full
     */
    public static boolean isNotFull(ArrayList<Need> needList, LatLng pos){
        for (Need need: needList){
            if(need.getPos().getLatitude() == pos.latitude && need.getPos().getLongitude() == pos.longitude && need.getParticipants().size() <= need.getNbPeopleNeeded() ){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param needList
     * @param pos
     * @param part
     * @return return true if the user as not yet accepted this need
     */
    public static boolean notAlreadyAccepted(ArrayList<Need> needList, LatLng pos, String part){
        for (Need need: needList){
            if(need.getPos().getLatitude() == pos.latitude && need.getPos().getLongitude() == pos.longitude && !need.getParticipants().contains(part) ){
                return true;
            }
        }
        return false;
    }


}
