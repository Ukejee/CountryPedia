package com.example.ukeje.countrypedia.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/11/19
 */

@Dao
public interface DaoAccess {

    @Insert
    Long insertCountry(Country country);

    @Query("SELECT * FROM Country ORDER BY created_at desc")
    List<Country> fectchAllCountries();

    @Query("SELECT * FROM Country WHERE id =:countryId")
    Country getCountry(int countryId);

    @Query("SELECT id FROM Country WHERE name =:countryName")
    int getCountryId(String countryName);

    @Query("SELECT * FROM Country WHERE favorite =:isFavorite")
    List<Country> getFavoriteCountries(boolean isFavorite);

    @Update
    void updateCountry(Country country);

    @Delete
    void deleteCountry(Country country);
}
