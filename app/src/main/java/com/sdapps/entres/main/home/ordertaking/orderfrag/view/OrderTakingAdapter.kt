package com.sdapps.entres.main.home.ordertaking.orderfrag.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.entres.R
import com.sdapps.entres.main.home.ordertaking.orderfrag.presenter.OrderTakingManager

class OrderTakingAdapter(private val data: ArrayList<Int>,private val map: HashMap<Int,String>, private var context : Context,private var view: OrderTakingManager.View): RecyclerView.Adapter<OrderTakingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.table_items, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try{
            val text = "Table : ${data[position]}"

            val statusMap = data.mapNotNull { key -> map[key] }

            if(statusMap[position]!!.contains("DEAD")){
                holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.dead_table))
            }else if(statusMap[position]!!.contains("ORD")){
                holder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.avail_table))
            }else{
                holder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.empty_table))
            }


            holder.layout.setOnClickListener {
                view.showDialog(data[position])
            }

            holder.textView.text = text
        }catch (ex: Exception){
            ex.printStackTrace()
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.tableId)
        var layout: RelativeLayout = itemView.findViewById(R.id.cardView)
    }



}