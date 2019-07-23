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
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavourite(favourite: Favourite): Completable

    @Update
    fun updateFavourite(favourite: Favourite): Completable

    @Delete
    fun deleteFavourite(favourite: Favourite): Completable

    @Query("SELECT * FROM Favourite")
    fun fetchAllFavourites(): Flowable<List<Favourite>>

    @Query("SELECT * FROM Favourite WHERE numericCode =:numericCode")
    fun fetchFavouriteByNumericCode(numericCode: Int): Flowable<Favourite>

}
