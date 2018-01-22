package luminosit.sunmera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import luminosit.sunmera.R;
import luminosit.sunmera.ui.GridViewAdapter;
import luminosit.sunmera.util.DatabaseHelper;
import luminosit.sunmera.util.PhotoHandler;

public class PosterGalleryActivity extends ActionBarActivity {

    public static final String EXTRA_RESULT_UID = "result_uid";

    String uidChosen;
    String[] uidList;

    Intent result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_gallery);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onResume(){
        super.onResume();
        final DatabaseHelper helper = new DatabaseHelper(this);

        //attach the adapter
        uidList = helper.getUIDListByUser(MainActivity.username);
        GridView gridView = (GridView)findViewById(R.id.gallery_poster_grid_view);
        gridView.setAdapter(new GridViewAdapter(this, uidList));

        //listener
        setResult(RESULT_CANCELED);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //return the selected image's uid as a result
                uidChosen = uidList[position];

                //storing the image in an intent and then return it
                result = new Intent();
                result.putExtra(EXTRA_RESULT_UID, uidChosen);
                setResult(RESULT_OK, result);

                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poster_gallery, menu);
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
}
