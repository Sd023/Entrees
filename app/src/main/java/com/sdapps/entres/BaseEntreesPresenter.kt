package com.sdapps.entres

import android.content.Context
import com.sdapps.entres.core.database.DBHandler
import com.sdapps.entres.main.login.data.LoginBO

class BaseEntreesPresenter(var context: Context) : BaseEntreesManager.BaseEntreesPresenter{

    public lateinit var userList : ArrayList<LoginBO>

    override fun downloadUserDetails() {
        //MasterUser(uid TEXT,email TEXT,userId INT PRIMARY KEY UNIQUE,role TEXT,hotel TEXT,hotelBranch TEXT,createdDate TEXT)
        userList = arrayListOf()
        val db = DBHandler(context)
        db.createDataBase()
        db.openDataBase()

        val sql = "select uid,email,userId,role,hotel,hotelBranch,createdDate from MasterUser"
        val cursor = db.selectSQL(sql)

        if(cursor != null){
            while(cursor.moveToNext()){
                val bo = LoginBO().apply {
                    currentUserUid = cursor.getString(0)
                    email = cursor.getString(1)
                    userId = cursor.getInt(2)
                    role = cursor.getString(3)
                    hotel = cursor.getString(4)
                    hotelBranch = cursor.getString(5)
                    createdDate = cursor.getString(6)
                }

                userList.add(bo)
            }
        }
    }
}