package com.sdapps.entres.core.date.db

import android.util.Log

object DBHelper {

    fun createTables(db: DBHandler){
        try{
            db.openDataBase()
            db.dbRawQuery("create table if not exists MasterUser(uid TEXT,email TEXT,createdDate TEXT)")
        }catch (ex: Exception){
            Log.d("Err!","wtf?")
            ex.printStackTrace()
        }
    }
}