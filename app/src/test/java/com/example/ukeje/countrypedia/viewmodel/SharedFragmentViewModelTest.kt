package com.example.ukeje.countrypedia.viewmodel

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 8/20/19
 */
class SharedFragmentViewModelTest {

    @Before
    @Throws(Exception::class)
    fun setUp(){
        throw Exception()
    }

    @Test
    fun setSplitTimeZones() {
    }

    @Test
    fun setTimeZonesFirstHalf() {
    }

    @Test
    fun formatStringListIntoString(){

        val testList = listOf("Apple","Pineapple","Fish")
        val testSeparator = "\n"

        val testFormattedString = StringBuilder()

        testList.forEachIndexed { index, string ->

            testFormattedString.append(string)

            if (testList.size > 1 && index < testList.lastIndex)
                testFormattedString.append(testSeparator)

        }

        assertEquals("Apple\nPineapple\nFish", testFormattedString.toString())
    }
}