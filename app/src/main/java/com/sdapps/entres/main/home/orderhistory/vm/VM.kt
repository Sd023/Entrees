package com.sdapps.entres.main.home.orderhistory.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdapps.entres.main.home.orderhistory.OrderHistoryBO
import kotlinx.coroutines.launch

class VM(private var app: Application, private var orderRepo: OrderHistoryDataManager) :
    ViewModel() {

    private val _orderList = MutableLiveData<ArrayList<OrderHistoryBO>>()

    val orderList: LiveData<ArrayList<OrderHistoryBO>> get() = _orderList


    private val _orderDetailList = MutableLiveData<ArrayList<OrderHistoryBO>>()
    val orderDetailList: LiveData<ArrayList<OrderHistoryBO>> get() = _orderDetailList

    private val totalNetValue = MutableLiveData<Double>()


    fun setTotalNetValue(value : Double){
        this.totalNetValue.value = value
    }

    fun getTotalNetValue(): Double?{
        return totalNetValue.value
    }

    fun getOrderDetail(orderId: String){
        viewModelScope.launch {
            _orderDetailList.value = orderRepo.getPastOrderDetailsById(orderId, this@VM)
        }
    }

    fun getPastOrdersFromDB() {
        viewModelScope.launch {
            _orderList.value = orderRepo.getPastOrders()
        }
    }


}