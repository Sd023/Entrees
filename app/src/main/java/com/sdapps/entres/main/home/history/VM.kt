package com.sdapps.entres.main.home.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VM: ViewModel() {
    private val _data = MutableLiveData<String>()
    private val _uid = MutableLiveData<String>()



    val uid : LiveData<String>
        get() = _uid

    fun setUid(str:String){
        _uid.value = str
    }

    val data : LiveData<String>
        get() = _data


    fun setData(str: String){
        _data.value = str
    }
}