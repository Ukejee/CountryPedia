package com.example.ukeje.countrypedia.web.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CountryResponse(@SerializedName("name")
                           var name: String,
                           @SerializedName("topLevelDomain")
                           var topLevelDomain: List<String>,
                           @SerializedName("alpha2Code")
                           var alpha2Code: String,
                           @SerializedName("alpha3Code")
                           var alpha3Code: String,
                           @SerializedName("callingCodes")
                           var callingCodes: List<String>,
                           @SerializedName("capital")
                           var capital: String,
                           @SerializedName("altSpellings")
                           var altSpellings: List<String>,
                           @SerializedName("region")
                           var region: String,
                           @SerializedName("subregion")
                           var subregion: String,
                           @SerializedName("population")
                           var population: Int,
                           @SerializedName("latlng")
                           var latlng: List<Float>,
                           @SerializedName("demonym")
                           var demonym: String,
                           @SerializedName("area")
                           var area: Float,
                           @SerializedName("gini")
                           var gini: Any,
                           @SerializedName("timezones")
                           var timezones: List<String>,
                           @SerializedName("borders")
                           var borders: List<String>,
                           @SerializedName("nativeName")
                           var nativeName: String,
                           @SerializedName("numericCode")
                           var numericCode: String,
                           @SerializedName("currencies")
                           var currencies: List<CurrencyResponse>,
                           @SerializedName("languages")
                           var languages: List<LanguageResponse>,
                           @SerializedName("translations")
                           var translations: TranslationsResponse,
                           @SerializedName("flag")
                           var flag: String,
                           @SerializedName("regionalBlocs")
                           var regionalBlocs: List<RegionalBlocResponse>,
                           @SerializedName("cioc")
                           var cioc: String) : Serializable
