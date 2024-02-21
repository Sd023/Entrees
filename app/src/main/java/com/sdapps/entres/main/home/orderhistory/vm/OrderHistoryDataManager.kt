package com.sdapps.entres.main.home.orderhistory.vm

import android.content.Context
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.main.home.orderhistory.OrderHistoryBO

class OrderHistoryDataManager(var context: Context) {

    private lateinit var orderList : ArrayList<OrderHistoryBO>
    private lateinit var orderDetailsList : ArrayList<OrderHistoryBO>
    var totalNetValue : Double? = 0.0

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

    fun getPastOrderDetailsById(orderId: String, vm: VM): ArrayList<OrderHistoryBO>{

        try {
            orderDetailsList = arrayListOf()
            val db = DBHandler(context)
            db.openDataBase()
            val cursor = db.selectSQL("SELECT OrderDetail.foodName,OrderDetail.qty,OrderDetail.price,OrderDetail.seatNumber,OrderDetail.tableId,OrderDetail.totalOrderValue AS netValue,OrderHeader.totalOrderValue FROM OrderDetail LEFT JOIN OrderHeader ON OrderHeader.orderId = OrderDetail.orderId where OrderHeader.orderId = $orderId")
            if(cursor != null){
                while (cursor.moveToNext()){
                    val orderbo = OrderHistoryBO().apply {
                        foodName = cursor.getString(0)
                        qty = cursor.getInt(1)
                        price = cursor.getDouble(2)
                        seatNumber = cursor.getString(3)
                        tableName = cursor.getString(4)
                        netValue = cursor.getDouble(5)

                        totalNetValue = cursor.getDouble(6)
                        vm.setTotalNetValue(totalNetValue!!)
                    }
                    orderDetailsList.add(orderbo)
                }
                cursor.close()
                db.close()
            }
        }catch (ex: Exception){
            ex.printStackTrace()
            return ArrayList()
        }
        return orderDetailsList
    }
}