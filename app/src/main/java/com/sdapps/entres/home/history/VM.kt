package com.sdapps.entres.home.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sdapps.entres.home.ordertaking.fragment.food.FoodBO

class VM: ViewModel() {
    private val _data = MutableLiveData<String>()

    val data : LiveData<String>
        get() = _data


    fun setData(str: String){
        _data.value = str
    }
}