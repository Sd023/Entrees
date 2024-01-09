package com.sdapps.entres.main.food

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.entres.R
import com.sdapps.entres.main.food.view.FoodBO
import com.sdapps.entres.main.food.view.presenter.FoodActivityManager

class FoodAdapter(private var context: Context,private var data: List<FoodBO>, var view: FoodActivityManager.View): RecyclerView.Adapter<FoodAdapter.ViewHolder>()  {


    var cartListener: CardClickListener? = null
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

        var count = 0
        holder.cardView.setOnClickListener {
            count += 1
            cardIsClicked(count)
        }

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        try{
            if(context is CardClickListener){
                cartListener = context as CardClickListener
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }


    }

    fun cardIsClicked(count: Int){
        cartListener!!.onCardClick(count)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        var cardView : RelativeLayout = itemView.findViewById(R.id.cardView)
        var amount: TextView = itemView.findViewById(R.id.amount)
        var food: TextView = itemView.findViewById(R.id.foodName)
    }

    fun updateData(newData: List<FoodBO>) {
        data = newData
        notifyDataSetChanged()
    }
}