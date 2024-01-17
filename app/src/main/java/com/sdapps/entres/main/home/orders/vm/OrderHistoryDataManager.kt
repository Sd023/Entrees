package com.sdapps.entres.main.home.orders.vm

import android.content.Context
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.main.home.orders.OrderHistoryBO

class OrderHistoryDataManager(var context: Context) {

    private lateinit var orderList : ArrayList<OrderHistoryBO>

    fun getPastOrders(): ArrayList<OrderHistoryBO>{

        val db = DBHandler(context)
        db.createDataBase()
        db.openDataBase()

        orderList = arrayListOf()
        val sql = "select orderId,tableId,seatNumber,totalItems,totalOrderValue from OrderHeader"
        val cursor = db.selectSQL(sql)

        if(cursor != null){
            while(cursor.moveToNext()){
                val orderBO = OrderHistoryBO().apply {
                    orderID = cursor.getString(0)
                    tableName = cursor.getString(1)
                    seatNumber = cursor.getString(2)
                    totalItems = cursor.getInt(3).toString()
                    totalPrice = cursor.getDouble(4).toString()
                }
                orderList.add(orderBO)
            }
            cursor.close()
        }

        return orderList
    }
}