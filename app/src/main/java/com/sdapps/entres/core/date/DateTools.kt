package com.sdapps.entres.core.date

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateTools {

    companion object{
        var DATE = 0
        var TIME = 1
        var DATE_TIME =3
    }

    fun now(format : Int): String{
        val cal = Calendar.getInstance()
        return if(DATE == format){
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
            sdf.format(cal.time)
        }else if (TIME == format){
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            sdf.format(cal.time)
        }else if (DATE_TIME == format){
            val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH)
            sdf.format(cal.time)
        }else{
            val sdf = SimpleDateFormat("MMddyyyyHHmmss", Locale.ENGLISH)
            sdf.format(cal.time)
        }

    }
}