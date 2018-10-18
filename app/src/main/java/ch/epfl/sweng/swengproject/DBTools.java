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
            if(need.getPos().getLatitude() == pos.latitude &&
                    need.getPos().getLongitude() == pos.longitude &&
                    computeNumber(need.getParticipants()) < need.getNbPeopleNeeded() ){
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
            if(need.getPos().getLatitude() == pos.latitude &&
                    need.getPos().getLongitude() == pos.longitude &&
                    !need.getParticipants().contains(part) ){
                return true;
            }
        }
        return false;
    }


    /**
     * @Brief compute the number of data in a csv-formatted string
     * @param csv
     * @return
     */
    public static int computeNumber(String csv){

        if(csv.isEmpty()){
            return 0;
        }

        char[] tab = csv.toCharArray();

        int length = 0;

        for(int i = 0; i < tab.length; ++i){
            if(tab[i] == ',') {
                length++;
            }

        }
        return length + 1;
    }

    /**
     * @Brief Convert a string in cvs format into an arraylist of string
     * @param csv
     * @return
     */

    public static ArrayList<String> convertCsvToArray(String csv){


        ArrayList<String> array = new ArrayList<>();

        char[] tab = csv.toCharArray();

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < tab.length; ++i){

            if(tab[i] == ','){
                array.add(sb.toString());
                sb = new StringBuilder();
            }
            else{
                sb.append(tab[i]);
            }
        }
        array.add(sb.toString());

        return reverseArray(array);
    }

    /**
     * @Brief Reverse a given arraylist
     * @param array
     * @return
     */

    public static <T> ArrayList<T> reverseArray(ArrayList<T> array){

        ArrayList<T> result = new ArrayList<>();

        for(T s:array){

            result.add(s);

        }

        return result;
    }


    public static Categories convertStringToCat(String s){

        Categories c ;

        switch(s){

            case "HELP" : c = Categories.HELP;
                break;
            case"MEET" : c = Categories.MEET;
                break;
            case "NEED" : c = Categories.NEED;
                break;
            default : c = Categories.ALL;
                break;

        }

        return c;

    }


    // -------------------------------Statistics And queries on ArrayList<Need>--------------------------------


    /**
     * @brief function used to check if someone participate to a need
     * @param list
     * @param user
     * @return
     */
    public boolean participateToNeed(ArrayList<Need> list, String user){

        if(list == null || user == null || user.isEmpty()){

            throw new NullPointerException();
        }

        for(Need n: list){

            ArrayList<String> participants = DBTools.convertCsvToArray(n.getParticipants());

            if(participants.contains(user)){
                return true;
            }
        }

        return false;

    }


    /**
     * @brief compute the number of needs to which the user participates
     * @param list
     * @param user
     * @return
     */
    public int numberOfParticipation(ArrayList<Need> list, String user){

        if(list == null || user == null || user.isEmpty()){

            throw new NullPointerException();
        }

        int count = 0;

        for(Need n: list){

            ArrayList<String> participants = DBTools.convertCsvToArray(n.getParticipants());

            if(participants.contains(user)){
                count++;
            }
        }

        return count;

    }

    /**
     * @brief Compute the proportion of each category in the list
     * @param list
     * @return
     */
    public int[] proportionOfCategories(ArrayList<Need> list){

        if(list == null){
            throw new NullPointerException();
        }

        int[] proportion = new int[CategoriesInfo.size];

        for(int i = 0; i < CategoriesInfo.size; ++i){
            proportion[i] = 0;
        }

        for(Need n : list){

            proportion[CategoriesInfo.convert(n.getCategory())] +=1;
        }

        return proportion;

    }

    /**
     * @brief Compute the total number of participants of all available needs
     * @param list
     * @return
     */
    public int totalNbrParticipants(ArrayList<Need> list){

        if(list == null){
            throw new NullPointerException();
        }

        int total = 0;

        for(Need n : list){

            total += computeNumber(n.getParticipants());
        }

        return total;
    }

    /**
     * @brief Method used to compute the closes Need
     * @param list
     * @param pos
     * @return
     */
    public Need findClosest(ArrayList<Need> list, GeoPoint pos){

        if(list == null){
            throw new NullPointerException();
        }
        if(list.isEmpty()){
            throw new IllegalArgumentException("List is empty. Cannot compute closes Need");
        }

        Need closest = new Need();

        for(int i = 0; i < list.size(); ++i){

            Need curr = list.get(i);

            if(i == 0){
                closest = curr;
            }
            else{

                double currDist = DBTools.distanceBetween(pos,closest.getPos());

                if(DBTools.distanceBetween(pos,curr.getPos()) < currDist){
                    closest = curr;
                }
            }
        }

        return closest;

    }

    /**
     * @Brief Compute the average number of people per need
     * @param list
     * @return
     */
    public double averageNbrParticipants(ArrayList<Need> list){
        if(list == null){
            throw new NullPointerException();
        }
        if(list.isEmpty()){
            throw new IllegalArgumentException("List is empty. Cannot compute average");
        }

        return totalNbrParticipants(list) / list.size();

    }


}
