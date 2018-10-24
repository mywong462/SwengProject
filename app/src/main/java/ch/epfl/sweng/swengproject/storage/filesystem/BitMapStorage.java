package ch.epfl.sweng.swengproject.storage.filesystem;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import ch.epfl.sweng.swengproject.MyApplication;


public class BitMapStorage {
    private BitMapStorage() {
    }


    /**
     * Store a Bitmap on disk in the private folder of the application
     *
     * @param image   the bitmap image to store
     * @param atPath  the path where to store the image
     * @return true if the image is saved, else false
     */
    public static boolean saveImage(Bitmap image, String atPath) {

        Context context = MyApplication.getAppContext();
        File file = new File(context.getFilesDir(), atPath);

        try {
            OutputStream fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * Fetch a bitmap from the filesystem at path atRelativePath in the private
     * folder of this application
     * @param atRelativePath the path where to fetch from a BitMap instance
     * @return the Bitmap instance if it could have been found, else null
     */
    public static Bitmap getImage(String atRelativePath) {
        Context context = MyApplication.getAppContext();
        File f = new File(context.getFilesDir(), atRelativePath);
        return  BitmapFactory.decodeFile(f.getPath());
    }
}
