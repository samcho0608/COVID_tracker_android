package com.example.covid.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.example.covid.R

class DetailedStatFragment : Fragment() {
    private var date: String? = null
    private var cases: Int? = null
    private var deaths: Int? = null
    private var recovered: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = it.getString("date")
            cases = it.getInt("cases")
            deaths = it.getInt("deaths")
            recovered = it.getInt("recovered")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_detailed_stat, container, false)
        val fragDate : TextView = view.findViewById(R.id.fragDate)
        val fragTotal : TextView = view.findViewById(R.id.fragTotal)
        val fragDeaths: TextView = view.findViewById(R.id.fragDeaths)
        val fragRecovered : TextView = view.findViewById(R.id.fragRecovered)
        fragDate.text = date
        fragDeaths.text = deaths.toString()
        fragTotal.text = cases.toString()
        fragRecovered.text = recovered.toString()

        var okBtn : Button = view.findViewById(R.id.ok)
        okBtn.setOnClickListener {
            var fragmentManager : FragmentManager = activity!!.supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
//            fragmentManager.popBackStack()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(date: String, cases: Int, deaths: Int, recovered: Int) =
            DetailedStatFragment().apply {
                arguments = Bundle().apply {
                    putString("date", date)
                    putInt("cases", cases)
                    putInt("deaths", deaths)
                    putInt("recovered", recovered)
                }
            }
    }
}