package com.sdapps.entres.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.entres.R
import com.sdapps.entres.home.ordertaking.fragment.food.FoodBO

class FoodAdapter(private var data: List<FoodBO>): RecyclerView.Adapter<FoodAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.food_item_list, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.food.text = data[position].foodName
        holder.amount.text = data[position].price.toString()

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        var amount: TextView = itemView.findViewById(R.id.amount)
        var food: TextView = itemView.findViewById(R.id.foodName)
    }

    fun updateData(newData: List<FoodBO>) {
        data = newData
        notifyDataSetChanged()
    }
}