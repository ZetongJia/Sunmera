package luminosit.sunmera.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import luminosit.sunmera.fragment.PosterFragment;

/**
 * Created by GaryTang on 3/6/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //version and name
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "luminosit_trace_database";

    //names of the columns of the table
    public static final String COLUMN_ENTRY_ID = "_id";
    public static final String COLUMN_UID = "uid";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_MOOD = "mood";

    //separator
    public static final String UID_NAME_SEPARATOR = "_";

    //format of the table
    public static final String FORMAT_DATABASE = "CREATE TABLE " + DATABASE_NAME + " ("
            + COLUMN_ENTRY_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_UID + " TEXT, "
            + COLUMN_TEXT + " TEXT, "
            + COLUMN_MOOD + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FORMAT_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + DATABASE_NAME);
        onCreate(db);
    }

    public void insertNewColumn(String column, String value) {
        //organizing values
        ContentValues values = new ContentValues();
        values.put(column, value);

        //insert a new value into designated column
        getWritableDatabase().insert(DATABASE_NAME,
                null,
                values);

        //close database
        close();
    }

    public void updateColumn(String column, String value, String column_selection, String value_selection) {
        //organizing values
        ContentValues values = new ContentValues();
        values.put(column, value);

        //update values at column_selected that has value_selected
        getWritableDatabase().update(DATABASE_NAME,
                values,
                column_selection + " = ?",
                new String[]{value_selection});

        //close database
        close();
    }

    public String read(String column_required, String column_selection, String value_selection) {
        //organizing values
        ContentValues values = new ContentValues();
        values.put(column_selection, value_selection);

        //sort order
        String sortOrder = column_required + " ASC";

        //query the database
        Cursor cursor = getReadableDatabase().query(DATABASE_NAME,
                new String[]{column_required},
                column_selection + " = ?",
                new String[]{value_selection},
                null,
                null,
                sortOrder);

        //read string from the first index
        cursor.moveToFirst();
        String value = cursor.getString(cursor.getColumnIndexOrThrow(column_required));

        //close database
        close();

        return value;
    }

    public void deleteRowBy(String column_selection, String value_selection) {
        //delete a specific row
        getWritableDatabase().delete(DATABASE_NAME,
                column_selection + " = ?",
                new String[]{value_selection});

        //close database
        close();
    }

    public void deleteAll(){
        //delete the entire database
        getWritableDatabase().delete(DATABASE_NAME,
                null,
                null);

        close();
    }

    public String[] getUIDListByUser(String user) {
        //returns all the uid belongs to that user
        Cursor cursor = getReadableDatabase().query(DATABASE_NAME,
                new String[]{COLUMN_UID},
                COLUMN_UID + " LIKE '" + user + "%'",
                null,
                null,
                null,
                COLUMN_UID + " ASC");

        ArrayList<String> uidList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                uidList.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UID)));
            } while (cursor.moveToNext());
        }

        close();
        return uidList.toArray(new String[uidList.size()]);
    }

    public String[] getUIDListByEmotion(String user, int mood){
        //returns a list of uid has the matching mood
        Cursor cursor = getReadableDatabase().query(DATABASE_NAME,
                new String[]{COLUMN_UID},
                COLUMN_MOOD + " = ?",
                new String[]{String.valueOf(mood)},
                null,
                null,
                COLUMN_UID + " ASC");

        ArrayList<String> uidList = new ArrayList<>();
        if (cursor.moveToFirst()){
            String item;
            do{
                item = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UID));
                //filter other users out
                if (item.split(UID_NAME_SEPARATOR)[0].equals(user)){
                    uidList.add(item);
                }
            } while (cursor.moveToNext());
        }

        close();
        return uidList.toArray(new String[uidList.size()]);
    }

    public String[] getUsersPoster(String user) {
        //returns a list of posters that the user has
        Cursor cursor = getReadableDatabase().query(DATABASE_NAME,
                new String[]{COLUMN_UID},
                COLUMN_UID + " LIKE '" + PosterFragment.PREFIX_POSTER_FILE + "%'",
                null,
                null,
                null,
                COLUMN_UID + " ASC");

        ArrayList<String> uidList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            String item;
            do {
                item = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UID));
                //filter out other users' posters
                if (item.split(UID_NAME_SEPARATOR)[1].equals(user)) {
                    uidList.add(item);
                }
            } while (cursor.moveToNext());
        }

        close();
        return uidList.toArray(new String[uidList.size()]);
    }
}

