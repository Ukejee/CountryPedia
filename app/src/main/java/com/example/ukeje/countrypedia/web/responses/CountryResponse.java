package com.example.ukeje.countrypedia.web.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class CountryResponse implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("topLevelDomain")
    @Expose
    private List<String> topLevelDomain = null;
    @SerializedName("alpha2Code")
    @Expose
    private String alpha2Code;
    @SerializedName("alpha3Code")
    @Expose
    private String alpha3Code;
    @SerializedName("callingCodes")
    @Expose
    private List<String> callingCodes = null;
    @SerializedName("capital")
    @Expose
    private String capital;
    @SerializedName("altSpellings")
    @Expose
    private List<String> altSpellings = null;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("subregion")
    @Expose
    private String subregion;
    @SerializedName("population")
    @Expose
    private Integer population;
    @SerializedName("latlng")
    @Expose
    private List<Float> latlng = null;
    @SerializedName("demonym")
    @Expose
    private String demonym;
    @SerializedName("area")
    @Expose
    private Float area;
    @SerializedName("gini")
    @Expose
    private Object gini;
    @SerializedName("timezones")
    @Expose
    private List<String> timezones = null;
    @SerializedName("borders")
    @Expose
    private List<String> borders = null;
    @SerializedName("nativeName")
    @Expose
    private String nativeName;
    @SerializedName("numericCode")
    @Expose
    private String numericCode;
    @SerializedName("currencies")
    @Expose
    private List<CurrencyResponse> currencies = null;
    @SerializedName("languages")
    @Expose
    private List<LanguageResponse> languages = null;
    @SerializedName("translations")
    @Expose
    private TranslationsResponse translations;
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("regionalBlocs")
    @Expose
    private List<RegionalBlocResponse> regionalBlocs = null;
    @SerializedName("cioc")
    @Expose
    private String cioc;
}
