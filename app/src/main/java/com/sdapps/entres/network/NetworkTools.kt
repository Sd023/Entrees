package com.sdapps.entres.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkTools() {

    fun isAvailableConnection(context: Context): Boolean{
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }
}