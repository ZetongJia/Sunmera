package luminosit.sunmera.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import luminosit.sunmera.R;
import luminosit.sunmera.util.DatabaseHelper;
import luminosit.sunmera.util.PhotoHandler;

public class SpecificViewActivity extends ActionBarActivity {

    //photo's uid extra
    public static final String EXTRA_UID = "uid";
    public static final String EXTRA_DELETED_UID = "deleted_uid";

    public static final int REQUEST_CODE_DELETE = 999;

    //ui
    ImageView imageView;
    EditText editText;

    //uid
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_view);

        PhotoHandler handler = new PhotoHandler(this);
        DatabaseHelper helper = new DatabaseHelper(this);

        //the result code will be "canceled" if no photo is deleted
        setResult(RESULT_CANCELED);

        //get the uid extra
        uid = getIntent().getStringExtra(EXTRA_UID);

        //set the photo and notes to the view
        imageView = (ImageView)findViewById(R.id.image_specific_view);
        imageView.setImageBitmap(handler.readImage(uid));
        editText = (EditText)findViewById(R.id.text_box_specific_view);
        editText.setText(helper.read(DatabaseHelper.COLUMN_TEXT, DatabaseHelper.COLUMN_UID, uid));

        //disable the up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void saveThisView(View view){
        DatabaseHelper helper = new DatabaseHelper(this);
        //update the input text
        helper.updateColumn(DatabaseHelper.COLUMN_TEXT,
                editText.getText().toString(),
                DatabaseHelper.COLUMN_UID,
                uid);

        Toast.makeText(this, R.string.str_toast_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void cancel(View view){
        finish();
    }

    public void deleteThisView(View view){
        final Dialog confirmDialog = new Dialog(this);
        confirmDialog.setContentView(R.layout.dialog_ok_cancel);
        confirmDialog.setTitle(R.string.str_title_confirm);

        confirmDialog.show();

        //set listeners
        confirmDialog.findViewById(R.id.dialog_button_ok)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmDialog.dismiss();
                        delete();
                    }
                });

        confirmDialog.findViewById(R.id.dialog_button_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmDialog.dismiss();
                    }
                });

    }

    public void delete(){
        DatabaseHelper helper = new DatabaseHelper(this);
        PhotoHandler handler = new PhotoHandler(this);

        //do delete photo, and returns the deleted uid
        Intent result = new Intent().putExtra(EXTRA_DELETED_UID, uid);
        setResult(RESULT_OK, result);

        //delete current specific view at database
        helper.deleteRowBy(DatabaseHelper.COLUMN_UID, uid);
        Toast.makeText(this, R.string.str_toast_deleted, Toast.LENGTH_SHORT).show();

        //delete the photo
        handler.deleteImage(uid);
        handler.deleteImageThumbnail(uid);

        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_specific_view, menu);
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
