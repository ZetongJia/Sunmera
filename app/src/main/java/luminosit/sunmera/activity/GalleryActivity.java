package luminosit.sunmera.activity;

import android.content.Intent;
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

public class GalleryActivity extends ActionBarActivity {

    //extras' keys
    public static final String EXTRA_PHOTO_LIST = "photo_list";

    String[] photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //get the list of photos that is going to be displayed
        photoList = getIntent().getStringArrayExtra(EXTRA_PHOTO_LIST);
    }

    @Override
    public void onResume(){
        super.onResume();

        //apply the adapter to the grid view
        GridView gridView = (GridView)findViewById(R.id.gallery_grid_view);
        gridView.setAdapter(new GridViewAdapter(this, photoList));

        //upon an item is clicked, open its specific view
        final Intent openSpecificViewIntent = new Intent(this, SpecificViewActivity.class);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openSpecificViewIntent.putExtra(SpecificViewActivity.EXTRA_UID, photoList[position]);
                startActivityForResult(openSpecificViewIntent, SpecificViewActivity.REQUEST_CODE_DELETE);
            }
        });
    }

    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data){
        //update the photoList, remove the deleted uid
        if(requestCode == SpecificViewActivity.REQUEST_CODE_DELETE
                && resultCode == RESULT_OK){
            ArrayList<String> list = new ArrayList<>();

            for(String uid : photoList){
                if(uid.equals(data.getStringExtra(SpecificViewActivity.EXTRA_DELETED_UID))){
                    continue;
                } else {
                    list.add(uid);
                }
            }
            photoList = list.toArray(new String[list.size()]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
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
