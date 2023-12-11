package com.sdapps.entres.home.ordertaking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.entres.R
import kotlin.random.Random

class OrderTakingAdapter(private val data: ArrayList<Int>, private var context : Context): RecyclerView.Adapter<OrderTakingAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderTakingAdapter.ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.table_items, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: OrderTakingAdapter.ViewHolder, position: Int) {
        try{
            val text ="Table : ${data[position]}"

            if(data[position] == 2){
                holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.dead_table))
            }else if (data[position] == 5){
                holder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.avail_table))
            }else if (data[position] == 6 || data[position] == 3 ){
                holder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.taken_table))
                holder.textView.setTextColor(ContextCompat.getColor(context,R.color.bg_color))
            }else {
                holder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.empty_table))
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