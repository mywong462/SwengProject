package ch.epfl.sweng.swengproject.storage.filesystem;


import android.content.Context;

import java.io.File;

import ch.epfl.sweng.swengproject.MyApplication;

public class Generic {
    private Generic(){}


    /**
     * Delete a file at the path atRelativePath in the private folder of the application
     * @param atRelativePath the path where to delete the file
     * @return true if the file doesn't exist in the file system at
     * the end of the invocation of this method, else false
     */
    public static boolean deleteFile(String atRelativePath){
        Context context = MyApplication.getAppContext();
        File f = new File(context.getFilesDir(), atRelativePath);
        if(f.exists() && f.isDirectory()){
            return false;
        }else if(f.exists() && f.isFile()) {
            return f.delete();
        }else{
            return true;
        }
    }

    /**
     * Delete a folder at the path atRelativePath in the private folder of the application
     * @param atRelativePath the path where to delete the folder
     * @return true if the folder doesn't exist in the file system at
     * the end of the invocation of this method, else false
     */
    public static boolean deleteFolder(String atRelativePath){
        Context context = MyApplication.getAppContext();
        File f = new File(context.getFilesDir(), atRelativePath);
        if(f.exists() && f.isFile()){
            return false;
        }else if(f.exists() && f.isDirectory()) {
            return f.delete();
        }else{
            return true;
        }
    }
}

