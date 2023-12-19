package com.sdapps.entres.home.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sdapps.entres.home.ordertaking.food.FoodBO

class VM: ViewModel() {
    private val _data = MutableLiveData<String>()

    val data : LiveData<String>
        get() = _data


    fun setData(str: String){
        _data.value = str
    }

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    private val _filteredData = MutableLiveData<List<FoodBO>>()
    val filteredData: LiveData<List<FoodBO>> get() = _filteredData

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilteredData(data: List<FoodBO>) {
        _filteredData.value = data
    }
}