package com.sdapps.entres.main.food.main.vm

import android.content.Context
import android.widget.Toast
import com.sdapps.entres.core.constants.DataMembers
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.main.food.main.FoodBO

class CartRepo(var context: Context) {

    fun insertData (list: ArrayList<FoodBO>, tableName : String, seats: String){

        try{
            Toast.makeText(context,"LETS GO ", Toast.LENGTH_LONG).show()
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
                .append(QS(seats))
                .append(",")
                .append(QS(list.size))
                .append(",")
                .append(QS(list.size))  //simple logic

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
                    .append(QS(seats))
                    .append(",")
                    .append(QS(orderDetail.price))

                db.insertSQL(
                    DataMembers.tbl_orderDetail,
                    DataMembers.tbl_orderDetailCols,orderDetails.toString())
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }


    }

    fun QS(data: Any):String{
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
}