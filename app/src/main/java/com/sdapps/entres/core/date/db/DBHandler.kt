package com.sdapps.entres.core.date.db

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DBHandler(val context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {


    private var sqlite: SQLiteDatabase? = null

    companion object{
        const val DB_NAME = "entrees.sqlite"
        const val DB_VERSION = 1
    }
    override fun onCreate(db: SQLiteDatabase?) {
        this.sqlite = db
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        dbUpdate(newVersion)
    }



    fun selectSQL(sql: String): Cursor{
        Log.d("QUERY", ": $sql")
        return sqlite!!.rawQuery(sql, null)
    }

    fun updateSQL(sql: String){
        Log.d("QUERY", ": $sql")
         sqlite!!.rawQuery(sql, null)
    }

    fun insertSQL(tblName: String, columns: String, content: String){
        val sql : String = "insert into $tblName ($columns) values($content)"
        Log.d("QUERY", ": $sql")
        sqlite!!.execSQL(sql)

    }
    fun dbUpdate(newVersion: Int){
        dbExport(newVersion)
        val delete: Boolean = context.deleteDatabase(DB_NAME)
        if (delete)
            createDataBase()
    }


    fun dbRawQuery(sql: String){
        sqlite!!.execSQL(sql)
    }

    fun createDataBase(){
        val dbExist: Boolean = checkDataBase()
        writableDatabase
        if (!dbExist) {
            try {
                copyDataBase()
                DBHelper.createTables(this)
            } catch (e: IOException) {
                e.printStackTrace()
                throw Error("Error copying database")
            }
        }
    }

    fun checkDataBase(): Boolean{
        return context.getDatabasePath(DB_NAME).exists()
    }

    fun copyDataBase(){

        val myInput: InputStream = context.getAssets().open(DB_NAME)
        val myOutput: OutputStream = FileOutputStream(context.getDatabasePath(DB_NAME).path)
        val buffer = ByteArray(1024)
        var length: Int
        while (myInput.read(buffer).also { length = it } > 0) {
            myOutput.write(buffer, 0, length)
        }
        myOutput.flush()
        myOutput.close()
        myInput.close()
    }


    @Throws(SQLException::class)
    fun openDataBase() {
        try {
            sqlite = SQLiteDatabase.openDatabase(context.getDatabasePath(DB_NAME).path, null, SQLiteDatabase.OPEN_READWRITE)
        } catch (e: SQLiteCantOpenDatabaseException) {
            Log.e("Cant open DB", e.message!!)
        }
    }
    fun dbExport(newVersion: Int){

        val dbPATH = context.getDatabasePath(DB_NAME).path

        val fileLocation: String =
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/sdapps/"

        val folder: File = File(fileLocation)
        if (!folder.exists()) {
            folder.mkdir()
        }

        val SDPath = File(fileLocation)
        if (!SDPath.exists()) {
            SDPath.mkdir()
        }
        try {
            val presentDB: File = File(dbPATH)
            val input: InputStream = FileInputStream(presentDB)
            val data = ByteArray(input.available())
            input.read(data)
            val out: OutputStream = FileOutputStream(
                fileLocation + "/"
                        + "sdapps_old" + newVersion
            )
            out.write(data)
            out.flush()
            out.close()
            input.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


}