package com.sdapps.entres.main.home.orderhistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.entres.R
import com.sdapps.entres.main.home.orderhistory.vm.Listener
import com.sdapps.entres.main.home.orderhistory.vm.OrderHistoryDataManager

class OrderHistoryAdapter(private var data: ArrayList<OrderHistoryBO>) :
    RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {

        private var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.history_view, parent, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setItemListener(listener: Listener){
        this.listener = listener
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tableId.text  = data[position].tableName
        holder.totalItems.text = StringBuilder().append("Total Items: ").append(data[position].totalItems).toString()
        holder.totalPrice.text = StringBuilder().append("Total Price: ").append(data[position].totalPrice).toString()

        holder.cardView.setOnClickListener {
           //listener?.loadOrderDetail(data[position].orderID!!)
        }
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

        var cardView :CardView = itemView.findViewById(R.id.cardView)

    }
}