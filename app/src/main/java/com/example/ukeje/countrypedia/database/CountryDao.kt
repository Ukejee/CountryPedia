package com.example.ukeje.countrypedia.database

import androidx.room.*

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/11/19
 */

@Dao
interface CountryDao {

    @Insert
    fun insertCountry(country: Country): Long?

    @Query("SELECT * FROM Country ORDER BY created_at desc")
    fun fetchAllCountries(): List<Country>

    @Query("SELECT * FROM Country WHERE id =:countryId")
    fun fetchCountryById(countryId: Int): Country

    @Query("SELECT * FROM Country WHERE name =:countryName")
    fun fetchCountryByName(countryName: String): Country

    @Query("SELECT id FROM Country WHERE name =:countryName")
    fun fetchCountryIdByName(countryName: String): Int

    @Update
    fun updateCountry(country: Country)

    @Delete
    fun deleteCountry(country: Country)

}
