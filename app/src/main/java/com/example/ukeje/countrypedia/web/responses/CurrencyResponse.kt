package com.example.ukeje.countrypedia.web.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CurrencyResponse(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("symbol")
        val symbol: String = ""
) : Serializable
