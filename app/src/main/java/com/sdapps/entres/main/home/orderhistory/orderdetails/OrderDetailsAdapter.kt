package com.sdapps.entres.main.home.orderhistory.orderdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.entres.R
import com.sdapps.entres.main.home.orderhistory.OrderHistoryBO
import java.lang.StringBuilder

class OrderDetailsAdapter(var data : ArrayList<OrderHistoryBO>): RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        var foodName : TextView = itemView.findViewById(R.id.foodName)
        var qty : TextView = itemView.findViewById(R.id.qty)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.order_details_view, parent, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
      return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       try {
           holder.foodName.text = data[position].foodName
           holder.qty.text = StringBuilder().append("x").append(data[position].qty).toString()

       }catch (ex: Exception){
           ex.printStackTrace()
       }
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}