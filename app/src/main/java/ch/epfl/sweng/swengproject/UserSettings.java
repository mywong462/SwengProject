package ch.epfl.sweng.swengproject;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swengproject.Categories;

public class UserSettings {

    private int range = 0; //in kilometers
    List<Categories> categories;

    public UserSettings(List<Categories> categories, int range){
        checkNonNull(categories);
        validRange(range);

        this.categories = categories;
        this.range = range;
    }

    //Getters
    public int getRange(){ return range; }
    public ArrayList<Categories> getCategories(){ return new ArrayList<>(categories); }

    //Setters
    public void setRange(int range){
        validRange(range);
        this.range = range;
    }
    public void setCategories(List<Categories> categories){
        checkNonNull(categories);
        this.categories = categories;
    }

    private void checkNonNull(List<Categories> categories){
        if(categories == null){
            throw new NullPointerException("categories is null");
        }
    }

    private void validRange(int range){
        if(range <= 0){
            throw new IllegalArgumentException("range must be greater or equal than zero");
        }
    }

}
