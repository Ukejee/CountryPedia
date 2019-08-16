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



//TODO: Add flag, region
@Entity
data class Country(var name: String?,
                   var capital: String?,
                   var numericCode: String?,
                   @PrimaryKey
                   @ColumnInfo(name = "country_id")
                   val id: String = UUID.randomUUID().toString()) : Serializable {

    constructor() : this("", "", "0")

}
