package com.sdapps.entres.main.home.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.entres.R
import com.sdapps.entres.main.home.tableview.dialog.CommonDialogAdapter

class OrderHistoryAdapter(private var data: ArrayList<OrderHistoryBO>) :
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.history_view, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tableId.text  = StringBuilder().append("Table: ").append(data[position].tableName).toString()
        holder.totalItems.text = StringBuilder().append("Total Items: ").append(data[position].totalItems).toString()
        holder.totalPrice.text = StringBuilder().append("Total Price: ").append(data[position].totalPrice).toString()
        holder.status.text = StringBuilder().append("Status: ").append(data[position].status).toString()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tableId: TextView = itemView.findViewById(R.id.tableId)
        var totalPrice: TextView = itemView.findViewById(R.id.totalPrice)
        var totalItems: TextView = itemView.findViewById(R.id.totalItems)
        var status: TextView = itemView.findViewById(R.id.status)

    }
}