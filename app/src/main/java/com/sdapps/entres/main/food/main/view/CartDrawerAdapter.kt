package com.sdapps.entres.main.food.main.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sdapps.entres.R
import com.sdapps.entres.main.food.main.FoodBO
import com.sdapps.entres.main.food.main.vm.CartViewModel

class CartDrawerAdapter(var vm: CartViewModel,var parent : CartDrawerFragment,var context: Context, var data: ArrayList<FoodBO>) :
    RecyclerView.Adapter<CartDrawerAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data != null) {
            holder.count.text = data[position].qty.toString()
            holder.foodName.text = data[position].foodName
            holder.foodPrice.text = data[position].price.toString()

            if(data[position].qty <= 1){
                holder.removeItem.setImageResource(R.drawable.cross)
            }
        }

        holder.addItem.setOnClickListener {
            val count = data[position].qty
            val addQty = count + 1
            data[position].qty = addQty
            notifyItemChanged(position)
            vm.calculateOrderValue(data)

        }

        holder.removeItem.setOnClickListener {
            val count = data[position].qty
            val removeQty = count - 1

            if (removeQty <= 0) {
                Toast.makeText(context, "Items cannot be 0", Toast.LENGTH_LONG).show()
            } else {
                data[position].qty = removeQty
                notifyItemChanged(position)
                vm.calculateOrderValue(data)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.drawer_items, parent, false)
        return ViewHolder(layout)
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foodName: TextView= itemView.findViewById(R.id.foodName)
        var foodPrice: TextView= itemView.findViewById(R.id.foodPrice)
        var addItem: ImageView= itemView.findViewById(R.id.addItem)
        var removeItem: ImageView= itemView.findViewById(R.id.removeItem)
        var count: TextView= itemView.findViewById(R.id.cartCount)

        var cardViewCart : RelativeLayout = itemView.findViewById(R.id.cardViewCart)
    }


    /*    fun insertData(list: ArrayList<FoodBO>){

            try{
                val db = DBHandler(context)
                //(orderId TEXT, tableId TEXT, seatNumber TEXT, totalItems INT, totalOrderValue Double)


                val timestamp = System.currentTimeMillis()
                val userID = fetchUserId(db)

                val uid = StringBuilder().append(userID).append(timestamp)

                val sb =  StringBuilder()
                    .append(QS(uid))
                    .append(",")
                    .append(QS(tableName))
                    .append(",")
                    .append(QS(seat))
                    .append(",")
                    .append(QS(data.size))
                    .append(",")
                    .append(QS(data.size))  //simple logic

                db.insertSQL(DataMembers.tbl_orderHeader, DataMembers.tbl_orderHeaderCols,sb.toString())

                for(orderDetail in list){

                    //(orderId TEXT,foodName TEXT, qty INT,price DOUBLE, tableId TEXT,seatNumber TEXT, totalOrderValue DOUBLE)
                    val orderDetails = StringBuilder()
                        .append(QS(uid))
                        .append(",")
                        .append(QS(orderDetail.foodName))
                        .append(",")
                        .append(QS(orderDetail.qty))
                        .append(",")
                        .append(QS(orderDetail.price))
                        .append(",")
                        .append(QS(tableName))
                        .append(",")
                        .append(QS(seat))
                        .append(",")
                        .append(QS(orderDetail.price))

                    db.insertSQL(
                        DataMembers.tbl_orderDetail,
                        DataMembers.tbl_orderDetailCols,orderDetails.toString())
                }
                data.clear()
                notifyDataSetChanged()
            }catch (ex: Exception){
                ex.printStackTrace()
            }

        }*/

    /*  fun QS(data: Any):String{
          return "'$data'"
      }
      fun fetchUserId(db: DBHandler): Int{
          db.createDataBase()
          db.openDataBase()
          val sql = "select userId from MasterUser"
          val cursor = db.selectSQL(sql)
          if(cursor != null){
              while (cursor.moveToNext()){
                  return cursor.getInt(0)
              }
          }
          return 0
      }
  */


}