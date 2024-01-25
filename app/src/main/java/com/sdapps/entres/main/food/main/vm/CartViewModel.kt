package com.sdapps.entres.main.food.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdapps.entres.main.food.main.FoodBO
import kotlinx.coroutines.launch

class CartViewModel(var repo: CartRepo): ViewModel() {

    private val _counter = MutableLiveData<Int>().apply { value = 0 }
    val count : LiveData<Int> = _counter

    private val foodInCartList = MutableLiveData<ArrayList<FoodBO>>()
    val cartList : LiveData<ArrayList<FoodBO>> = foodInCartList

    private val _orderValue = MutableLiveData<Double>().apply { value = 0.0 }
    val totalOrderValue : LiveData<Double> =  _orderValue

    fun increaseCount(){
        _counter.value = (_counter.value ?: 0) + 1
    }



    fun calculateOrderValue(list: ArrayList<FoodBO>){
        var sum = 0.0
        for(value in list){
            sum += (value.qty * value.price)
        }
        _orderValue.value = sum
    }

    fun getOrderValue(): Double?{
        return totalOrderValue.value
    }
    fun setFoodDetailList(bo: ArrayList<FoodBO>){
        foodInCartList.value = bo
    }

    fun resetCount(){
        _counter.value = 0
    }
    fun insertDataToDB(totalOrderPrice: Double,list: ArrayList<FoodBO>, tableName: String, seat: String){
        viewModelScope.launch {
            repo.insertData(totalOrderPrice,list, tableName, seat)
        }

    }
}