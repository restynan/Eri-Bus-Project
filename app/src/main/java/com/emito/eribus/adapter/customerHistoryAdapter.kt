package com.emito.eribus.adapter

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.emito.eribus.R
import com.emito.eribus.model.customerHistory
import kotlinx.android.synthetic.main.historycustomerlocation.*
import kotlinx.android.synthetic.main.historycustomerlocation.view.*

class customerHistoryAdapter (var context: Context, val custHistoryList: ArrayList<customerHistory>): RecyclerView.Adapter<customerHistoryAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): customerHistoryAdapter .MyViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.historycustomerlocation, parent, false)
        return MyViewHolder(v);
    }
    override fun getItemCount(): Int {
        return  custHistoryList.size
    }

    override fun onBindViewHolder(holder: customerHistoryAdapter.MyViewHolder, position: Int) {
        holder.plocation?.text = "${custHistoryList[position].city},${custHistoryList[position].state}, ${custHistoryList[position].country}"
        holder.ptime.text=custHistoryList[position].time



       /* holder.parent.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            var res = productlist[position].title
            Toast.makeText(context," $res clicked", Toast.LENGTH_LONG).show()
            intent.putExtra("image", productlist[position].imageName)
            intent.putExtra("title", productlist[position].title)
            intent.putExtra("color",productlist[position].color)
            intent.putExtra("id",productlist[position].itemid)
            intent.putExtra("detail",productlist[position].descProduct)
            context.startActivity(intent)
        }*/
    }

    class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val plocation=itemView.lasttLocation
        val ptime=itemView.time
        val parent =itemView.itemView



    }



}
