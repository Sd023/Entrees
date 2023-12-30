package com.sdapps.entres.home.ordertaking.frag

import android.os.Parcel
import android.os.Parcelable

data class FoodBO(val category: String, val foodName: String, val price:Int) : Parcelable {



    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
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