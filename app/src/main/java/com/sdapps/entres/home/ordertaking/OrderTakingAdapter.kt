package com.sdapps.entres.home.ordertaking

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.entres.R
import com.sdapps.entres.view.CommonDialog
import kotlin.random.Random

class OrderTakingAdapter(private val data: ArrayList<Int>, private var context : Context,private var view: OrderTaking.View): RecyclerView.Adapter<OrderTakingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.table_items, parent, false)
        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try{
            val text ="Table : ${data[position]}"

            if(data[position] == 2){
                holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.dead_table))
            }else if (data[position] == 5){
                holder.layout.setBackgroundColor(ContextCompat.getColor(context,R.color.avail_table))
            }else {
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