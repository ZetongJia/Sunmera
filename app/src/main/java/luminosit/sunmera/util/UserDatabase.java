package luminosit.sunmera.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by GaryTang on 5/9/15.
 */
public class UserDatabase extends SQLiteOpenHelper {

    //version and name
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "luminosit_trace_database_users";

    //columns
    public static final String COLUMN_ENTRY_ID = "_id";
    public static final String COLUMN_USER_NAME = "user_name";

    //table format
    public static final String DATABASE_FORMAT = "CREATE TABLE " + DATABASE_NAME + " ("
            + COLUMN_ENTRY_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_USER_NAME + " TEXT)";

    public UserDatabase (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_FORMAT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + DATABASE_NAME);
        onCreate(db);
    }

    public void addUser(String username){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, username);

        //add a new user into the database
        getWritableDatabase().insert(DATABASE_NAME,
                null,
                values);

        close();
    }

    public void deleteUser(String username){
        //delete the specific username
        getWritableDatabase().delete(DATABASE_NAME,
                COLUMN_USER_NAME + " = ?",
                new String[]{username});
        close();
    }

    public String[] getUserList(){
        //return all the user and their id
        Cursor cursor = getReadableDatabase().query(DATABASE_NAME,
                new String[]{COLUMN_USER_NAME},
                null,
                null,
                null,
                null,
                COLUMN_ENTRY_ID + " ASC"
        );

        //put the result into a array
        ArrayList<String> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)));
            } while (cursor.moveToNext());
        }
        close();

        return list.toArray(new String[list.size()]);
    }

    public void deleteAll() {
        //delete the entire database
        getWritableDatabase().delete(DATABASE_NAME,
                null,
                null);

        close();
    }
}
