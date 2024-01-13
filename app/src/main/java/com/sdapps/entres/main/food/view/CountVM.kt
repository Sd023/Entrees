package com.sdapps.entres.main.food.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountVM: ViewModel() {

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
}