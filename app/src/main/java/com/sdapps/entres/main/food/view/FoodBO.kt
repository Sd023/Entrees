package com.sdapps.entres.main.food.view

import android.os.Parcel
import android.os.Parcelable

data class FoodBO(val category: String, val foodName: String, val price:Int, val imgUrl : String, var count: Int = 1) : Parcelable {



    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(category)
        dest.writeString(foodName)
        dest.writeInt(price)
        dest.writeString(imgUrl)
        dest.writeInt(count)
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