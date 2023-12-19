package com.sdapps.entres.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.entres.R


class CommonDialogAdapter(
    private var data: ArrayList<String>,
    private var onLastItemClickListener: OnLastItemClickListener,
    private var view: CommonDialogView.View
) : RecyclerView.Adapter<CommonDialogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.tbl_grid_layout, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == itemCount -1){
            holder.tbl.text = "+"
            holder.itemView.setOnClickListener {
                onLastItemClickListener.onLastItemClick(position)
            }
        }else{
            holder.tbl.text = data[position]
            holder.itemView.setOnClickListener {
                view.switchActivity(position, data[position])
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

    interface OnLastItemClickListener {
        fun onLastItemClick(lastPos: Int)
    }

    fun handleCardClick(position: Int, holder: ViewHolder) {

    }


}