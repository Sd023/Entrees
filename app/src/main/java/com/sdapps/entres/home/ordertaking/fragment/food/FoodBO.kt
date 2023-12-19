package com.sdapps.entres.home.ordertaking.fragment.food

import android.os.Parcel
import android.os.Parcelable

data class FoodBO(val cat: String, val food: String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(cat)
        dest.writeString(food)
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