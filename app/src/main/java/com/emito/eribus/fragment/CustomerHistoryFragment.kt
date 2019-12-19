package com.emito.eribus.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.emito.eribus.R
import com.emito.eribus.adapter.customerHistoryAdapter
import com.emito.eribus.model.customerHistory
import kotlinx.android.synthetic.main.fragment_customer_history.*
import java.util.*
import kotlin.collections.ArrayList


class CustomerHistoryFragment : Fragment() {
    lateinit var HistView: View
    val c = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //r1 = HistView.findViewById<RecyclerView>(R.id. recycleId)
        //  layoutManager = LinearLayoutManager(userView.context)
        //  r1?.layoutManager = layoutManager
        // Inflate the layout for this fragment
        HistView= inflater.inflate(R.layout.fragment_customer_history, container, false)
      var  r1 = HistView.findViewById<RecyclerView>(R.id. recycleId)
        // Give the recycler view with Linear layout manager.
        r1?.layoutManager =  LinearLayoutManager(HistView.context)

        // Populate Product  data list
        var historydata = ArrayList<customerHistory>()
        val mYear= c.get(Calendar.YEAR)
        val mMonth=c.get(Calendar.MONTH)
        val mDay= c.get(Calendar.DAY_OF_MONTH)
        val mHour = c.get(Calendar.HOUR_OF_DAY)
        val mMinute = c.get(Calendar.MINUTE)
        val mSecond = c.get(Calendar.SECOND)

      val time  ="$mHour:$mMinute:$mSecond  $mDay /$mMonth /$mYear"
        historydata.add(customerHistory("Fairfield","Iowa","UnitedStates",time))
        historydata.add(customerHistory("Makindye","Kampala","Uganda",time))
        historydata.add(customerHistory("Kon","Totyo","Japan",time))
        historydata.add(customerHistory("kapnni","Nairobi","Kenya",time))

        // Create an adapter and supply the data to be displayed.
        var adapter = customerHistoryAdapter(HistView.context,historydata)
        // Connect the adapter with the recycler view.
        r1.adapter = adapter
        return HistView




    }

}
