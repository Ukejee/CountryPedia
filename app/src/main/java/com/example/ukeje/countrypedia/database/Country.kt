package com.example.ukeje.countrypedia.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ukeje.countrypedia.utils.TimestampConverter
import lombok.Data
import java.io.Serializable
import java.util.*

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/11/19
 */

@Data
@Entity
class Country : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
        set(id) {
            field = this.id
        }

    var name: String? = null
        set(name) {
            field = this.name
        }
    var capital: String? = null
        set(capital) {
            field = this.capital
        }
    var numericCode: Int? = null
        set(numericCode) {
            field = this.numericCode
        }

    @ColumnInfo(name = "created_at")
    @TypeConverters(TimestampConverter::class)
    var createdAt: Date? = null
        set(createdAt) {
            field = this.createdAt
        }

    @ColumnInfo(name = "modified_at")
    @TypeConverters(TimestampConverter::class)
    var modifiedAt: Date? = null
        set(modifiedAt) {
            field = this.modifiedAt
        }
}
