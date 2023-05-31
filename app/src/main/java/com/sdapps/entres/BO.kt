package com.sdapps.entres

import com.google.gson.annotations.SerializedName

data class BO (@SerializedName("code") val code : String,
               @SerializedName("api") val api : String
)

data class Bo1(@SerializedName("message") val msg : String)
