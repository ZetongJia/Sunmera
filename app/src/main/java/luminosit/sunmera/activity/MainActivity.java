package luminosit.sunmera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import luminosit.sunmera.R;
import luminosit.sunmera.fragment.CalendarFragment;
import luminosit.sunmera.fragment.CategoryFragment;
import luminosit.sunmera.fragment.PosterFragment;
import luminosit.sunmera.util.CalendarManager;
import luminosit.sunmera.util.DatabaseHelper;
import luminosit.sunmera.util.MyTabListener;
import luminosit.sunmera.util.PhotoHandler;


public class MainActivity extends ActionBarActivity{

    //stock image's uidList
    static final String UID_STOCK_IMAGE_0 = "19970623_000000";
    static final String UID_STOCK_IMAGE_1 = "31415926_000000";
    static final String UID_STOCK_IMAGE_2 = "20121221_000000";
    static final String UID_STOCK_IMAGE_3 = "11235813_000000";

    static final String[] LIST_UID_STOCK_IMAGE = {UID_STOCK_IMAGE_0,
            UID_STOCK_IMAGE_1, UID_STOCK_IMAGE_2, UID_STOCK_IMAGE_3};

    //current user name
    public static String username;

    //extras' names
    public static final String EXTRA_USER_NAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the logged in user's name
        username = getIntent().getStringExtra(EXTRA_USER_NAME);

        //action bar setting
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //tabs' labels configs
        ActionBar.Tab tabCalendar, tabCategory, tabPoster;
        tabCalendar = actionBar.newTab().setText(R.string.str_tab_calendar);
        tabCategory = actionBar.newTab().setText(R.string.str_tab_category);
        tabPoster = actionBar.newTab().setText(R.string.str_tab_poster);

        //correspond fragments to tabs
        tabCalendar.setTabListener(new MyTabListener(new CalendarFragment()));
        tabCategory.setTabListener(new MyTabListener(new CategoryFragment()));
        tabPoster.setTabListener(new MyTabListener(new PosterFragment()));

        //add the tabs to action bar
        actionBar.addTab(tabCalendar);
        actionBar.addTab(tabCategory);
        actionBar.addTab(tabPoster);

        //disable the up button
        actionBar.setDisplayHomeAsUpEnabled(false);

        //saving the stock images in if not exist already
        //currently commented out because a memory overflow issue
        //saveStockImages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_take_pic:
                openCamera();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openCamera(){
        //explicit intent start the camera activity
        Intent cameraIntent = new Intent(this, CameraActivity.class);
        startActivity(cameraIntent);
    }

    /*
    public void saveStockImages(){
        PhotoHandler handler = new PhotoHandler(this);

        //check if the stock images have already existed if not, save the images
        if (handler.readImage(UID_STOCK_IMAGE_0) == null){
            savePicture(UID_STOCK_IMAGE_0,
                    BitmapFactory.decodeResource(getResources(), R.drawable.stock_image_0));
        }
        if (handler.readImage(UID_STOCK_IMAGE_1) == null){
            savePicture(UID_STOCK_IMAGE_1,
                    BitmapFactory.decodeResource(getResources(), R.drawable.stock_image_1));
        }
        if (handler.readImage(UID_STOCK_IMAGE_2) == null){
            savePicture(UID_STOCK_IMAGE_2,
                    BitmapFactory.decodeResource(getResources(), R.drawable.stock_image_2));
        }
        if (handler.readImage(UID_STOCK_IMAGE_3) == null){
            savePicture(UID_STOCK_IMAGE_3,
                    BitmapFactory.decodeResource(getResources(), R.drawable.stock_image_3));
        }
    }
    */

    public void savePicture(String uid, Bitmap bitmap){
        DatabaseHelper helper = new DatabaseHelper(this);
        PhotoHandler handler = new PhotoHandler(this);

        helper.insertNewColumn(DatabaseHelper.COLUMN_UID, uid);

        handler.saveImage(bitmap, uid);
    }
}
