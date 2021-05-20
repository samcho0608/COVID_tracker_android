package com.example.covid

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    // API variables
    private val BASE_URL = "https://api.covid19api.com/"
    private lateinit var retrofit: Retrofit
    private lateinit var api : CovidAPI

    // View
    private lateinit var countrySpinner : Spinner
    private lateinit var sortBySpinner : Spinner
    private lateinit var statsRecycler : RecyclerView

    // Info Variables
    private lateinit var countries : Map<String, String>
    private var dailyData : ArrayList<DailyData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // FINDING VIEWS
        countrySpinner = findViewById(R.id.countries)
        sortBySpinner = findViewById(R.id.sortBy)
        statsRecycler = findViewById(R.id.stats)


        // HTTP REQUEST SET UP
        // build retrofit to the desired URL
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(CovidAPI::class.java)


        // POPULATION
        // countrySpinner
        populateCountries()

        // sortBySpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.sortingOrder,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sortBySpinner.adapter = adapter
        }

        // RecyclerView
        val rAdapter = MainAdapter(this, dailyData)
        statsRecycler.adapter = rAdapter

        val layout = LinearLayoutManager(this)
        statsRecycler.layoutManager = layout



        // LISTENER SET UP
        // countrySpinner
        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                populateStats(countries[parent!!.selectedItem])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        sortBySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(parent!!.selectedItem){
                    "Oldest"-> dailyData.sortBy{it -> it.date}
                    "Newest"-> dailyData.sortByDescending{it->it.date}
                    else -> dailyData.sortByDescending{it->it.percent}
                }
                statsRecycler.adapter!!.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

    }

    private fun populateCountries() {
        val callGetCountries = api.getCountries()

        // use coroutine to make an HTTP request
        callGetCountries.enqueue(object : Callback<List<ResultGetCountries>> {
            override fun onResponse(
                call: Call<List<ResultGetCountries>>,
                response: Response<List<ResultGetCountries>>
            ) {
                Log.d("Result", "Success : ${response.raw()}")
                if (response.isSuccessful){
                    countries = response.body()!!.map{it.country to it.slug}.toMap()

                    countrySpinner.adapter = ArrayAdapter<String>(
                        applicationContext, R.layout.support_simple_spinner_dropdown_item, countries.keys.sorted()
                    )
                }
            }

            override fun onFailure(call : Call<List<ResultGetCountries>>, t : Throwable) {
                Log.d("Result", "Failure: $t")
            }
        })
    }

    private fun populateStats(slug : String?) {
        val callGetDayOne = api.getDayOne(slug!!)

        // use coroutine to make an HTTP request
        callGetDayOne.enqueue(object : Callback<List<ResultGetDayOne>> {
            override fun onResponse(
                call: Call<List<ResultGetDayOne>>,
                response: Response<List<ResultGetDayOne>>
            ) {
                Log.d("Result", "Success : ${response.raw()}")

                dailyData.clear()
                val items = response.body()
                val mostCases = items!!.mapIndexed{index, it -> if (index == 0)  it.cases else (it.cases - items[index - 1].cases) }.maxOrNull()

                var prevCase : Int = 0
                var prevDeaths : Int = 0
                var prevRecovered : Int = 0
                items.forEachIndexed(){
                    index, it ->
                    var total : Int
                    var deaths : Int
                    var recovered : Int

                    if(index == 0){
                        total = it.cases
                        deaths = it.deaths
                        recovered = it.recovered
                    }
                    else {
                        total = it.cases - items[index - 1].cases
                        deaths = it.deaths - items[index - 1].deaths
                        recovered = it.recovered - items[index - 1].recovered
                    }

                    dailyData.add(DailyData(it.date, (total.toDouble() / mostCases!!.toDouble() * 100).toInt(), total, deaths, recovered))
                }
                when(sortBySpinner.selectedItem){
                    "Oldest"-> dailyData.sortBy{it -> it.date}
                    "Newest"-> dailyData.sortByDescending{it->it.date}
                    else -> dailyData.sortByDescending{it->it.cases}
                }
                statsRecycler.adapter!!.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<ResultGetDayOne>>, t: Throwable) {
                Log.d("Result", "Failure: $t")
            }
        })
    }
}