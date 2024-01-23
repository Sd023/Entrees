package com.sdapps.entres.main.food.main

import android.os.Parcel
import android.os.Parcelable

data class FoodBO(val category: String,
                  val foodName: String,
                  val price:Int,
                  val imgUrl : String,
                  var qty: Int = 1, var seatName: String, var tableName: String) : Parcelable {



    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(category)
        dest.writeString(foodName)
        dest.writeInt(price)
        dest.writeString(imgUrl)
        dest.writeInt(qty)
        dest.writeString(seatName)
        dest.writeString(tableName)
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