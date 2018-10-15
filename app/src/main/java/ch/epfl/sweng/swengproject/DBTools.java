package ch.epfl.sweng.swengproject;

import android.location.Location;
import android.util.Log;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class DBTools {




    private static Location createAndSetLoc(double lat, double lon){

        Location loc = new Location("");
        loc.setLatitude(lat);
        loc.setLongitude(lon);

        return loc;
    }


    public static ArrayList<Need> filterNeeds(GeoPoint mGeoPoint, int range, ArrayList<Categories> categories, List<Need> listNeeds){

        ArrayList<Need> availableNeeds = new ArrayList<>();

        //To remove the needs that aren't in the range
        Location here = createAndSetLoc(mGeoPoint.getLatitude(),mGeoPoint.getLongitude());

        for (Need need : listNeeds) {
            Location needLoc = createAndSetLoc(need.getLatitude(),need.getLongitude());

            //If the need isn't in the desired range (range is in kilometer) and the need isn't outdated
            if (here.distanceTo(needLoc) <= (float) range * 1000 && need.getTimeToLive() > System.currentTimeMillis()) {
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
        if(range < 0){
            throw new IllegalArgumentException();
        }


    }


}
