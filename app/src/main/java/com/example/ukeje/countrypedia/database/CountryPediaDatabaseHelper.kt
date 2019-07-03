package com.example.ukeje.countrypedia.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.ukeje.countrypedia.CountryRepository
import com.example.ukeje.countrypedia.CountryRepository.BASE_URL
import com.example.ukeje.countrypedia.web.CountryPediaApiService
import com.example.ukeje.countrypedia.web.helper.ApiCallHelper
import com.example.ukeje.countrypedia.web.responses.CountryResponse
import com.example.ukeje.countrypedia.web.responses.ErrorResponse


/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/10/19
 */
class CountryPediaDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME,
        null, DB_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("CREATE TABLE COUNTRY ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT,"
                + "FAVOURITES NUMERIC);")




    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {

        const val DB_NAME = "countrypedia"
        const val DB_VERSION = 1
       // val BASE_URL = "https://restcountries.eu/rest/v2/"
    }
}

