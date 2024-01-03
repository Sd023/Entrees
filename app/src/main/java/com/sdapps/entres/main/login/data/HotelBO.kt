package com.sdapps.entres.main.login.data

import com.google.gson.annotations.SerializedName

class HotelBO {

    var hotel: String? = null
    var hotelBranch: String? = null


    var foodMasterName : String? = null

    @SerializedName("id")
    var id : String? = null
    @SerializedName("imgUrl") var imgUrl : String? = null
    @SerializedName("price") var price : String? = null


    var itemList: MutableList<Items>? = null

    data class Items(var name: String,var category: String,var id: String, var imgUrl : String, var price: String)

    data class Seats(var seatNumber : String, var tblId : String)
}