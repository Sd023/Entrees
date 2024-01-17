package com.sdapps.entres.main.home.orders.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdapps.entres.main.home.orders.OrderHistoryBO
import kotlinx.coroutines.launch

class VM(private var app: Application, private var orderRepo: OrderHistoryDataManager) :
    ViewModel() {

    private val _orderList = MutableLiveData<ArrayList<OrderHistoryBO>>()

    val orderList: LiveData<ArrayList<OrderHistoryBO>> get() = _orderList

    fun getPastOrdersFromDB() {
        viewModelScope.launch {
            _orderList.value = orderRepo.getPastOrders()
        }
    }


}