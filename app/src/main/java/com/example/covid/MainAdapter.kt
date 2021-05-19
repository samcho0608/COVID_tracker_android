package com.example.covid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}