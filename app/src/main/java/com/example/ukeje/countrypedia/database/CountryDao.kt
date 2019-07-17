package com.example.ukeje.countrypedia.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/11/19
 */

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountry(country: Country) : Completable

    @Update
    fun updateCountry(country: Country) : Completable

    @Delete
    fun deleteCountry(country: Country) : Completable

    @Query("SELECT * FROM Country")
    fun fetchAllCountries(): Flowable<List<Country>>

    @Query("SELECT * FROM Country WHERE numericCode =:countryNumericCode")
    fun fetchCountryByNumericCode(countryNumericCode: Int): Flowable<Country>

    @Query("SELECT * FROM Country WHERE name =:countryName")
    fun fetchCountryByName(countryName: String): Flowable<Country>

}
