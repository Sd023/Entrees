package com.sdapps.entres.main.home.orderhistory.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OrderHistoryFactory(private val app: Application,
                          private val repo: OrderHistoryDataManager)
    : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(VM::class.java)){
            return VM(app,repo) as T
        }

        throw IllegalAccessException("Unknown VM")
    }
}