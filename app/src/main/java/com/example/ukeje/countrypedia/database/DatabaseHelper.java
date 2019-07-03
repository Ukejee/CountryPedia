package com.example.ukeje.countrypedia.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/10/19
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "countrypedia";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context){

        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL("CREATE TABLE COUNTRY ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT,"
                + "FAVOURITES NUMERIC);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVerison, int newVersion){}
}
