package com.sdapps.entres

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Base {

     private val BASE_URL = "http://10.0.2.2:3400/api/"

    val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(APIservice::class.java)


}