package com.example.covid

import com.google.gson.annotations.SerializedName
import java.util.*

data class ResultGetDayOne (
    @SerializedName("Country")
    var country : String,
    @SerializedName("Confirmed")
    var cases: Int,
    @SerializedName("Deaths")
    var deaths: Int,
    @SerializedName("Recovered")
    var recovered: Int,
    @SerializedName("Date")
    var date: Date
)