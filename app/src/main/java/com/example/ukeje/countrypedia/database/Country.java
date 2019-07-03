package com.example.ukeje.countrypedia.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.ukeje.countrypedia.utils.TimestampConverter;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/11/19
 */

@Data
@Entity
public class Country implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String capital;
    private boolean favorite;

    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    private Date createdAt;

    @ColumnInfo(name = "modified_at")
    @TypeConverters(TimestampConverter.class)
    private Date modifiedAt;
}
