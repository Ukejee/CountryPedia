package com.example.ukeje.countrypedia.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/11/19
 */

@Entity
data class Favourite(var name: String?, var numericCode: Int?,
                     @PrimaryKey
                     @ColumnInfo(name = "country_id")
                     val id: String = UUID.randomUUID().toString()) : Serializable {

    constructor() : this("", 0)

    companion object {
        fun buildFavouriteWithCountry(country: Country): Favourite {
            return Favourite(country.name, country.numericCode)
        }
    }

}
