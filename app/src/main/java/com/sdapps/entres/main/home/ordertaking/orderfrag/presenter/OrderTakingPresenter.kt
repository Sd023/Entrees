package com.sdapps.entres.main.home.ordertaking.orderfrag.presenter

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sdapps.entres.core.constants.DataMembers
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.main.home.ordertaking.dialog.CommonDialogView
import com.sdapps.entres.main.login.data.HotelBO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderTakingPresenter(private var context: Context) : OrderTakingManager.Presenter {


    private lateinit var view: OrderTakingManager.View


    public var hotelName: String? = null
    public var hotelBranch: String? = null
    private lateinit var databaseReference: DatabaseReference
    public lateinit var tableList: ArrayList<Int>
    lateinit var tblWithStatus: HashMap<Int, String>
    private lateinit var masterTableMap: HashMap<*, *>
    private lateinit var tblKeyList: ArrayList<String>
    private lateinit var db: DBHandler


    var tblKeyMap: HashMap<String, ArrayList<HotelBO.Seats>>? = null
    var tblSeatDetail: ArrayList<HotelBO.Seats>? = null
    override fun attachView(view: OrderTakingManager.View, db: DBHandler) {
        this.view = view
        this.db = db

    }


    override fun loadUserDetails() {
        try {


            val db = DBHandler(context)
            db.createDataBase()
            db.openDataBase()

            val cursor = db.selectSQL("select hotel,hotelBranch from MasterUser")
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    hotelName = cursor.getString(0)
                    hotelBranch = cursor.getString(1)
                }
                cursor.close()
            }
        } catch (ex: Exception) {

        }

    }

    fun getSeatHashMap(): HashMap<String, ArrayList<HotelBO.Seats>>? {
        return tblKeyMap
    }

    fun setSeatHashMap(map: HashMap<String, ArrayList<HotelBO.Seats>>?) {
        this.tblKeyMap = map
    }

    override fun tableRef() {
        tableList = arrayListOf()
        tblWithStatus = hashMapOf()
        masterTableMap = HashMap<Any, Any>()
        tblKeyList = arrayListOf()

        CoroutineScope(Dispatchers.IO).launch {
            if (hotelName != null && hotelBranch != null) {
                databaseReference = FirebaseDatabase
                    .getInstance()
                    .getReference("hotels")
                    .child(hotelName!!)
                    .child(hotelBranch!!)
                val tblDbRef = databaseReference
                tblDbRef.child("TableMaster").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot != null) {
                            masterTableMap = ((snapshot.value as? HashMap<*, *>)!!)
                            if (tableList.isNotEmpty())
                                tableList.clear()
                            for (mKey in masterTableMap.keys) {
                                tableList.add(extractString(mKey.toString()))
                                tblKeyList.add(mKey.toString())
                                val localMap = masterTableMap[mKey] as? HashMap<String, String>
                                if (localMap != null) {
                                    val isStatus = localMap["isStatus"].toString()
                                    tblWithStatus[extractString(mKey.toString())] = isStatus
                                }


                            }
                            //handleInsertRecords(masterTableMap, false)

                            CoroutineScope(Dispatchers.Main).launch {
                                //getSeatsReference(tblKeyList, databaseReference)
                                view.setupView(list = tableList, map = tblWithStatus)
                            }


                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }

        }
    }


    fun handleInsertRecords(map: HashMap<*, *>, isSeat: Boolean) {
        var isDataExist: Boolean = false
        db.createDataBase()
        db.openDataBase()


        var sql = ""
        if (isSeat) {
            sql = "select * from TableSeatMapping"
        } else {
            sql = "select * from TableMaster"
        }
        val cursor = db.selectSQL(sql)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                isDataExist = true
            }
            cursor.close()
        }

        if (isDataExist) {
            db.dbRawQuery("DELETE FROM TableMaster")
            if (isSeat)
                db.dbRawQuery("DELETE FROM TableSeatMapping")
        }

        if (isSeat) {
            var seatNum = String()
            var tableId = String()

            for (data in map.keys) {
                val tblData = map[data] as? HashMap<String, String>

                if (tblData != null) {
                    seatNum = tblData["seatNum"].toString()
                    tableId = tblData["tableId"].toString()
                }
                insertData(data.toString(), seatNum, tableId, true)
            }

        } else {
            var isStatus = String()
            var tableId = String()
            for (data in map.keys) {
                val tblData = map[data] as? HashMap<String, String>
                if (tblData != null) {
                    isStatus = tblData["isStatus"].toString()
                    tableId = tblData["tableId"].toString()
                    insertData(data.toString(), isStatus, tableId, false)
                }


            }
        }

    }

    fun insertData(tblName: String, seatNumOrStatus: String, tableId: String, isSeat: Boolean) {
        try {

            if (isSeat) {

                val sb = StringBuilder()
                    .append(QT(tableId))
                    .append(",")
                    .append(QT(tblName))
                    .append(",")
                    .append(QT(seatNumOrStatus))

                db.insertSQL(
                    DataMembers.tbl_tableSeatMapping,
                    DataMembers.tbl_tableSeatMappingCols,
                    sb.toString()
                )
            } else {
                val sb = StringBuilder()
                    .append(QT(tableId))
                    .append(",")
                    .append(QT(tblName))
                    .append(",")
                    .append(QT(seatNumOrStatus))

                db.insertSQL(
                    DataMembers.tbl_tableMaster,
                    DataMembers.tbl_tableMasterCols,
                    sb.toString()
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }


    }

    fun getSeatsReference(dialogView: CommonDialogView.View) {

        tblKeyMap = hashMapOf()


        databaseReference.child("SeatMaster").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot != null) {
                        val map = (snapshot.value as? HashMap<*, *>)!!
                        for (test in map.keys) {
                            val seatKeyList = (map[test] as HashMap<String, String>)
                            tblSeatDetail = arrayListOf()
                            for (testp in seatKeyList.keys) {

                                val seatAttributes = seatKeyList[testp] as? HashMap<String, String>

                                if (seatAttributes != null) {

                                    val items = HotelBO.Seats(
                                        seatAttributes["seatNum"].toString(),
                                        seatAttributes["tableId"].toString()
                                    )
                                    tblSeatDetail!!.add(items)
                                    tblKeyMap!![test.toString()] = tblSeatDetail!!

                                }
                            }

                        }
                        //handleInsertRecords(map, true)


                    setSeatHashMap(tblKeyMap)
                    dialogView.setupView(tblKeyMap)
//                    Log.d("TAG", ":->>> $tblKeyMap")

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", error.message)
            }

        })

    }

    fun extractString(msg: String): Int {
        return msg.removePrefix("Table").toIntOrNull() ?: 0
    }

    fun QT(data: String): String {
        return "'$data'"
    }

}