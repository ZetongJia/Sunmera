package luminosit.sunmera.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by GaryTang on 4/11/15.
 */
public class PhotoHandler {

    static final String SUFFIX_PHOTO = ".jpg";
    public static final String PREFIX_THUMBNAIL = "thumbnail_";

    //thumbnail size
    public static final int WIDTH = 150;
    public static final int HEIGHT = 200;

    //storage direction
    public static final String DIRECTORY_IMAGE = "savedImages";

    final Context activityContext;
    final File directory;

    ContextWrapper wrapper;

    public PhotoHandler(Context context){
        activityContext = context;
        wrapper = new ContextWrapper(activityContext);

        //directory: /data/data/sunmera/app_data/<storageDir>
        directory = wrapper.getDir(DIRECTORY_IMAGE, Context.MODE_PRIVATE);
    }

    public String saveImage(Bitmap image, String name){
        //save the image and return its absolute path
        File imagePath = new File(directory, name + SUFFIX_PHOTO);
        Log.d("### PhotoHandler", "Saving " + name + SUFFIX_PHOTO);

        //write the image using the compress method
        FileOutputStream stream = null;
        try{
            stream = new FileOutputStream(imagePath);
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }catch (IOException ex){
            Log.d("### PhotoHandler", "Error during saving");
        }

        //close the output stream
        if (stream != null) {
            try{
                stream.close();
            }catch (IOException ex){
                Log.d("### PhotoHandler", "Error during closing file output stream");
            }
        }

        //return the absolute path
        return imagePath.getAbsolutePath();
    }

    public Bitmap readImage(String name){
        //read the image, returns null if the image does not exist
        Log.d("### PhotoHandler", "Reading " + name + SUFFIX_PHOTO);
        Bitmap image = null;
        try {
            image = BitmapFactory.decodeStream(new FileInputStream(
                    new File(directory, name + SUFFIX_PHOTO)));

        } catch (FileNotFoundException ex){
            Log.d("### PhotoHandler", "File not found");
        }
        return image;
    }

    public Bitmap readImageThumbnail(String name){
        //read the thumbnail version of the image
        Bitmap image = null;
        Log.d("### PhotoHandler", "Reading " + PREFIX_THUMBNAIL + name + SUFFIX_PHOTO);
        try {
            image = BitmapFactory.decodeStream(new FileInputStream(
                    new File(directory, PREFIX_THUMBNAIL + name + SUFFIX_PHOTO)));

        } catch (FileNotFoundException ex){
            Log.d("### PhotoHandler", "File not found");
        }
        return image;
    }

    public void deleteImage(String name){
        //delete the photo that has the name
        if((new File(directory, name + SUFFIX_PHOTO)).delete()) {
            Log.d("### PhotoHandler", "Delete " + name + SUFFIX_PHOTO);
        }
    }

    public void deleteImageThumbnail(String name){
        if((new File(directory, PREFIX_THUMBNAIL + name + SUFFIX_PHOTO)).delete()){
            Log.d("### PhotoHandler", "Delete " + PREFIX_THUMBNAIL + name + SUFFIX_PHOTO);
        }
    }

}
