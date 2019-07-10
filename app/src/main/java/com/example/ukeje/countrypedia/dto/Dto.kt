package com.example.ukeje.countrypedia.dto

import java.io.Serializable

/**
 * @author .: Oriaghan Uyi
 * @email ..: uyioriaghan@gmail.com, uyi.oriaghan@cwg-plc.com
 * @created : 2019-07-09 17:09
 */

data class HomeNavItem @JvmOverloads constructor(val name: String, val position: Int, var id: Int = 0) : Serializable{

    companion object {
        const val HOME_POSITION = 0
        const val REGION_POSITION = 1
        const val FAVORITE_POSITION = 2

    }

    override fun toString(): String {

        return "HomeNavItem(name=$name, position=$position, id=$id)"
    }
}