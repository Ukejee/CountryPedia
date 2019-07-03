package com.example.ukeje.countrypedia.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/11/19
 */

@Database(entities = {Country.class}, version = 1, exportSchema = false)
public abstract class CountryDatabase extends RoomDatabase {

    public abstract DaoAccess daoAccess();
}
