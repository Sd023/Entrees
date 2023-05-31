package com.sdapps.entres

import retrofit2.http.GET

interface APIservice {

    @GET("downloadMaster")
    suspend fun getRes() : List<BO>

    @GET("v1/attendanceAPI")
    suspend fun getResponse() : List<Bo1>
}