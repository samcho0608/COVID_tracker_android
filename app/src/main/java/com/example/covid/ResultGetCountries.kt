package com.example.covid

import com.google.gson.annotations.SerializedName

data class ResultGetCountries (
    @SerializedName("Country")
    var country : String,
    @SerializedName("Slug")
    var slug : String,
    @SerializedName("ISO2")
    var iso2 : String
)