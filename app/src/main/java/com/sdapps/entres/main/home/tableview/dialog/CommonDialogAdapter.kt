package com.sdapps.entres.main.home.tableview.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.entres.R
import com.sdapps.entres.main.login.data.HotelBO


class CommonDialogAdapter(
    private var data: ArrayList<HotelBO.Seats>?,
//    private var onLastItemClickListener: OnLastItemClickListener,
    private var view: CommonDialogView.View
) : RecyclerView.Adapter<CommonDialogAdapter.ViewHolder>() {

    private lateinit var context : Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.tbl_grid_layout, parent, false)
        context = parent.context
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Dynamically add seats in mobile. Not in use for now
//        if (position == itemCount -1){
//            holder.tbl.text = "+"
//            holder.itemView.setOnClickListener {
//                onLastItemClickListener.onLastItemClick(position)
//            }
//        }else{

        if(data != null){
            if(data!![position].isOrdered){
                holder.itemView.setOnClickListener {
                    view.switchActivity(position, data?.get(position)!!.seatNumber)
                }
                //holder.itemView.setOnClickListener(null)
                holder.tbl.text = data!![position].seatNumber ?: ""
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.avail_table))

            }else{
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primary_light))
                holder.tbl.text = data?.get(position)?.seatNumber ?: ""
                holder.itemView.setOnClickListener {
                    view.switchActivity(position, data?.get(position)!!.seatNumber)
                }
            }
        }



    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tbl: TextView = itemView.findViewById(R.id.tblBatch)
        var cardView: CardView = itemView.findViewById(R.id.cardView)
    }

//    interface OnLastItemClickListener {
//        fun onLastItemClick(lastPos: Int)
//    }
//
//    fun handleCardClick(position: Int, holder: ViewHolder) {
//
//    }


}