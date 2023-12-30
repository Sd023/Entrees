package com.sdapps.entres.home.ordertaking.ui.presenter

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sdapps.entres.core.date.db.DBHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderTakingPresenter(private var context: Context) : OrderTakingManager.Presenter{


    private lateinit var view: OrderTakingManager.View


    public var hotelName : String? = null
    public var hotelBranch: String? = null
    private lateinit var databaseReference : DatabaseReference
    public lateinit var tableList: ArrayList<Int>
    lateinit var tblWithStatus: HashMap<Int,String>
    private lateinit var masterTableMap : HashMap<*,*>
    override fun attachView(view: OrderTakingManager.View) {
        this.view = view
    }


    override fun loadUserDetails() {
        try{


            val db = DBHandler(context)
            db.openDataBase()

            val cursor  = db.selectSQL("select hotel,hotelBranch from MasterUser")
            if(cursor != null){
                while(cursor.moveToNext()){
                    hotelName = cursor.getString(0)
                    hotelBranch = cursor.getString(1)
                }
            }
        }catch (ex: Exception){

        }

    }

    override fun tableRef() {
        tableList = arrayListOf()
        tblWithStatus = hashMapOf()
        CoroutineScope(Dispatchers.Main).launch {
            if(hotelName != null && hotelBranch != null){
                databaseReference = FirebaseDatabase
                    .getInstance()
                    .getReference("hotels")
                    .child(hotelName!!)
                    .child(hotelBranch!!)
                databaseReference.child("TableMaster").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot != null){
                            masterTableMap = ((snapshot.value as? HashMap<*,*>)!!)
                            if(tableList.isNotEmpty())
                                tableList.clear()
                            for(mKey in masterTableMap.keys) {
                                tableList.add(extractString(mKey.toString()))
                                val localMap = masterTableMap[mKey] as? HashMap<String,String>
                                if(localMap != null){
                                    val isStatus = localMap["isStatus"].toString()
                                    tblWithStatus[extractString(mKey.toString())] = isStatus
                                }



                            }
                            view.setupView(list = tableList, map = tblWithStatus)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }
    }

    fun extractString(msg: String): Int{
        return msg.removePrefix("Table").toIntOrNull() ?: 0
    }

}