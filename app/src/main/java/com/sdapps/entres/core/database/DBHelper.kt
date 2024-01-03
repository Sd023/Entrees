package com.sdapps.entres.core.database

import android.util.Log

object DBHelper {

    fun createTables(db: DBHandler){
        try{
            db.openDataBase()
            db.dbRawQuery("create table if not exists MasterUser(uid TEXT,email TEXT,userId INT PRIMARY KEY UNIQUE,role TEXT,hotel TEXT,hotelBranch TEXT,createdDate TEXT)")
            db.dbRawQuery("create table if not exists FoodDataMaster(id INT PRIMARY KEY UNIQUE,foodName TEXT,category TEXT,price INT,imgUrl TEXT)")
            db.dbRawQuery("create table if not exists TableMaster (tableId TEXT PRIMARY KEY UNIQUE, tableName TEXT,isStatus TEXT)")
            db.dbRawQuery("create table if not exists TableSeatMapping (tableId TEXT PRIMARY KEY UNIQUE,tableName TEXT,seatNum TEXT)")
        }catch (ex: Exception){
            Log.d("Err!","wtf?")
            ex.printStackTrace()
        }
    }
}