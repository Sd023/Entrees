package com.sdapps.entres.main.food.cartdialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.entres.R
import com.sdapps.entres.main.food.main.FoodBO

class CartAdapter(private val data : ArrayList<FoodBO>): RecyclerView.Adapter<CartAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.drawer_items,parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
      return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.count.text = data[position].qty.toString()
            holder.foodName.text = data[position].foodName
            holder.foodPrice.text = data[position].price.toString()


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


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

         var foodName : TextView
         var foodPrice : TextView

         var count : TextView
        init {
            foodName = view.findViewById(R.id.foodName)
            foodPrice = view.findViewById(R.id.foodPrice)
            count = view.findViewById(R.id.cartCount)
        }

    }
}