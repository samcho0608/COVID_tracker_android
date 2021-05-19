package com.example.covid

import com.google.gson.annotations.SerializedName
import java.util.*

data class ResultGetDayOne (
    @SerializedName("Country")
    var country : String,
    @SerializedName("CountryCode")
    var countryCode : String,
    @SerializedName("Cases")
    var cases: Int,
    @SerializedName("Status")
    var status: String,
    @SerializedName("Date")
    var date: Date
)