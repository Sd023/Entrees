package com.sdapps.entres.main.home.tableview.tableFrag.presenter

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sdapps.entres.core.constants.DataMembers
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.main.home.tableview.dialog.CommonDialogView
import com.sdapps.entres.main.login.data.HotelBO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TableViewPresenter(private var context: Context) : TableViewManager.Presenter {


    private lateinit var view: TableViewManager.View


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
    override fun attachView(view: TableViewManager.View, db: DBHandler) {
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
            ex.printStackTrace()
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
        if (hotelName != null && hotelBranch != null) {
            databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("hotels")
                .child(hotelName!!)
                .child(hotelBranch!!)
            val tblDbRef = databaseReference
            tblDbRef.child("TableMaster").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
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
                        CoroutineScope(Dispatchers.Main).launch {
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
    fun getSeatsReference(dialogView: CommonDialogView.View) {

        tblKeyMap = hashMapOf()


        databaseReference.child("SeatMaster").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val map = (snapshot.value as? HashMap<*, *>)!!
                    for (test in map.keys) {
                        val seatKeyList = (map[test] as HashMap<String, String>)
                        tblSeatDetail = arrayListOf()
                        for (testp in seatKeyList.keys) {

                            val seatAttributes = seatKeyList[testp] as? HashMap<String, String>

                            if (seatAttributes != null) {

                                val items = HotelBO.Seats(
                                    seatAttributes["seatNum"].toString(),
                                    seatAttributes["tableId"].toString(),
                                    seatAttributes["isOrdered"].toString().toBoolean()
                                )
                                tblSeatDetail!!.add(items)
                                tblKeyMap!![test.toString()] = tblSeatDetail!!

                            }
                        }

                    }

                    setSeatHashMap(tblKeyMap)
                    dialogView.setupView(tblKeyMap)

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