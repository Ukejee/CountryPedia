package com.example.ukeje.countrypedia.web.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class CountryResponse implements Serializable {

    @SerializedName("name")
    public String name;

    @SerializedName("topLevelDomain")
    public List<String> topLevelDomain = null;

    @SerializedName("alpha2Code")

    public String alpha2Code;
    @SerializedName("alpha3Code")

    public String alpha3Code;
    @SerializedName("callingCodes")

    public List<String> callingCodes = null;
    @SerializedName("capital")

    public String capital;
    @SerializedName("altSpellings")

    public List<String> altSpellings = null;
    @SerializedName("region")

    public String region;
    @SerializedName("subregion")

    public String subregion;
    @SerializedName("population")

    public Integer population;
    @SerializedName("latlng")

    public List<Float> latlng = null;
    @SerializedName("demonym")

    public String demonym;
    @SerializedName("area")

    public Float area;
    @SerializedName("gini")

    public Object gini;
    @SerializedName("timezones")

    public List<String> timezones = null;
    @SerializedName("borders")

    public List<String> borders = null;
    @SerializedName("nativeName")

    public String nativeName;
    @SerializedName("numericCode")

    public String numericCode;
    @SerializedName("currencies")

    public List<CurrencyResponse> currencies = null;
    @SerializedName("languages")

    public List<LanguageResponse> languages = null;
    @SerializedName("translations")

    public TranslationsResponse translations;
    @SerializedName("flag")

    public String flag;
    @SerializedName("regionalBlocs")

    public List<RegionalBlocResponse> regionalBlocs = null;
    @SerializedName("cioc")

    public String cioc;
}
