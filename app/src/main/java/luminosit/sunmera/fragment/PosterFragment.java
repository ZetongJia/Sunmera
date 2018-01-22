package luminosit.sunmera.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import luminosit.sunmera.R;
import luminosit.sunmera.activity.GalleryActivity;
import luminosit.sunmera.activity.MainActivity;
import luminosit.sunmera.activity.PosterGalleryActivity;
import luminosit.sunmera.util.CalendarManager;
import luminosit.sunmera.util.DatabaseHelper;
import luminosit.sunmera.util.PhotoHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class PosterFragment extends Fragment {

    //request codes
    public static final int REQUEST_CODE_CHOOSE_HAPPIEST = 0;
    public static final int REQUEST_CODE_CHOOSE_EXCITING = 1;
    public static final int REQUEST_CODE_CHOOSE_AWESOME = 2;
    public static final int REQUEST_CODE_CHOOSE_GRATEFUL = 3;

    //uids
    private String happiest = null,
            exciting = null,
            awesome = null,
            grateful = null;

    //filename
    public static final String PREFIX_POSTER_FILE = "poster_";

    View fragmentView;
    Intent openGalleryIntent;

    public static final int SCALING_FACTOR = 200;
    public static final int RATIO_WIDTH = 3;
    public static final int RATIO_HEIGHT = 4;

    public PosterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_poster, container, false);

        //set listeners for each image button
        openGalleryIntent = new Intent(getActivity(), PosterGalleryActivity.class);
        fragmentView.findViewById(R.id.button_poster_happiest)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(openGalleryIntent, REQUEST_CODE_CHOOSE_HAPPIEST);
                    }
                });

        fragmentView.findViewById(R.id.button_poster_exciting)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(openGalleryIntent, REQUEST_CODE_CHOOSE_EXCITING);
                    }
                });

        fragmentView.findViewById(R.id.button_poster_awesome)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(openGalleryIntent, REQUEST_CODE_CHOOSE_AWESOME);
                    }
                });

        fragmentView.findViewById(R.id.button_poster_grateful)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(openGalleryIntent, REQUEST_CODE_CHOOSE_GRATEFUL);

                    }
                });

        //listeners for submit and view poster
        fragmentView.findViewById(R.id.button_poster_submit)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //only if the user has filled out all the items
                        if(happiest != null
                                && exciting != null
                                && awesome != null
                                && grateful != null) {
                            submit();
                        }else{
                            Toast.makeText(getActivity(),
                                    R.string.str_poster_not_completed,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        fragmentView.findViewById(R.id.button_poster_saved_photo)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadSavedPoster();
                    }
                });

        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        PhotoHandler handler = new PhotoHandler(getActivity());
        //get the image extra
        String result;

        //if the activity is good, get the picture
        if (resultCode == PosterGalleryActivity.RESULT_OK) {
            result = data.getStringExtra(PosterGalleryActivity.EXTRA_RESULT_UID);

        //setting the previews
            if (requestCode == REQUEST_CODE_CHOOSE_HAPPIEST) {
                happiest = result;
                ((ImageView) fragmentView.findViewById(R.id.button_poster_happiest))
                        .setImageBitmap(handler.readImageThumbnail(happiest));
            }
            if (requestCode == REQUEST_CODE_CHOOSE_EXCITING) {
                exciting = result;
                ((ImageView) fragmentView.findViewById(R.id.button_poster_exciting))
                        .setImageBitmap(handler.readImageThumbnail(exciting));
            }

            if (requestCode == REQUEST_CODE_CHOOSE_AWESOME) {
                awesome = result;
                ((ImageView) fragmentView.findViewById(R.id.button_poster_awesome))
                        .setImageBitmap(handler.readImageThumbnail(awesome));
            }
               if (requestCode == REQUEST_CODE_CHOOSE_GRATEFUL) {
                grateful = result;
                ((ImageView) fragmentView.findViewById(R.id.button_poster_grateful))
                        .setImageBitmap(handler.readImageThumbnail(grateful));
            }
        }

    }

    public void submit(){
        DatabaseHelper helper = new DatabaseHelper(getActivity());
        PhotoHandler handler = new PhotoHandler(getActivity()); //poster directory
        CalendarManager manager = new CalendarManager();

        int scaleX = RATIO_WIDTH * SCALING_FACTOR;
        int scaleY = RATIO_HEIGHT * SCALING_FACTOR;

        Bitmap happiestPic = Bitmap.createScaledBitmap(handler.readImage(happiest), scaleX, scaleY, true),
                excitingPic = Bitmap.createScaledBitmap(handler.readImage(exciting), scaleX, scaleY, true),
                awesomePic = Bitmap.createScaledBitmap(handler.readImage(awesome), scaleX, scaleY, true),
                gratefulPic = Bitmap.createScaledBitmap(handler.readImage(grateful), scaleX, scaleY, true);

        //making a big blank image
        int width = happiestPic.getWidth();
        int height = happiestPic.getHeight();

        Bitmap bigImage = Bitmap.createBitmap(width * 2,
                height * 2,
                Bitmap.Config.ARGB_8888);

        //canvas for the big image
        Canvas canvas = new Canvas(bigImage);

        //draw all the images in and rescale the big picture
        canvas.drawBitmap(happiestPic, 0f, 0f, null);
        canvas.drawBitmap(excitingPic, width, 0f, null);
        canvas.drawBitmap(awesomePic, 0f, height, null);
        canvas.drawBitmap(gratefulPic, width, height, null);

        Bitmap bigImageScaled = Bitmap.createScaledBitmap(bigImage, width, height, true);

        //save the photo and register the uid + thumbnail
        String time = manager.getTodayToSeconds();
        String uid = PREFIX_POSTER_FILE + MainActivity.username
                + DatabaseHelper.UID_NAME_SEPARATOR + time;//+username

        handler.saveImage(bigImageScaled, uid);
        handler.saveImage(Bitmap.createScaledBitmap(bigImageScaled, 80, 80, true), PhotoHandler.PREFIX_THUMBNAIL + uid);

        helper.insertNewColumn(DatabaseHelper.COLUMN_UID, uid);

        //notification
        Toast.makeText(getActivity(), R.string.str_toast_saved, Toast.LENGTH_SHORT).show();
    }

    public void loadSavedPoster(){
        //start the saved poster activity
        DatabaseHelper helper = new DatabaseHelper(getActivity());
        Intent savedPosterIntent = new Intent(getActivity(), GalleryActivity.class);
        savedPosterIntent.putExtra(GalleryActivity.EXTRA_PHOTO_LIST,
                helper.getUsersPoster(MainActivity.username));
        startActivity(savedPosterIntent);
    }
}
