package com.sdapps.entres.main.food.main

import android.os.Parcel
import android.os.Parcelable

data class FoodBO(val category: String,
                  val foodName: String,
                  var price:Double,
                  val imgUrl : String,
                  var qty: Int = 1,
                  var seatName: String,
                  var tableName: String,
    var totalOrderValue : Double

) : Parcelable {



    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble()?: 0.0,
        parcel.readString() ?: "",
        parcel.readInt()?: 0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble() ?: 0.0
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(category)
        dest.writeString(foodName)
        dest.writeDouble(price)
        dest.writeString(imgUrl)
        dest.writeInt(qty)
        dest.writeString(seatName)
        dest.writeString(tableName)
        dest.writeDouble(totalOrderValue)
    }

    companion object CREATOR : Parcelable.Creator<FoodBO> {
        override fun createFromParcel(parcel: Parcel): FoodBO {
            return FoodBO(parcel)
        }

        override fun newArray(size: Int): Array<FoodBO?> {
            return arrayOfNulls(size)
        }
    }

}