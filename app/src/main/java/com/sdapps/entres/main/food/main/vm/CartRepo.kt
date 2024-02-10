package com.sdapps.entres.main.food.main.vm

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.sdapps.entres.core.constants.DataMembers
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.main.food.main.FoodBO

class CartRepo(var context: Context) {

    private lateinit var currentUserID: String
    private lateinit var hotel: String
    private lateinit var hotelBranch: String


    fun insertData(
        vm: CartViewModel,
        totalOrderPrice: Double,
        list: ArrayList<FoodBO>,
        tableName: String,
        seats: String
    ) {

        try {
            val db = DBHandler(context)
            //(orderId TEXT, tableId TEXT, seatNumber TEXT, totalItems INT, totalOrderValue Double)
            fetchUserId(db)
            if (currentUserID.isNotEmpty()) {
                val timestamp = System.currentTimeMillis()
                val userID = currentUserID
                var lineValue = 0.0
                val uid = StringBuilder().append(userID).append(timestamp)
                vm.setOrderId(uid.toString())

                val sb = StringBuilder()
                    .append(QS(uid))
                    .append(",")
                    .append(QS(tableName))
                    .append(",")
                    .append(QS(seats))
                    .append(",")
                    .append(QS(list.size))
                    .append(",")
                    .append(totalOrderPrice)  //simple logic

                db.insertSQL(
                    DataMembers.tbl_orderHeader,
                    DataMembers.tbl_orderHeaderCols,
                    sb.toString()
                )

                for (orderDetail in list) {

                    lineValue = (orderDetail.qty * orderDetail.price)

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
                        .append(QS(lineValue))

                    db.insertSQL(
                        DataMembers.tbl_orderDetail,
                        DataMembers.tbl_orderDetailCols, orderDetails.toString()
                    )

                }
                sendOrderDetailsToFirebase(
                    vm,
                    uid.toString(),
                    list,
                    totalOrderPrice,
                    tableName,
                    seats
                )

            } else {
                showToast("User id empty, Unable to create Order")
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }


    }

    fun sendOrderDetailsToFirebase(
        vm: CartViewModel,
        uid: String,
        list: ArrayList<FoodBO>,
        totalOrderPrice: Double,
        tableName: String,
        seats: String
    ) {
        try {
            var isDataInserted = false

            val dataBaseRef = FirebaseDatabase.getInstance()

            val ref =
                dataBaseRef.getReference("hotels")
                    .child(hotel)
                    .child(hotelBranch)

            vm.setHotelName(hotel)
            vm.setHotelBranch(hotelBranch)

            val orderHeaderMap: HashMap<String, Any> = hashMapOf()
            val orderDetailsMaster: HashMap<String, HashMap<String, Any>> = hashMapOf()
            //OrderHeader - (orderId TEXT, tableId TEXT, seatNumber TEXT, totalItems INT, totalOrderValue Double)


            orderHeaderMap["OrderID"] = uid
            orderHeaderMap["Table"] = tableName
            orderHeaderMap["Seat"] = seats
            orderHeaderMap["TotalOrderValue"] = totalOrderPrice
            orderHeaderMap["totalItems"] = list.size

            ref.child("OrderHeader")
                .child(tableName)
                .child(seats)
                .child(uid)
                .setValue(orderHeaderMap)
                .addOnSuccessListener { task ->
                    showToast("Order Created in server")
                }
                .addOnFailureListener {
                    showToast(it.message!!)
                }

            //OrderDetail - (orderId TEXT,foodName TEXT, qty INT,price DOUBLE, tableId TEXT,seatNumber TEXT, totalOrderValue DOUBLE)

            var totalLineValue = 0.0
            for ((index, data) in list.withIndex()) {
                totalLineValue = (data.qty * data.price)
                val orderDetailsMap: HashMap<String, Any> = hashMapOf()
                orderDetailsMap["OrderID"] = uid
                orderDetailsMap["FoodName"] = data.foodName
                orderDetailsMap["qty"] = data.qty
                orderDetailsMap["price"] = data.price
                orderDetailsMap["lineTotal"] = totalLineValue
                orderDetailsMap["totalOrderValue"] = totalOrderPrice
                orderDetailsMaster["$uid : $index"] = orderDetailsMap
            }
            vm.setOrderedHashMap(orderDetailsMaster)

            ref.child("OrderDetails")
                .child(tableName)
                .child(seats)
                .setValue(orderDetailsMaster)
                .addOnSuccessListener {
                    isDataInserted = true
                    changeStatusInTable(isDataInserted, tableName, seats)
                }
                .addOnFailureListener {
                    showToast(it.message!!)
                }


        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun changeStatusInTable(isDataInserted: Boolean, tableName: String, seats: String) {
        try {
            val master = FirebaseDatabase.getInstance().getReference("hotels").child(hotel)
                .child(hotelBranch)
            val ref = master.child("TableMaster")
            val seatRef = master.child("SeatMaster")


            if (tableName.isNotEmpty() && seats.isNotEmpty()) {
                if (isDataInserted) {
                    ref.child(tableName).child("isStatus").setValue("ORD")
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("TASK", "${task.isSuccessful}")
                            }

                        }
                        .addOnFailureListener {
                            showToast(it.message!!)
                        }
                }
                seatRef.child(tableName).child(seats).child("isOrdered").setValue(isDataInserted)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showToast("Order Details Saved!")
                        }
                    }
                    .addOnFailureListener {
                        showToast(it.message!!)
                    }

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun showToast(msg: String) {
        if (msg.isNotEmpty())
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

    }

    fun QS(data: Any): String {
        return "'$data'"
    }

    fun fetchUserId(db: DBHandler) {
        db.createDataBase()
        db.openDataBase()
        val sql = "select userId,hotel,hotelBranch from MasterUser"
        val cursor = db.selectSQL(sql)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                currentUserID = cursor.getString(0)
                hotel = cursor.getString(1)
                hotelBranch = cursor.getString(2)
            }
        }
    }
}