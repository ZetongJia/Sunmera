package luminosit.sunmera.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import luminosit.sunmera.R;
import luminosit.sunmera.util.CalendarManager;
import luminosit.sunmera.util.DatabaseHelper;
import luminosit.sunmera.util.PhotoHandler;

public class CameraActivity extends ActionBarActivity {

    //request code
    static final int REQUEST_CODE_TAKE_PHOTO = 1;
    static final int REQUEST_CODE_FROM_GALLERY = 2;

    //temporary storage place
    static final Uri tempUri = Uri.fromFile(new File(Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "temp"));

    //recent taken photo
    Bitmap photo;

    //UI
    Dialog setFeelingDialog;
    Dialog inputPhotoDialog;

    //photo attributes
    String uid;
    String text;
    String mood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //disable the up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //dialog for choosing input type
        inputPhotoDialog = new Dialog(this);
        inputPhotoDialog.setContentView(R.layout.dialog_input_method);
        inputPhotoDialog.setTitle(R.string.str_title_input_type);

        //define listeners for the buttons
        inputPhotoDialog.findViewById(R.id.button_take_a_photo)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputPhotoDialog.dismiss();
                        openCamera();
                    }
                });

        inputPhotoDialog.findViewById(R.id.button_choose_from_gallery)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputPhotoDialog.dismiss();
                        chooseFromGallery();
                    }
                });

        inputPhotoDialog.findViewById(R.id.button_cancel_choosing_input)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputPhotoDialog.dismiss();
                        finish();
                    }
                });

        inputPhotoDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void chooseFromGallery(){
        Intent chooseFromGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFromGalleryIntent.setType("image/*");
        startActivityForResult(chooseFromGalleryIntent, REQUEST_CODE_FROM_GALLERY);
    }

    public void openCamera(){
        //implicit intent launching the camera
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //check if there is at least one activity can handle this intent
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null){
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            startActivityForResult(takePhotoIntent, REQUEST_CODE_TAKE_PHOTO);
        } else {
            Toast.makeText(this, "No camera application found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK){
            //unwrap the bundle to get the bitmap picture

            ContentResolver resolver = getContentResolver();
            resolver.notifyChange(tempUri, null);
            try {
                photo = MediaStore.Images.Media.getBitmap(resolver, tempUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //photo = (Bitmap) data.getExtras().get("data");

            //show the image in the preview
            ((ImageView) findViewById(R.id.image_preview)).setImageBitmap(photo);
        }
        if(requestCode == REQUEST_CODE_FROM_GALLERY && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((ImageView) findViewById(R.id.image_preview)).setImageBitmap(photo);
        }
    }

    public void saveImage(View view){

        final Context activityContext = this;

        //construct a new dialog
        setFeelingDialog = new Dialog(this);
        setFeelingDialog.setContentView(R.layout.dialog_set_emotion);
        setFeelingDialog.setTitle(R.string.str_title_how_you_feel);

        //construct the seek bar
        final SeekBar feelingSeekBar = (SeekBar)setFeelingDialog
                .findViewById(R.id.dialog_set_feeling_seekbar);
        feelingSeekBar.setMax(5); //max progress: start from 0 to 5

        //initialize the seek bar
        feelingSeekBar.setProgress(feelingSeekBar.getMax());
        updateEmoji(feelingSeekBar.getMax());

        //listeners for dialog buttons
        setFeelingDialog.findViewById(R.id.dialog_set_feeling_button_ok)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //retrieve feeling
                        mood = String.valueOf(feelingSeekBar.getProgress());

                        //save photo
                        savePhoto();

                        //finishing up
                        setFeelingDialog.dismiss();
                        Toast.makeText(activityContext,
                                R.string.str_toast_saved,
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

        setFeelingDialog.findViewById(R.id.dialog_set_feeling_button_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setFeelingDialog.dismiss();
                    }
                });

        //listeners for seek bar
        feelingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //update the emoji
                updateEmoji(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //if no photo is taken, simply exit the activity
        if(photo != null) {
            setFeelingDialog.show(); //pop up
        } else {
            finish();
        }
    }

    public void savePhoto(){
        PhotoHandler handler = new PhotoHandler(this);
        DatabaseHelper helper = new DatabaseHelper(this);
        CalendarManager manager = new CalendarManager();

        //save the photo
        uid = MainActivity.username + DatabaseHelper.UID_NAME_SEPARATOR
                + manager.getTodayToSeconds(); //username + "_" + time to seconds
        text = ((EditText)findViewById(R.id.text_camera_input_note)).getText().toString();
        handler.saveImage(photo, uid);

        //save a thumbnail version of the image for better performance
        handler.saveImage(Bitmap.createScaledBitmap(photo, PhotoHandler.WIDTH, PhotoHandler.HEIGHT, true),
                PhotoHandler.PREFIX_THUMBNAIL + uid);

        //register the uid and save the text, mood
        helper.insertNewColumn(DatabaseHelper.COLUMN_UID, uid);

        helper.updateColumn(DatabaseHelper.COLUMN_TEXT,
                text,
                DatabaseHelper.COLUMN_UID,
                uid);

        helper.updateColumn(DatabaseHelper.COLUMN_MOOD,
                mood,
                DatabaseHelper.COLUMN_UID,
                uid);
    }

    public void updateEmoji(int progress){
        //emoji view
        ImageView emoji = (ImageView)setFeelingDialog.findViewById(R.id.dialog_set_feeling_emoji);

        switch (progress){
            //update the emoji regarding the progress
            case 0:
                emoji.setImageResource(R.drawable.sad);
                break;
            case 1:
                emoji.setImageResource(R.drawable.bored);
                break;
            case 2:
                emoji.setImageResource(R.drawable.happy);
                break;
            case 3:
                emoji.setImageResource(R.drawable.excited);
                break;
            case 4:
                emoji.setImageResource(R.drawable.excellent);
                break;
            case 5:
                emoji.setImageResource(R.drawable.grateful);
                break;
        }
    }

    public void discard(View view){
        finish();
    }

    public void retakePhoto(View view){
        inputPhotoDialog.show();
    }

}
