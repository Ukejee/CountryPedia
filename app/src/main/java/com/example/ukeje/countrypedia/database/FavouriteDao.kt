package com.example.ukeje.countrypedia.database

import androidx.room.*

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/11/19
 */

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavourite(favourite: Favourite): Long?

    @Update
    fun updateFavourite(favourite: Favourite)

    @Delete
    fun deleteFavourite(favourite: Favourite)

    @Query("SELECT * FROM Favourite")
    fun fetchAllFavourites(): List<Favourite>

    @Query("SELECT * FROM Favourite WHERE numericCode =:numericCode")
    fun fetchFavouriteByNumericCode(numericCode: Int): Country

}
