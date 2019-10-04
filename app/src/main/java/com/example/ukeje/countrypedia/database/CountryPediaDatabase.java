package com.example.ukeje.countrypedia.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @author .: Oriaghan Uyi
 * @email ..: uyioriaghan@gmail.com, uyi.oriaghan@cwg-plc.com
 * @created : 2019-07-12 15:30
 */
@Database(entities = {Country.class, Favourite.class}, version = 2, exportSchema = false)
public abstract class CountryPediaDatabase extends RoomDatabase {

    public abstract CountryDao countryDao();

    public abstract FavouriteDao favoriteDao();
}
