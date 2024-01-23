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


    fun increaseCount(){
        _counter.value = (_counter.value ?: 0) + 1
    }

    fun setFoodDetailList(bo: ArrayList<FoodBO>){
        foodInCartList.value = bo
    }

    fun resetCount(){
        _counter.value = 0
    }

    fun insertDataToDB(list: ArrayList<FoodBO>, tableName: String, seat: String){
        viewModelScope.launch {
            repo.insertData(list, tableName, seat)
        }

    }
}