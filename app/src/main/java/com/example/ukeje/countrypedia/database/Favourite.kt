package com.example.ukeje.countrypedia.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/11/19
 */

@Entity
data class Favourite(@PrimaryKey
                     var numericCode: String) : Serializable {

    constructor() : this("")

    companion object {
        fun buildFavouriteWithCountry(country: Country): Favourite {
            return Favourite(country.numericCode!!)
        }
    }

}
