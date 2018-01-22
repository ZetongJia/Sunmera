package luminosit.sunmera.activity;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import luminosit.sunmera.R;
import luminosit.sunmera.util.CalendarManager;
import luminosit.sunmera.util.DatabaseHelper;
import luminosit.sunmera.util.PhotoHandler;
import luminosit.sunmera.util.UserDatabase;

/*
* Created by 35 34 20 36 38 20 36 35 20 32 30 20 37 30 20 36 35 20
* 37 32 20 37 33 20 36 66 20 36 65 20 32 30 20 37 37 20
* 36 38 20 36 66 20 32 30 20 36 63 20 36 66 20 37 36 20
* 36 35 20 37 33 20 32 30 20 35 34 20 36 39 20 36 65 20
* 36 31 20 32 30 20 36 64 20 36 66 20 37 33 20 37 34 on 3/6/15
* */

public class TestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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

    public void save(View view){
        DatabaseHelper helper = new DatabaseHelper(this);
        String uid = getIDInput();

        helper.insertNewColumn(DatabaseHelper.COLUMN_UID, uid);
        helper.updateColumn(DatabaseHelper.COLUMN_TEXT, getTextInput(),
                DatabaseHelper.COLUMN_UID, uid);
    }

    public void retrieve(View view) {
        DatabaseHelper helper = new DatabaseHelper(this);

        print(helper.read(DatabaseHelper.COLUMN_TEXT,
                DatabaseHelper.COLUMN_UID,
                getIDInput()));
    }

    public void addUser(View view){
        UserDatabase userDatabase = new UserDatabase(this);
        userDatabase.addUser(getIDInput());
    }

    public void displayAllUsers(View view){
        UserDatabase userDatabase = new UserDatabase(this);
        printArray(userDatabase.getUserList());
    }

    public void extra(View view){
        DatabaseHelper helper = new DatabaseHelper(this);
        PhotoHandler handler = new PhotoHandler(this);

        Cursor cursor = helper.getReadableDatabase().query(DatabaseHelper.DATABASE_NAME,
                new String[]{DatabaseHelper.COLUMN_UID},
                null,
                null,
                null,
                null,
                DatabaseHelper.COLUMN_UID + " ASC");

        ArrayList<String> uidList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                uidList.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UID)));
            } while (cursor.moveToNext());
        }

        String[] list = uidList.toArray(new String[uidList.size()]);

        for (int i = 0; i < list.length; i++){
            handler.deleteImage(list[i]);
        }

        helper.deleteAll();
        helper.close();
        UserDatabase userDatabase = new UserDatabase(this);
        userDatabase.deleteAll();
    }

    public String getIDInput(){
        return ((EditText)findViewById(R.id.input_uid)).getText().toString();
    }

    public String getTextInput(){
        return ((EditText)findViewById(R.id.input_text)).getText().toString();
    }

    public void print(String string){
        ((TextView)findViewById(R.id.output)).setText(string);
    }

    public void printArray(String[] string){
        String text = "";
        for (int i = 0; i < string.length; i++){
            text += "," + string[i];
        }
        print(text);
    }
}

