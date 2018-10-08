package ch.epfl.sweng.swengproject.storage.db;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.Date;

import ch.epfl.sweng.swengproject.storage.filesystem.BitMapStorage;
import ch.epfl.sweng.swengproject.storage.filesystem.Generic;


@Entity()
public class User {

    private static String PICTURES_FOLDER = "user_pictures";

    @PrimaryKey
    @NonNull
    private String email;
    private String firstName = null;
    private String lastName = null;
    private Date inscriptionDate = null;



    @Ignore
    private Bitmap picture = null;

    /*public User(long id, String firstName, String lastName, String email, Date inscriptionDate, Bitmap picture){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.inscriptionDate = inscriptionDate;
        this.picture = picture;
    }*/

    private String pathToPicture(){
        return PICTURES_FOLDER + File.pathSeparator + email + ".jpeg";
    }

    /**
     * Save the picture of the user in the filesystem
     * @return true if the picture can have been saved, else false
     */
    boolean savePicture(){
        if(picture != null){
            return BitMapStorage.saveImage(picture, pathToPicture());
        }else{
            return false;
        }
    }

    /**
     * Delete the picture of a user in the file system
     * @return true if the picture doesn't exist in the file system at
     *       the end of the invocation of this method, else false
     */
    boolean deletePicture(){
        return Generic.deleteFile(pathToPicture());
    }

    /**
     *
     * @return true if the folder doesn't exist in the file system at
     *  the end of the invocation of this method, else false
     */
    static boolean deletePictureFolder(){
        return Generic.deleteFolder(PICTURES_FOLDER);
    }

    //-----------SETTERS----------
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setInscriptionDate(Date inscriptionDate) {
        this.inscriptionDate = inscriptionDate;
    }
    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    //-----------GETTERS----------
    public String firstName(){
        return firstName;
    }
    public String lastName(){
        return lastName;
    }
    public String email(){
        return email;
    }
    public Date inscriptionDate(){
        return inscriptionDate;
    }

    /**
     *
     * @return the picture of the user, if it is not in the memory,
     * it tries to load it from the disk
     */
    public Bitmap picture(){
        if(picture == null){
            picture = BitMapStorage.getImage(pathToPicture());
        }
        return picture;
    }

}