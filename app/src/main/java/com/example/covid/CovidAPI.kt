package com.example.covid;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path

interface CovidAPI {
    @GET("countries")
    fun getCountries() : Call<List<ResultGetCountries>>

    @GET("dayone/country/{country}")
    fun getDayOne(@Path("country") country : String ): Call<List<ResultGetDayOne>>
}
