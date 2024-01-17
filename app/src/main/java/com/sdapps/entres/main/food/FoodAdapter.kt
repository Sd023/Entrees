package com.sdapps.entres.main.food

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.sdapps.entres.R
import com.sdapps.entres.main.food.main.FoodBO

class FoodAdapter(private var data: List<FoodBO>): RecyclerView.Adapter<FoodAdapter.ViewHolder>()  {

    private lateinit var appContext: Context

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun itemClickListener(listener: (Int) -> Unit){
        onItemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.food_item_list, parent, false)
        appContext = parent.context
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.food.text = data[position].foodName
        holder.amount.text = data[position].price.toString()


        val img = data[position].imgUrl.replace("\"","").toString()
        if(img.isNotEmpty()){
            val imgRef = Firebase.storage.getReferenceFromUrl(img)

            imgRef.downloadUrl.addOnCompleteListener(OnCompleteListener { task : Task<Uri>  ->

                if(task.isSuccessful){
                    val imgLink = task.result.toString()

                    Glide.with(appContext)
                        .load(imgLink)
                        .apply(RequestOptions().placeholder(R.drawable.ic_launcher_background))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(holder.foodImg)

                }
            })
        }else{
            Glide.with(appContext)
                .load(R.drawable.placeholder)
                .into(holder.foodImg)
        }

        holder.foodCardView.setOnClickListener{
            onItemClickListener?.invoke(position)
        }
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
        var foodImg : ImageView = itemView.findViewById(R.id.foodImg)
        var foodCardView : RelativeLayout = itemView.findViewById(R.id.foodCardView)
    }

    fun updateData(newData: List<FoodBO>) {
        data = newData
        notifyDataSetChanged()
    }
}