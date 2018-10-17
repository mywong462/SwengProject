package ch.epfl.sweng.swengproject;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swengproject.Categories;

public class UserSettings {

    private int range = 0; //in kilometers
    List<Categories> categories;

    public UserSettings(List<Categories> categories, int range){
        this.categories = categories;
        this.range = range;
    }

    //Getters
    public int getRange(){ return range; }
    public ArrayList<Categories> getCategories(){ return new ArrayList<>(categories); }

    //Setters
    public void setRange(int range){
        if(range <= 0){
            throw new IllegalArgumentException("range must be greater than 0");
        }
        this.range = range;
    }
    public void setCategories(List<Categories> categories){
        if(categories == null){
            throw new NullPointerException("list of categories is null");
        }
        this.categories = categories;
    }

}
