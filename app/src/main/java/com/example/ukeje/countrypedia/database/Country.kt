package com.example.ukeje.countrypedia.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ukeje.countrypedia.utils.TimestampConverter
import java.io.Serializable
import java.util.*

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/11/19
 */

@Entity
data class Country(var name: String?,
                   var capital: String?,
                   var numericCode: Int?) : Serializable {

    constructor() : this("", "", 0)

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "created_at")
    @TypeConverters(TimestampConverter::class)
    var createdAt: Date? = null

    @ColumnInfo(name = "modified_at")
    @TypeConverters(TimestampConverter::class)
    var modifiedAt: Date? = null

}
