package com.example.covid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.covid.fragments.DetailedStatFragment
import java.text.DateFormat
import java.text.DateFormat.MEDIUM

class MainAdapter(private val context: Context, private val dataList: ArrayList<DailyData>) :
    RecyclerView.Adapter<MainAdapter.ItemViewHolder>(){

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val dateText = itemView.findViewById<TextView>(R.id.date)
            private val proportionBar = itemView.findViewById<ProgressBar>(R.id.proportion)

            fun bind(dailyData: DailyData,context : Context) {
                dateText.text = DateFormat.getDateInstance(MEDIUM).format(dailyData.date)
                proportionBar.progress = dailyData.percent
            }


        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false )
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataList[position], context)

        holder.itemView.setOnClickListener {
            val activity = it!!.context as AppCompatActivity
            val detailedStatFragment = DetailedStatFragment()
            val bundle = Bundle()
            bundle.putInt("deaths", dataList[position].deaths)
            bundle.putInt("recovered", dataList[position].recovered)
            bundle.putInt("cases", dataList[position].cases)
            bundle.putString("date", DateFormat.getDateInstance(MEDIUM).format(dataList[position].date))
            detailedStatFragment.arguments = bundle
            activity.supportFragmentManager.beginTransaction().replace(R.id.main, detailedStatFragment).addToBackStack(null).commit()

        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}